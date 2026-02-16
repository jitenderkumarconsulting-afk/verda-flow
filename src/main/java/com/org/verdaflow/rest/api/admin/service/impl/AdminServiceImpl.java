package com.org.verdaflow.rest.api.admin.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.verdaflow.rest.api.admin.AdminBuilder;
import com.org.verdaflow.rest.api.admin.form.MasterCategoryForm;
import com.org.verdaflow.rest.api.admin.form.MasterEffectForm;
import com.org.verdaflow.rest.api.admin.form.MasterEtaForm;
import com.org.verdaflow.rest.api.admin.form.MasterTypeForm;
import com.org.verdaflow.rest.api.admin.form.RegisterDispatcherForm;
import com.org.verdaflow.rest.api.admin.model.MasterCategoryModel;
import com.org.verdaflow.rest.api.admin.model.MasterEffectModel;
import com.org.verdaflow.rest.api.admin.model.MasterEtaModel;
import com.org.verdaflow.rest.api.admin.model.MasterTypeModel;
import com.org.verdaflow.rest.api.admin.model.OrderListWithUserCustomerModel;
import com.org.verdaflow.rest.api.admin.model.OrderListWithUserDispatcherModel;
import com.org.verdaflow.rest.api.admin.model.UserDetailModel;
import com.org.verdaflow.rest.api.admin.model.UserDriverListWithUserDispatcherModel;
import com.org.verdaflow.rest.api.admin.service.AdminService;
import com.org.verdaflow.rest.api.customer.CustomerBuilder;
import com.org.verdaflow.rest.api.dispatcher.DispatcherBuilder;
import com.org.verdaflow.rest.api.dispatcher.model.GraphCountModel;
import com.org.verdaflow.rest.api.dispatcher.model.ProductCountGraphModel;
import com.org.verdaflow.rest.api.dispatcher.model.ProductCountGraphModelForWeekOrMonthOrYear;
import com.org.verdaflow.rest.api.driver.DriverBuilder;
import com.org.verdaflow.rest.api.user.UserBuilder;
import com.org.verdaflow.rest.api.user.form.ChangePasswordForm;
import com.org.verdaflow.rest.api.user.model.OrderModel;
import com.org.verdaflow.rest.common.enums.ApplicationStatus;
import com.org.verdaflow.rest.config.common.AppConst;
import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.config.security.jwt.JwtUser;
import com.org.verdaflow.rest.dto.PaginatedResponse;
import com.org.verdaflow.rest.entity.MasterCategory;
import com.org.verdaflow.rest.entity.MasterEffect;
import com.org.verdaflow.rest.entity.MasterEta;
import com.org.verdaflow.rest.entity.MasterType;
import com.org.verdaflow.rest.entity.Order;
import com.org.verdaflow.rest.entity.UserEntity;
import com.org.verdaflow.rest.error.AppException;
import com.org.verdaflow.rest.repo.MasterCategoryRepo;
import com.org.verdaflow.rest.repo.MasterEffectRepo;
import com.org.verdaflow.rest.repo.MasterEtaRepo;
import com.org.verdaflow.rest.repo.MasterTypeRepo;
import com.org.verdaflow.rest.repo.OrderRepo;
import com.org.verdaflow.rest.repo.ProductAggregateDetailRepo;
import com.org.verdaflow.rest.repo.UserEntityRepo;
import com.org.verdaflow.rest.util.AppUtil;
import com.org.verdaflow.rest.util.DateUtil;

@Service
public class AdminServiceImpl implements AdminService {
	public static final Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);

	@Autowired
	private AdminBuilder adminBuilder;

	@Autowired
	private UserEntityRepo userEntityRepo;

	@Autowired
	private AppUtil appUtil;

	@Autowired
	private UserBuilder userBuilder;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private MasterCategoryRepo masterCategoryRepo;

	@Autowired
	private MasterEffectRepo masterEffectRepo;

	@Autowired
	private DispatcherBuilder dispatcherBuilder;

	@Autowired
	private MasterTypeRepo masterTypeRepo;

	@Autowired
	private MasterEtaRepo masterEtaRepo;

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private CustomerBuilder customerBuilder;

	@Autowired
	private DriverBuilder driverBuilder;

	@Autowired
	private ProductAggregateDetailRepo productAggregateDetailRepo;

	@Override
	@Transactional
	public boolean changePassword(ChangePasswordForm changePasswordForm, JwtUser jwtUser) {
		log.info("changePassword");
		if (!passwordEncoder.matches(changePasswordForm.getCurrentPass(), jwtUser.getUserEntity().getPassword())) {
			throw new AppException(StringConst.INCORRECT_PASWORD, AppConst.EXCEPTION_CAT.BAD_REQUEST_400);
		}

		if (changePasswordForm.getCurrentPass().equals(changePasswordForm.getNewPass()))
			throw new AppException(StringConst.OLD_AND_NEW_MISS_MATCH, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		userBuilder.changePassword(changePasswordForm, jwtUser.getUserEntity());

		return true;
	}

	@Override
	@Transactional
	public boolean registerDispatcher(RegisterDispatcherForm registerDispatcherForm, JwtUser jwtUser) {
		log.info("registerDispatcher");

		UserEntity userEntity = userEntityRepo.checkUserExistenceByEmailAndRole(registerDispatcherForm.getEmail(),
				AppConst.USER_ROLE.DISPATCHER);
		if (null != userEntity)
			throw new AppException(StringConst.EMAIL_ALREADY_EXIST, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
		log.info("registerDispatcher :: userEntity " + userEntity);

		userEntity = userEntityRepo.checkUserExistenceByMobileNumberAndRole(registerDispatcherForm.getCountryCode(),
				registerDispatcherForm.getMobileNumber(), AppConst.USER_ROLE.DISPATCHER);
		if (null != userEntity)
			throw new AppException(StringConst.MOBILE_ALREADY_EXIST, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
		log.info("registerDispatcher :: userEntity " + userEntity);

		String password = appUtil.generateRandomAlphaNumericCode(AppConst.NUMBER.EIGHT);

		userEntity = adminBuilder.registerDispatcher(registerDispatcherForm, password);
		adminBuilder.welcomeDispatcherEmail(userEntity, password);

		return true;
	}

	@Override
	@Transactional
	public UserDetailModel updateDispatcherDetails(RegisterDispatcherForm registerDispatcherForm, JwtUser jwtUser) {
		log.info("updateDispatcherDetails");
		if (AppConst.NUMBER.ZERO == registerDispatcherForm.getId())
			throw new AppException(StringConst.UPDATE_DISPATCHER_ID_INVALID, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		UserEntity dipatcherUserEntity = userEntityRepo.findByUserIdAndRole(registerDispatcherForm.getId(),
				AppConst.USER_ROLE.DISPATCHER);
		if (null == dipatcherUserEntity)
			throw new AppException(StringConst.USER_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("updateDispatcherDetails :: dipatcherUserEntity " + dipatcherUserEntity);

		if (null == dipatcherUserEntity.getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		log.info("updateDispatcherDetails :: dipatcherUserEntity.getUserDispatcherDetail() "
				+ dipatcherUserEntity.getUserDispatcherDetail());

		dipatcherUserEntity = adminBuilder.updateUserDispatcherDetail(registerDispatcherForm, dipatcherUserEntity);
		log.info("updateDispatcherDetails :: dipatcherUserEntity " + dipatcherUserEntity);

		return dispatcherBuilder.createDispatcherUserDetailModel(dipatcherUserEntity);
	}

	@Override
	@Transactional
	public PaginatedResponse<UserDetailModel> listDispatcherUsers(Pageable pageable, String query, int filter,
			JwtUser jwtUser) {
		log.info("listDispatcherUsers");
		Page<UserEntity> userEntities = null;

		if (null == query || StringUtils.isBlank(query)) {
			if (AppConst.NUMBER.ZERO == filter) {
				// all dispatcher users
				userEntities = userEntityRepo.findAllDispatcherUsers(pageable);
			} else if (AppConst.NUMBER.ONE == filter) {
				// all approved dispatcher users
				userEntities = userEntityRepo.findAllDispatcherUsersByApplicationStatus(pageable,
						ApplicationStatus.APPROVED);
			} else if (AppConst.NUMBER.TWO == filter) {
				// all pending dispatcher users
				userEntities = userEntityRepo.findAllDispatcherUsersByApplicationStatus(pageable,
						ApplicationStatus.PENDING);
			} else if (AppConst.NUMBER.THREE == filter) {
				// all rejected dispatcher users
				userEntities = userEntityRepo.findAllDispatcherUsersByApplicationStatus(pageable,
						ApplicationStatus.REJECTED);
			} else {
				throw new AppException(StringConst.INVALID_FILTER, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
			}
		} else {
			String searchQuery = new StringBuilder(StringConst.PERCENTILE).append(appUtil.sanatizeQuery(query))
					.append(StringConst.PERCENTILE).toString();

			if (AppConst.NUMBER.ZERO == filter) {
				// all dispatcher users
				userEntities = userEntityRepo.findAllDispatcherUsersSearch(pageable, searchQuery);
			} else if (AppConst.NUMBER.ONE == filter) {
				// all approved dispatcher users
				userEntities = userEntityRepo.findAllDispatcherUsersByApplicationStatusSearch(pageable,
						ApplicationStatus.APPROVED, searchQuery);
			} else if (AppConst.NUMBER.TWO == filter) {
				// all pending dispatcher users
				userEntities = userEntityRepo.findAllDispatcherUsersByApplicationStatusSearch(pageable,
						ApplicationStatus.PENDING, searchQuery);
			} else if (AppConst.NUMBER.THREE == filter) {
				// all rejected dispatcher users
				userEntities = userEntityRepo.findAllDispatcherUsersByApplicationStatusSearch(pageable,
						ApplicationStatus.REJECTED, searchQuery);
			} else {
				throw new AppException(StringConst.INVALID_FILTER, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
			}
		}
		log.info("listDispatcherUsers :: userEntities " + userEntities);

		List<UserDetailModel> userDetailModels = new ArrayList<>();
		if (!userEntities.getContent().isEmpty()) {
			userDetailModels = userEntities.getContent().stream().filter(predicate -> !predicate.isDeleted())
					.map(userEntity -> dispatcherBuilder.createDispatcherUserDetailModel(userEntity))
					.collect(Collectors.toList());
		}
		log.info("listDispatcherUsers :: userDetailModels " + userDetailModels);

		int nextPage;
		if (userEntities.getContent().isEmpty()
				|| userEntities.getTotalPages() == pageable.getPageNumber() + AppConst.NUMBER.ONE
				|| userEntities.getTotalPages() == AppConst.NUMBER.ONE) {
			nextPage = -AppConst.NUMBER.ONE;
		} else {
			nextPage = (pageable.getPageNumber() + AppConst.NUMBER.ONE) + AppConst.NUMBER.ONE;
		}
		log.info("listDispatcherUsers :: nextPage " + nextPage);
		return new PaginatedResponse<>(userDetailModels, (int) userEntities.getTotalPages(),
				userEntities.getTotalElements(), nextPage);
	}

	@Override
	@Transactional
	public UserDetailModel getUserDispatcherDetails(int dispatcherUserId, JwtUser jwtUser) {
		log.info("getUserDispatcherDetails");
		UserEntity dispatcherUserEntity = userEntityRepo.findByUserIdAndRole(dispatcherUserId,
				AppConst.USER_ROLE.DISPATCHER);
		if (null == dispatcherUserEntity)
			throw new AppException(StringConst.USER_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("getUserDispatcherDetails :: dispatcherUserEntity " + dispatcherUserEntity);

		if (null == dispatcherUserEntity.getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		log.info("updateDispatcherDetails :: dipatcherUserEntity.getUserDispatcherDetail() "
				+ dispatcherUserEntity.getUserDispatcherDetail());
		return adminBuilder.createDispatcherUserDetailModelWithDriverAndOrdersCount(dispatcherUserEntity);
	}

	@Override
	@Transactional
	public UserDriverListWithUserDispatcherModel listDriverUsersForDispatcher(Pageable pageable, String query,
			int dispatcherUserId, int filter, JwtUser jwtUser) {
		log.info("listDriverUsersForDispatcher");
		UserEntity dispatcherUserEntity = userEntityRepo.findByUserIdAndRole(dispatcherUserId,
				AppConst.USER_ROLE.DISPATCHER);
		if (null == dispatcherUserEntity)
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		if (null == dispatcherUserEntity.getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		Page<UserEntity> userEntities = null;

		if (null == query || StringUtils.isBlank(query)) {
			if (AppConst.NUMBER.ZERO == filter) {
				// all driver users
				userEntities = userEntityRepo.findAllDriverUsersByDispatcherId(pageable,
						dispatcherUserEntity.getUserDispatcherDetail().getId());
			} else if (AppConst.NUMBER.ONE == filter) {
				// all approved driver users
				userEntities = userEntityRepo.findAllDriverUsersByDispatcherIdAndApplicationStatus(pageable,
						dispatcherUserEntity.getUserDispatcherDetail().getId(), ApplicationStatus.APPROVED);
			} else if (AppConst.NUMBER.TWO == filter) {
				// all pending driver users
				userEntities = userEntityRepo.findAllDriverUsersByDispatcherIdAndApplicationStatus(pageable,
						dispatcherUserEntity.getUserDispatcherDetail().getId(), ApplicationStatus.PENDING);
			} else if (AppConst.NUMBER.THREE == filter) {
				// all rejected driver users
				userEntities = userEntityRepo.findAllDriverUsersByDispatcherIdAndApplicationStatus(pageable,
						dispatcherUserEntity.getUserDispatcherDetail().getId(), ApplicationStatus.REJECTED);
			} else {
				throw new AppException(StringConst.INVALID_FILTER, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
			}
		} else {
			String searchQuery = new StringBuilder(StringConst.PERCENTILE).append(appUtil.sanatizeQuery(query))
					.append(StringConst.PERCENTILE).toString();

			if (AppConst.NUMBER.ZERO == filter) {
				// all driver users
				userEntities = userEntityRepo.findAllDriverUsersByDispatcherIdSearch(pageable,
						dispatcherUserEntity.getUserDispatcherDetail().getId(), searchQuery);
			} else if (AppConst.NUMBER.ONE == filter) {
				// all approved driver users
				userEntities = userEntityRepo.findAllDriverUsersByDispatcherIdAndApplicationStatusSearch(pageable,
						dispatcherUserEntity.getUserDispatcherDetail().getId(), ApplicationStatus.APPROVED,
						searchQuery);
			} else if (AppConst.NUMBER.TWO == filter) {
				// all pending driver users
				userEntities = userEntityRepo.findAllDriverUsersByDispatcherIdAndApplicationStatusSearch(pageable,
						dispatcherUserEntity.getUserDispatcherDetail().getId(), ApplicationStatus.PENDING, searchQuery);
			} else if (AppConst.NUMBER.THREE == filter) {
				// all rejected driver users
				userEntities = userEntityRepo.findAllDriverUsersByDispatcherIdAndApplicationStatusSearch(pageable,
						dispatcherUserEntity.getUserDispatcherDetail().getId(), ApplicationStatus.REJECTED,
						searchQuery);
			} else {
				throw new AppException(StringConst.INVALID_FILTER, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
			}
		}
		log.info("listDriverUsersForDispatcher :: userEntities " + userEntities);

		List<UserDetailModel> userDetailModels = new ArrayList<>();
		if (!userEntities.getContent().isEmpty()) {
			userDetailModels = userEntities.getContent().stream().filter(predicate -> !predicate.isDeleted())
					.map(userEntity -> driverBuilder.createDriverUserDetailModel(userEntity))
					.collect(Collectors.toList());
		}
		log.info("listDriverUsersForDispatcher :: userDetailModels " + userDetailModels);

		int nextPage;
		if (userEntities.getContent().isEmpty()
				|| userEntities.getTotalPages() == pageable.getPageNumber() + AppConst.NUMBER.ONE
				|| userEntities.getTotalPages() == AppConst.NUMBER.ONE) {
			nextPage = -AppConst.NUMBER.ONE;
		} else {
			nextPage = (pageable.getPageNumber() + AppConst.NUMBER.ONE) + AppConst.NUMBER.ONE;
		}
		log.info("listDriverUsersForDispatcher :: nextPage " + nextPage);

		return new UserDriverListWithUserDispatcherModel(
				new PaginatedResponse<>(userDetailModels, (int) userEntities.getTotalPages(),
						userEntities.getTotalElements(), nextPage),
				dispatcherBuilder.createDispatcherUserDetailModel(dispatcherUserEntity));
	}

	@Override
	@Transactional
	public boolean createMasterCategory(MasterCategoryForm categoryForm, JwtUser jwtUser) {
		log.info("createMasterCategory");
		if (masterCategoryRepo.checkCategoryExistenceByName(categoryForm.getName()) > 0)
			throw new AppException(StringConst.CATEGORY_ALREADY_EXIST, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		adminBuilder.saveMasterCategory(categoryForm);

		return true;
	}

	@Override
	@Transactional
	public boolean updateMasterCategory(MasterCategoryForm categoryForm, JwtUser jwtUser) {
		log.info("updateMasterCategory");
		if (AppConst.NUMBER.ZERO == categoryForm.getId())
			throw new AppException(StringConst.UPDATE_CATEGORY_ID_INVALID, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		MasterCategory masterCategory = masterCategoryRepo.findByCategoryId(categoryForm.getId());
		if (null == masterCategory)
			throw new AppException(StringConst.CATEGORY_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("updateMasterCategory :: masterCategory " + masterCategory);

		if (masterCategoryRepo.checkCategoryExistenceByNameExceptCurrent(categoryForm.getName(),
				categoryForm.getId()) > 0)
			throw new AppException(StringConst.CATEGORY_ALREADY_EXIST, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		adminBuilder.updateMasterCategory(categoryForm, masterCategory);

		return true;
	}

	@Override
	@Transactional
	public boolean deleteMasterCategory(int categoryId, JwtUser jwtUser) {
		log.info("deleteMasterCategory");
		MasterCategory masterCategory = masterCategoryRepo.findByCategoryId(categoryId);
		if (null == masterCategory)
			throw new AppException(StringConst.CATEGORY_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("deleteMasterCategory :: masterCategory " + masterCategory);

		adminBuilder.deleteMasterCategory(masterCategory);

		return true;
	}

	@Override
	@Transactional
	public PaginatedResponse<MasterCategoryModel> listMasterCategories(Pageable pageable, String query,
			JwtUser jwtUser) {
		log.info("listMasterCategories");
		Page<MasterCategory> masterCategories = null;

		if (null == query || StringUtils.isBlank(query)) {
			masterCategories = masterCategoryRepo.findAllCategories(pageable);
		} else {
			String searchQuery = new StringBuilder(StringConst.PERCENTILE).append(appUtil.sanatizeQuery(query))
					.append(StringConst.PERCENTILE).toString();

			masterCategories = masterCategoryRepo.findAllCategoriesSearch(pageable, searchQuery);
		}
		log.info("listMasterCategories :: masterCategories " + masterCategories);

		List<MasterCategoryModel> categoryModels = new ArrayList<>();

		if (!masterCategories.getContent().isEmpty()) {
			categoryModels = masterCategories.getContent().stream().filter(predicate -> !predicate.isDeleted())
					.map(masterCategory -> adminBuilder.createMasterCategoryModel(masterCategory))
					.collect(Collectors.toList());
		}

		log.info("listMasterCategories :: categoryModels " + categoryModels);

		int nextPage;
		if (masterCategories.getContent().isEmpty()
				|| masterCategories.getTotalPages() == pageable.getPageNumber() + AppConst.NUMBER.ONE
				|| masterCategories.getTotalPages() == AppConst.NUMBER.ONE) {
			nextPage = -AppConst.NUMBER.ONE;
		} else {
			nextPage = (pageable.getPageNumber() + AppConst.NUMBER.ONE) + AppConst.NUMBER.ONE;
		}
		log.info("listMasterCategories :: nextPage " + nextPage);

		return new PaginatedResponse<>(categoryModels, (int) masterCategories.getTotalPages(),
				masterCategories.getTotalElements(), nextPage);
	}

	@Override
	@Transactional
	public MasterCategoryModel getMasterCategory(int categoryId, JwtUser jwtUser) {
		log.info("getMasterCategory");
		MasterCategory masterCategory = masterCategoryRepo.findByCategoryId(categoryId);
		if (null == masterCategory)
			throw new AppException(StringConst.CATEGORY_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("getMasterCategory :: masterCategory " + masterCategory);

		return adminBuilder.createMasterCategoryModel(masterCategory);
	}

	@Override
	@Transactional
	public boolean createMasterEffect(MasterEffectForm masterEffectForm, JwtUser jwtUser) {
		log.info("createMasterEffect");
		if (masterEffectRepo.checkEffectExistenceByName(masterEffectForm.getName()) > 0)
			throw new AppException(StringConst.EFFECT_ALREADY_EXIST, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		adminBuilder.saveMasterEffect(masterEffectForm);

		return true;
	}

	@Override
	@Transactional
	public boolean updateMasterEffect(MasterEffectForm masterEffectForm, JwtUser jwtUser) {
		log.info("updateMasterEffect");
		if (AppConst.NUMBER.ZERO == masterEffectForm.getId())
			throw new AppException(StringConst.UPDATE_EFFECT_ID_INVALID, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		MasterEffect masterEffect = masterEffectRepo.findByEffectId(masterEffectForm.getId());
		if (null == masterEffect)
			throw new AppException(StringConst.EFFECT_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("updateMasterEffect :: masterEffect " + masterEffect);

		if (masterEffectRepo.checkEffectExistenceByNameExceptCurrent(masterEffectForm.getName(),
				masterEffectForm.getId()) > 0)
			throw new AppException(StringConst.EFFECT_ALREADY_EXIST, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		adminBuilder.updateMasterEffect(masterEffectForm, masterEffect);

		return true;
	}

	@Override
	@Transactional
	public boolean deleteMasterEffect(int effectId, JwtUser jwtUser) {
		log.info("deleteMasterEffect");
		MasterEffect masterEffect = masterEffectRepo.findByEffectId(effectId);
		if (null == masterEffect)
			throw new AppException(StringConst.EFFECT_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("deleteMasterEffect :: masterEffect " + masterEffect);

		adminBuilder.deleteMasterEffect(masterEffect);
		return true;
	}

	@Override
	@Transactional
	public PaginatedResponse<MasterEffectModel> listMasterEffects(Pageable pageable, String query, JwtUser jwtUser) {
		log.info("listMasterEffects");
		Page<MasterEffect> masterEffects = null;

		if (null == query || StringUtils.isBlank(query)) {
			masterEffects = masterEffectRepo.findAllEffects(pageable);
		} else {
			String searchQuery = new StringBuilder(StringConst.PERCENTILE).append(appUtil.sanatizeQuery(query))
					.append(StringConst.PERCENTILE).toString();

			masterEffects = masterEffectRepo.findAllEffectsSearch(pageable, searchQuery);
		}

		log.info("listMasterEffects :: masterEffects " + masterEffects);

		List<MasterEffectModel> effectModels = new ArrayList<>();

		if (!masterEffects.getContent().isEmpty()) {
			effectModels = masterEffects.getContent().stream().filter(predicate -> !predicate.isDeleted())
					.map(masterEffect -> adminBuilder.createMasterEffectModel(masterEffect))
					.collect(Collectors.toList());
		}

		log.info("listMasterEffects :: effectModels " + effectModels);

		int nextPage;
		if (masterEffects.getContent().isEmpty()
				|| masterEffects.getTotalPages() == pageable.getPageNumber() + AppConst.NUMBER.ONE
				|| masterEffects.getTotalPages() == AppConst.NUMBER.ONE) {
			nextPage = -AppConst.NUMBER.ONE;
		} else {
			nextPage = (pageable.getPageNumber() + AppConst.NUMBER.ONE) + AppConst.NUMBER.ONE;
		}
		log.info("listMasterEffects :: nextPage " + nextPage);
		return new PaginatedResponse<>(effectModels, (int) masterEffects.getTotalPages(),
				masterEffects.getTotalElements(), nextPage);

	}

	@Override
	@Transactional
	public MasterEffectModel getMasterEffect(int effectId, JwtUser jwtUser) {
		log.info("getMasterEffect");
		MasterEffect masterEffect = masterEffectRepo.findByEffectId(effectId);
		if (null == masterEffect)
			throw new AppException(StringConst.EFFECT_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("getMasterEffect :: masterEffect " + masterEffect);

		return adminBuilder.createMasterEffectModel(masterEffect);
	}

	@Override
	@Transactional
	public boolean createMasterType(MasterTypeForm masterTypeForm, JwtUser jwtUser) {
		log.info("createMasterType");
		if (masterTypeRepo.checkTypeExistenceByName(masterTypeForm.getName()) > 0)
			throw new AppException(StringConst.TYPE_ALREADY_EXIST, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		adminBuilder.saveMasterType(masterTypeForm);

		return true;
	}

	@Override
	@Transactional
	public boolean updateMasterType(MasterTypeForm masterTypeForm, JwtUser jwtUser) {
		log.info("updateMasterType");
		if (AppConst.NUMBER.ZERO == masterTypeForm.getId())
			throw new AppException(StringConst.UPDATE_TYPE_ID_INVALID, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		MasterType masterType = masterTypeRepo.findByTypeId(masterTypeForm.getId());
		if (null == masterType)
			throw new AppException(StringConst.TYPE_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("updateMasterType :: masterType " + masterType);

		if (masterTypeRepo.checkTypeExistenceByNameExceptCurrent(masterTypeForm.getName(), masterTypeForm.getId()) > 0)
			throw new AppException(StringConst.TYPE_ALREADY_EXIST, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		adminBuilder.updateMasterType(masterTypeForm, masterType);

		return true;

	}

	@Override
	@Transactional
	public boolean deleteMasterType(int typeId, JwtUser jwtUser) {
		log.info("deleteMasterType");
		MasterType masterType = masterTypeRepo.findByTypeId(typeId);
		if (null == masterType)
			throw new AppException(StringConst.TYPE_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("deleteMasterType :: masterType " + masterType);

		adminBuilder.deleteMasterType(masterType);
		return true;
	}

	@Override
	@Transactional
	public PaginatedResponse<MasterTypeModel> listMasterTypes(Pageable pageable, String query, JwtUser jwtUser) {
		log.info("listMasterTypes");
		Page<MasterType> masterTypes;
		if (null == query || StringUtils.isBlank(query)) {
			masterTypes = masterTypeRepo.findAllTypes(pageable);
		} else {
			String searchQuery = new StringBuilder(StringConst.PERCENTILE).append(appUtil.sanatizeQuery(query))
					.append(StringConst.PERCENTILE).toString();

			masterTypes = masterTypeRepo.findAllTypesSearch(pageable, searchQuery);
		}
		log.info("listMasterTypes :: masterTypes " + masterTypes);

		List<MasterTypeModel> typeModels = new ArrayList<>();

		if (!masterTypes.getContent().isEmpty()) {
			typeModels = masterTypes.getContent().stream().filter(predicate -> !predicate.isDeleted())
					.map(masterType -> adminBuilder.createMasterTypeModel(masterType)).collect(Collectors.toList());
		}

		log.info("listMasterTypes :: typeModels " + typeModels);

		int nextPage;
		if (masterTypes.getContent().isEmpty()
				|| masterTypes.getTotalPages() == pageable.getPageNumber() + AppConst.NUMBER.ONE
				|| masterTypes.getTotalPages() == AppConst.NUMBER.ONE) {
			nextPage = -AppConst.NUMBER.ONE;
		} else {
			nextPage = (pageable.getPageNumber() + AppConst.NUMBER.ONE) + AppConst.NUMBER.ONE;
		}
		log.info("listMasterTypes :: nextPage " + nextPage);
		return new PaginatedResponse<>(typeModels, (int) masterTypes.getTotalPages(), masterTypes.getTotalElements(),
				nextPage);

	}

	@Override
	@Transactional
	public MasterTypeModel getMasterType(int typeId, JwtUser jwtUser) {
		log.info("getMasterType");
		MasterType masterType = masterTypeRepo.findByTypeId(typeId);
		if (null == masterType)
			throw new AppException(StringConst.TYPE_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("getMasterType :: masterType " + masterType);

		return adminBuilder.createMasterTypeModel(masterType);
	}

	@Override
	@Transactional
	public boolean createMasterEta(MasterEtaForm masterEtaForm, JwtUser jwtUser) {
		log.info("createMasterEta");
		if (masterEtaRepo.checkEtaExistenceByName(masterEtaForm.getName()) > 0)
			throw new AppException(StringConst.ETA_ALREADY_EXIST, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		adminBuilder.saveMasterEta(masterEtaForm);

		return true;
	}

	@Override
	@Transactional
	public boolean updateMasterEta(MasterEtaForm masterEtaForm, JwtUser jwtUser) {
		log.info("updateMasterEta");

		if (AppConst.NUMBER.ZERO == masterEtaForm.getId())
			throw new AppException(StringConst.UPDATE_ETA_ID_INVALID, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		MasterEta masterEta = masterEtaRepo.findByEtaId(masterEtaForm.getId());
		if (null == masterEta)
			throw new AppException(StringConst.ETA_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("updateMasterEta :: masterEta " + masterEta);

		if (masterEtaRepo.checkEtaExistenceByNameExceptCurrent(masterEtaForm.getName(), masterEtaForm.getId()) > 0)
			throw new AppException(StringConst.ETA_ALREADY_EXIST, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		adminBuilder.updateMasterEta(masterEtaForm, masterEta);

		return true;

	}

	@Override
	@Transactional
	public boolean deleteMasterEta(int etaId, JwtUser jwtUser) {
		log.info("deleteMasterEta");
		MasterEta masterEta = masterEtaRepo.findByEtaId(etaId);
		if (null == masterEta)
			throw new AppException(StringConst.ETA_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("deleteMasterEta :: masterEta " + masterEta);

		return adminBuilder.deleteMasterEta(masterEta);
	}

	@Override
	@Transactional
	public PaginatedResponse<MasterEtaModel> listMasterEtas(Pageable pageable, String query, JwtUser jwtUser) {
		log.info("listMasterEtas");
		Page<MasterEta> masterEtas;
		if (null == query || StringUtils.isBlank(query)) {
			masterEtas = masterEtaRepo.findAllEtas(pageable);
		} else {
			String searchQuery = new StringBuilder(StringConst.PERCENTILE).append(appUtil.sanatizeQuery(query))
					.append(StringConst.PERCENTILE).toString();

			masterEtas = masterEtaRepo.findAllEtasSearch(pageable, searchQuery);
		}
		log.info("listMasterEtas :: masterEtas " + masterEtas);

		List<MasterEtaModel> etaModels = new ArrayList<>();

		if (!masterEtas.getContent().isEmpty()) {
			etaModels = masterEtas.getContent().stream().filter(predicate -> !predicate.isDeleted())
					.map(masterEta -> adminBuilder.createMasterEtaModel(masterEta)).collect(Collectors.toList());
		}

		log.info("listMasterEtas :: etaModels " + etaModels);
		int nextPage;
		if (masterEtas.getContent().isEmpty()
				|| masterEtas.getTotalPages() == pageable.getPageNumber() + AppConst.NUMBER.ONE
				|| masterEtas.getTotalPages() == AppConst.NUMBER.ONE) {
			nextPage = -AppConst.NUMBER.ONE;
		} else {
			nextPage = (pageable.getPageNumber() + AppConst.NUMBER.ONE) + AppConst.NUMBER.ONE;
		}
		log.info("listMasterEtas :: nextPage " + nextPage);
		return new PaginatedResponse<>(etaModels, (int) masterEtas.getTotalPages(), masterEtas.getTotalElements(),
				nextPage);
	}

	@Override
	@Transactional
	public MasterEtaModel getMasterEta(int etaId, JwtUser jwtUser) {
		log.info("getMasterEta");
		MasterEta masterEta = masterEtaRepo.findByEtaId(etaId);
		if (null == masterEta)
			throw new AppException(StringConst.ETA_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("getMasterEta :: masterEta " + masterEta);

		return adminBuilder.createMasterEtaModel(masterEta);
	}

	@Override
	@Transactional
	public boolean logout(JwtUser jwtUser) {
		log.info("logout");
		adminBuilder.logout(jwtUser.getUserEntity());
		return true;
	}

	@Override
	@Transactional
	public UserDetailModel activateDispatcher(int dispatcherUserId, boolean deact, JwtUser jwtUser) {
		log.info("activateDispatcher");
		UserEntity dispatcherUserEntity = userEntityRepo.findByUserIdAndRole(dispatcherUserId,
				AppConst.USER_ROLE.DISPATCHER);
		if (null == dispatcherUserEntity)
			throw new AppException(StringConst.USER_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("activateDispatcher :: dispatcherUserEntity " + dispatcherUserEntity);

		if (null == dispatcherUserEntity.getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		if (orderRepo
				.findNewAndPendingOrdersCountForDispatcher(dispatcherUserEntity.getUserDispatcherDetail().getId()) > 0)
			throw new AppException(StringConst.DISPATCHER_DEACTIVATE_REJECT, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		dispatcherUserEntity = adminBuilder.activateDispatcher(dispatcherUserEntity, deact);

		adminBuilder.activateDispatcherEmail(dispatcherUserEntity, deact);

		return dispatcherBuilder.createDispatcherUserDetailModel(dispatcherUserEntity);
	}

	@Override
	@Transactional
	public PaginatedResponse<UserDetailModel> listCustomerUsers(Pageable pageable, String query, int filter,
			JwtUser jwtUser) {
		log.info("listCustomerUsers");

		Page<UserEntity> userEntities = null;
		if (null == query || StringUtils.isBlank(query)) {
			if (AppConst.NUMBER.ZERO == filter) {
				// all customer users
				userEntities = userEntityRepo.findAllCustomerUsers(pageable);
			} else if (AppConst.NUMBER.ONE == filter) {
				// all approved customer users
				userEntities = userEntityRepo.findAllCustomerUsersByApplicationStatus(pageable,
						ApplicationStatus.APPROVED);
			} else if (AppConst.NUMBER.TWO == filter) {
				// all pending customer users
				userEntities = userEntityRepo.findAllCustomerUsersByApplicationStatus(pageable,
						ApplicationStatus.PENDING);
			} else if (AppConst.NUMBER.THREE == filter) {
				// all rejected customer users
				userEntities = userEntityRepo.findAllCustomerUsersByApplicationStatus(pageable,
						ApplicationStatus.REJECTED);
			} else {
				throw new AppException(StringConst.INVALID_FILTER, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
			}
		} else {
			String searchQuery = new StringBuilder(StringConst.PERCENTILE).append(appUtil.sanatizeQuery(query))
					.append(StringConst.PERCENTILE).toString();

			if (AppConst.NUMBER.ZERO == filter) {
				// all customer users
				userEntities = userEntityRepo.findAllCustomerUsersSearch(pageable, searchQuery);
			} else if (AppConst.NUMBER.ONE == filter) {
				// all approved customer users
				userEntities = userEntityRepo.findAllCustomerUsersByApplicationStatusSearch(pageable,
						ApplicationStatus.APPROVED, searchQuery);
			} else if (AppConst.NUMBER.TWO == filter) {
				// all pending customer users
				userEntities = userEntityRepo.findAllCustomerUsersByApplicationStatusSearch(pageable,
						ApplicationStatus.PENDING, searchQuery);
			} else if (AppConst.NUMBER.THREE == filter) {
				// all rejected customer users
				userEntities = userEntityRepo.findAllCustomerUsersByApplicationStatusSearch(pageable,
						ApplicationStatus.REJECTED, searchQuery);
			} else {
				throw new AppException(StringConst.INVALID_FILTER, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
			}
		}

		log.info("listCustomerUsers :: userEntities " + userEntities);

		List<UserDetailModel> userDetailModels = new ArrayList<>();
		if (!userEntities.getContent().isEmpty()) {
			userDetailModels = userEntities.getContent().stream().filter(predicate -> !predicate.isDeleted())
					.map(userEntity -> adminBuilder.createCustomerUserDetailModel(userEntity))
					.collect(Collectors.toList());
		}
		log.info("listCustomerUsers :: userDetailModels " + userDetailModels);

		int nextPage;
		if (userEntities.getContent().isEmpty()
				|| userEntities.getTotalPages() == pageable.getPageNumber() + AppConst.NUMBER.ONE
				|| userEntities.getTotalPages() == AppConst.NUMBER.ONE) {
			nextPage = -AppConst.NUMBER.ONE;
		} else {
			nextPage = (pageable.getPageNumber() + AppConst.NUMBER.ONE) + AppConst.NUMBER.ONE;
		}
		log.info("listCustomerUsers :: nextPage " + nextPage);
		return new PaginatedResponse<>(userDetailModels, (int) userEntities.getTotalPages(),
				userEntities.getTotalElements(), nextPage);

	}

	@Override
	@Transactional
	public UserDetailModel activateCustomer(int customerUserId, boolean deact, JwtUser jwtUser) {
		log.info("activateCustomer");
		UserEntity customerUserEntity = userEntityRepo.findByUserIdAndRole(customerUserId, AppConst.USER_ROLE.CUSTOMER);
		if (null == customerUserEntity)
			throw new AppException(StringConst.USER_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("activateCustomer :: customerUserEntity " + customerUserEntity);

		if (null == customerUserEntity.getUserCustomerDetail())
			throw new AppException(StringConst.CUSTOMER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		customerUserEntity = adminBuilder.activateCustomer(customerUserEntity, deact);

		adminBuilder.activateCustomerEmail(customerUserEntity, deact);

		return adminBuilder.createCustomerUserDetailModel(customerUserEntity);
	}

	@Override
	@Transactional
	public OrderListWithUserDispatcherModel listOrdersForDispatcher(Pageable pageable, int dispatcherUserId,
			String query, int filter, JwtUser jwtUser) {
		log.info("listOrdersForDispatcher");
		UserEntity dispatcherUserEntity = userEntityRepo.findDispatcherByUserId(dispatcherUserId);
		if (null == dispatcherUserEntity || null == dispatcherUserEntity.getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		UserDetailModel dispatcherUser = dispatcherBuilder.createDispatcherUserDetailModel(dispatcherUserEntity);
		log.info("createOrderModel :: dispatcherUser " + dispatcherUser);

		Page<Order> orders = null;
		if (null == query || StringUtils.isBlank(query)) {
			if (AppConst.NUMBER.ZERO == filter) {
				// All orders
				orders = orderRepo.findAllByDispatcherId(pageable,
						dispatcherUserEntity.getUserDispatcherDetail().getId());
			} else if (AppConst.NUMBER.ONE == filter) {
				// All new and pending orders
				orders = orderRepo.findAllNewAndPendingOrdersForDispatcher(pageable,
						dispatcherUserEntity.getUserDispatcherDetail().getId());
			} else if (AppConst.NUMBER.TWO == filter) {
				// All completed orders
				orders = orderRepo.findAllCompletedOrdersForDispatcher(pageable,
						dispatcherUserEntity.getUserDispatcherDetail().getId());
			} else {
				throw new AppException(StringConst.INVALID_FILTER, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
			}
		} else {
			String searchQuery = new StringBuilder(StringConst.PERCENTILE).append(appUtil.sanatizeQuery(query))
					.append(StringConst.PERCENTILE).toString();
			if (AppConst.NUMBER.ZERO == filter) {
				// All orders
				orders = orderRepo.findAllByDispatcherIdSearch(pageable,
						dispatcherUserEntity.getUserDispatcherDetail().getId(), searchQuery);
			} else if (AppConst.NUMBER.ONE == filter) {
				// All new and pending orders
				orders = orderRepo.findAllNewAndPendingOrdersForDispatcherSearch(pageable,
						dispatcherUserEntity.getUserDispatcherDetail().getId(), searchQuery);
			} else if (AppConst.NUMBER.TWO == filter) {
				// All completed orders
				orders = orderRepo.findAllCompletedOrdersForDispatcherSearch(pageable,
						dispatcherUserEntity.getUserDispatcherDetail().getId(), searchQuery);
			} else {
				throw new AppException(StringConst.INVALID_FILTER, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
			}
		}
		List<OrderModel> orderModels = new ArrayList<>();

		if (!orders.getContent().isEmpty()) {
			log.info("listOrdersForDispatcher :: orders " + orders);
			orderModels = orders.getContent().stream().filter(predicate -> !predicate.isDeleted())
					.map(order -> adminBuilder.createOrderModelForDispatcher(order)).collect(Collectors.toList());
		}
		log.info("listOrdersForDispatcher :: orderModels " + orderModels);

		int nextPage;
		if (orders.getContent().isEmpty() || orders.getTotalPages() == pageable.getPageNumber() + AppConst.NUMBER.ONE
				|| orders.getTotalPages() == AppConst.NUMBER.ONE) {
			nextPage = -AppConst.NUMBER.ONE;
		} else {
			nextPage = (pageable.getPageNumber() + AppConst.NUMBER.ONE) + AppConst.NUMBER.ONE;
		}
		return new OrderListWithUserDispatcherModel(
				new PaginatedResponse<>(orderModels, (int) orders.getTotalPages(), orders.getTotalElements(), nextPage),
				dispatcherBuilder.createDispatcherUserDetailModel(dispatcherUserEntity));
	}

	@Override
	@Transactional
	public OrderListWithUserCustomerModel listOrdersOfCustomer(Pageable pageable, int customerUserId, int filter,
			JwtUser jwtUser) {
		log.info("listOrdersOfCustomer");
		UserEntity customerUserEntity = userEntityRepo.findCustomerByUserId(customerUserId);
		if (null == customerUserEntity || null == customerUserEntity.getUserCustomerDetail())
			throw new AppException(StringConst.CUSTOMER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		// Page<Order> orders = orderRepo.findAllByCustomerId(pageable,
		// customerUserEntity.getId());

		Page<Order> orders = null;
		if (AppConst.NUMBER.ZERO == filter) {
			// All orders
			orders = orderRepo.findAllByUserId(pageable, customerUserEntity.getId());
		} else if (AppConst.NUMBER.ONE == filter) {
			// All new orders
			orders = orderRepo.findAllNewOrdersForCustomer(pageable, customerUserEntity.getId());
		} else if (AppConst.NUMBER.TWO == filter) {
			// All pending orders
			orders = orderRepo.findAllPendingOrdersForCustomer(pageable, customerUserEntity.getId());
		} else if (AppConst.NUMBER.THREE == filter) {
			// All completed orders
			orders = orderRepo.findAllCompletedOrdersForCustomer(pageable, customerUserEntity.getId());
		} else {
			throw new AppException(StringConst.INVALID_FILTER, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
		}

		List<OrderModel> orderModels = new ArrayList<>();

		if (!orders.getContent().isEmpty()) {
			log.info("listOrdersOfCustomer :: orders " + orders);
			orderModels = orders.getContent().stream().filter(predicate -> !predicate.isDeleted())
					.map(order -> adminBuilder.createOrderModelForCustomer(order)).collect(Collectors.toList());
		}
		log.info("listOrdersOfCustomer :: orderModels " + orderModels);

		int nextPage;
		if (orders.getContent().isEmpty() || orders.getTotalPages() == pageable.getPageNumber() + AppConst.NUMBER.ONE
				|| orders.getTotalPages() == AppConst.NUMBER.ONE) {
			nextPage = -AppConst.NUMBER.ONE;
		} else {
			nextPage = (pageable.getPageNumber() + AppConst.NUMBER.ONE) + AppConst.NUMBER.ONE;
		}
		return new OrderListWithUserCustomerModel(
				new PaginatedResponse<>(orderModels, (int) orders.getTotalPages(), orders.getTotalElements(), nextPage),
				customerBuilder.createCustomerUserDetailModel(customerUserEntity));
	}

	@Override
	@Transactional
	public GraphCountModel graphProductSold(int dispatcherId, long startDate, long endDate, int filter,
			JwtUser jwtUser) {
		log.info("graphProductSold");
		UserEntity dipatcherUserEntity = userEntityRepo.findByUserIdAndRole(dispatcherId,
				AppConst.USER_ROLE.DISPATCHER);
		if (null == dipatcherUserEntity)
			throw new AppException(StringConst.USER_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("graphProductSold :: dipatcherUserEntity " + dipatcherUserEntity);

		if (null == dipatcherUserEntity.getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("graphProductSold :: dipatcherUserEntity.getUserDispatcherDetail() "
				+ dipatcherUserEntity.getUserDispatcherDetail());

		log.info("graphProductSold :: currentDate " + DateUtil.getCurrentDate());

		log.info("graphProductSold :: startDate " + startDate);
		log.info("graphProductSold :: endDate " + endDate);

		if (AppConst.NUMBER.ZERO == startDate || AppConst.NUMBER.ZERO == endDate)
			throw new AppException(StringConst.INVALID_DATE_FORMAT, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		Date fromDate = DateUtil.convertMillisecondsToDate(startDate);
		Date toDate = DateUtil.convertMillisecondsToDate(endDate);

		log.info("graphProductSold :: fromDate " + fromDate);
		log.info("graphProductSold :: toDate " + toDate);

		if (null == fromDate || null == toDate || fromDate.after(toDate) || fromDate.equals(toDate))
			throw new AppException(StringConst.INVALID_DATE_FORMAT, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		GraphCountModel graphCountModel = null;

		if (AppConst.NUMBER.ZERO == filter) {
			// Product count DateWise
			List<ProductCountGraphModel> productCountGraphModels = productAggregateDetailRepo
					.getProductsCountByDispatcherIdDateWise(fromDate, toDate,
							dipatcherUserEntity.getUserDispatcherDetail().getId());
			log.info("graphProductSold :: productCountGraphModels " + productCountGraphModels);

			graphCountModel = dispatcherBuilder.createProductsCountGraphModelDateWise(productCountGraphModels, fromDate,
					toDate, filter);
		} else {
			List<ProductCountGraphModelForWeekOrMonthOrYear> productCountGraphModelForWeekOrMonthOrYears = null;

			if (AppConst.NUMBER.ONE == filter) {
				if (DateUtil.getYear(fromDate) != DateUtil.getYear(toDate)
						|| DateUtil.getWEEK(fromDate) > DateUtil.getWEEK(toDate))
					throw new AppException(StringConst.START_DATEYEAR_OR_END_DATEYEAR_CANNOT_BE_DIFFERENT,
							AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

				// Product count Weekly
				productCountGraphModelForWeekOrMonthOrYears = productAggregateDetailRepo
						.getProductsCountByDispatcherIdWeekly(fromDate, toDate,
								dipatcherUserEntity.getUserDispatcherDetail().getId());
				log.info("graphProductSold :: productCountGraphModelForWeekOrMonthOrYears "
						+ productCountGraphModelForWeekOrMonthOrYears);

				graphCountModel = dispatcherBuilder.createProductsCountGraphModelWeekly(
						productCountGraphModelForWeekOrMonthOrYears, fromDate, toDate, filter);
			} else if (AppConst.NUMBER.TWO == filter) {
				if (DateUtil.getYear(fromDate) != DateUtil.getYear(toDate))
					throw new AppException(StringConst.START_DATEYEAR_OR_END_DATEYEAR_CANNOT_BE_DIFFERENT,
							AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

				// Product count Monthly
				productCountGraphModelForWeekOrMonthOrYears = productAggregateDetailRepo
						.getProductsCountByDispatcherIdMonthly(fromDate, toDate,
								dipatcherUserEntity.getUserDispatcherDetail().getId());
				log.info("graphProductSold :: productCountGraphModelForWeekOrMonthOrYears "
						+ productCountGraphModelForWeekOrMonthOrYears);

				graphCountModel = dispatcherBuilder.createProductsCountGraphModelMonthly(
						productCountGraphModelForWeekOrMonthOrYears, fromDate, toDate, filter);
			} else if (AppConst.NUMBER.THREE == filter) {
				// Product count Yearly
				productCountGraphModelForWeekOrMonthOrYears = productAggregateDetailRepo
						.getProductsCountByDispatcherIdYearly(fromDate, toDate,
								dipatcherUserEntity.getUserDispatcherDetail().getId());
				log.info("graphProductSold :: productCountGraphModelForWeekOrMonthOrYears "
						+ productCountGraphModelForWeekOrMonthOrYears);

				graphCountModel = dispatcherBuilder.createProductsCountGraphModelYearly(
						productCountGraphModelForWeekOrMonthOrYears, fromDate, toDate, filter);
			} else {
				throw new AppException(StringConst.INVALID_FILTER, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
			}
		}

		return graphCountModel;
	}

}
