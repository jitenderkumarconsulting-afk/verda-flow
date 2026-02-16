package com.org.verdaflow.rest.api.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.org.verdaflow.rest.api.admin.form.MasterCategoryForm;
import com.org.verdaflow.rest.api.admin.form.MasterEffectForm;
import com.org.verdaflow.rest.api.admin.form.MasterEtaForm;
import com.org.verdaflow.rest.api.admin.form.MasterTypeForm;
import com.org.verdaflow.rest.api.admin.form.RegisterDispatcherForm;
import com.org.verdaflow.rest.api.admin.model.MasterCategoryModel;
import com.org.verdaflow.rest.api.admin.model.MasterEffectModel;
import com.org.verdaflow.rest.api.admin.model.MasterEtaModel;
import com.org.verdaflow.rest.api.admin.model.MasterTypeModel;
import com.org.verdaflow.rest.api.admin.model.UserDetailModel;
import com.org.verdaflow.rest.api.auth.AuthBuilder;
import com.org.verdaflow.rest.api.auth.model.UserCustomerDetailModel;
import com.org.verdaflow.rest.api.auth.model.UserDispatcherDetailModel;
import com.org.verdaflow.rest.api.customer.CustomerBuilder;
import com.org.verdaflow.rest.api.customer.model.CustomerAddressDetailModel;
import com.org.verdaflow.rest.api.dispatcher.DispatcherBuilder;
import com.org.verdaflow.rest.api.dispatcher.model.PromoCodeModel;
import com.org.verdaflow.rest.api.driver.DriverBuilder;
import com.org.verdaflow.rest.api.master.MasterBuilder;
import com.org.verdaflow.rest.api.master.model.MasterModel;
import com.org.verdaflow.rest.api.user.UserBuilder;
import com.org.verdaflow.rest.api.user.model.AuditOrderStatusModel;
import com.org.verdaflow.rest.api.user.model.OrderItemDetailModel;
import com.org.verdaflow.rest.api.user.model.OrderModel;
import com.org.verdaflow.rest.api.user.model.OrderPriceDetailModel;
import com.org.verdaflow.rest.api.user.model.OrderRatingDetailModel;
import com.org.verdaflow.rest.common.enums.ApplicationStatus;
import com.org.verdaflow.rest.common.model.UserRoleModel;
import com.org.verdaflow.rest.config.common.AppConst;
import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.entity.DeviceDetail;
import com.org.verdaflow.rest.entity.DriverOrderMapping;
import com.org.verdaflow.rest.entity.MasterCategory;
import com.org.verdaflow.rest.entity.MasterEffect;
import com.org.verdaflow.rest.entity.MasterEta;
import com.org.verdaflow.rest.entity.MasterType;
import com.org.verdaflow.rest.entity.Order;
import com.org.verdaflow.rest.entity.UserDispatcherDetail;
import com.org.verdaflow.rest.entity.UserEntity;
import com.org.verdaflow.rest.entity.UserRoleMapping;
import com.org.verdaflow.rest.error.AppException;
import com.org.verdaflow.rest.event.ActivateCustomerEvent;
import com.org.verdaflow.rest.event.ActivateDispatcherEvent;
import com.org.verdaflow.rest.event.DeactivateCustomerEvent;
import com.org.verdaflow.rest.event.DeactivateDispatcherEvent;
import com.org.verdaflow.rest.event.WelcomeDispatcherEvent;
import com.org.verdaflow.rest.repo.DeviceDetailRepo;
import com.org.verdaflow.rest.repo.DriverOrderMappingRepo;
import com.org.verdaflow.rest.repo.MasterCategoryRepo;
import com.org.verdaflow.rest.repo.MasterEffectRepo;
import com.org.verdaflow.rest.repo.MasterEtaRepo;
import com.org.verdaflow.rest.repo.MasterTypeRepo;
import com.org.verdaflow.rest.repo.UserCustomerDetailRepo;
import com.org.verdaflow.rest.repo.UserDispatcherDetailRepo;
import com.org.verdaflow.rest.repo.UserEntityRepo;
import com.org.verdaflow.rest.util.AWSImageUpload;
import com.org.verdaflow.rest.util.AppUtil;

@Component
public class AdminBuilder {
	public static final Logger log = LoggerFactory.getLogger(AdminBuilder.class);

	@Autowired
	private UserEntityRepo userEntityRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserDispatcherDetailRepo userDispatcherDetailRepo;

	@Autowired
	private UserCustomerDetailRepo userCustomerDetailRepo;

	@Autowired
	private AppUtil appUtil;

	@Autowired
	private AWSImageUpload awsImageUpload;

	@Autowired
	private ApplicationEventMulticaster applicationEventMulticaster;

	@Autowired
	private AuthBuilder authBuilder;

	@Autowired
	private MasterCategoryRepo masterCategoryRepo;

	@Autowired
	private MasterEffectRepo masterEffectRepo;

	@Autowired
	private MasterTypeRepo masterTypeRepo;

	@Autowired
	private MasterEtaRepo masterEtaRepo;

	@Autowired
	private DeviceDetailRepo deviceDetailRepo;

	@Autowired
	private DispatcherBuilder dispatcherBuilder;

	@Autowired
	private CustomerBuilder customerBuilder;

	@Autowired
	private DriverOrderMappingRepo driverOrderMappingRepo;

	@Autowired
	private MasterBuilder masterBuilder;

	@Autowired
	private DriverBuilder driverBuilder;

	@Autowired
	private UserBuilder userBuilder;

	@Transactional
	public UserEntity registerDispatcher(RegisterDispatcherForm registerDispatcherForm, String password) {
		log.info("registerDispatcher");
		UserEntity userEntity = saveUserDetail(registerDispatcherForm, password);
		log.info("registerDispatcher :: userEntity " + userEntity);

		userEntity = saveUserDispatcherDetail(registerDispatcherForm, userEntity);

		return userEntity;
	}

	@Transactional
	public UserEntity saveUserDetail(RegisterDispatcherForm registerDispatcherForm, String password) {
		log.info("saveUserDetail");
		UserEntity userEntity = new UserEntity();
		userEntity.setEmail(registerDispatcherForm.getEmail());
		userEntity.setCountryCode(registerDispatcherForm.getCountryCode());
		userEntity.setMobileNumber(registerDispatcherForm.getMobileNumber());
		userEntity.setPassword(passwordEncoder.encode(password));
		userEntity.setActive(true);
		userEntityRepo.save(userEntity);

		log.info("saveUserDetail :: userEntity " + userEntity);

		// Set the User Role Mapping
		List<UserRoleMapping> userRoleMappings = authBuilder.saveUserRoleMapping(AppConst.USER_ROLE.DISPATCHER,
				userEntity, ApplicationStatus.APPROVED);
		userEntity.setUserRoleMappings(userRoleMappings);

		return userEntity;
	}

	@Transactional
	public UserEntity saveUserDispatcherDetail(RegisterDispatcherForm registerDispatcherForm, UserEntity userEntity) {
		log.info("saveUserDispatcherDetail");
		UserDispatcherDetail userDispatcherDetail = new UserDispatcherDetail();
		userDispatcherDetail.setStoreName(registerDispatcherForm.getStoreName());
		userDispatcherDetail.setManagerName(registerDispatcherForm.getManagerName());
		userDispatcherDetail.setAddress(registerDispatcherForm.getAddress());
		userDispatcherDetail.setLat(registerDispatcherForm.getLat());
		userDispatcherDetail.setLng(registerDispatcherForm.getLng());
		userDispatcherDetail.setApplicationStatus(ApplicationStatus.APPROVED);
		userDispatcherDetail.setActive(true);
		userDispatcherDetail.setUser(userEntity);

		MasterEta masterEta = masterEtaRepo.findByEtaId(registerDispatcherForm.getEtaId());
		if (null == masterEta)
			throw new AppException(StringConst.ETA_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("saveUserDispatcherDetail :: masterEta " + masterEta);
		userDispatcherDetail.setEta(masterEta);

		userDispatcherDetail.setDeliveryCharges(registerDispatcherForm.getDeliveryCharges());
		userDispatcherDetailRepo.save(userDispatcherDetail);

		log.info("saveUserDispatcherDetail :: userDispatcherDetail " + userDispatcherDetail);

		// save user dispatcher images
		userDispatcherDetail = saveUserDispatcherImages(registerDispatcherForm, userDispatcherDetail);

		userEntity.setUserDispatcherDetail(userDispatcherDetail);

		return userEntity;
	}

	@Transactional
	public UserDispatcherDetail saveUserDispatcherImages(RegisterDispatcherForm registerDispatcherForm,
			UserDispatcherDetail userDispatcherDetail) {
		log.info("saveUserDispatcherImages");
		if (null != registerDispatcherForm.getImage() && StringUtils.isNotBlank(registerDispatcherForm.getImage())
				&& null != registerDispatcherForm.getImageThumb()
				&& StringUtils.isNotBlank(registerDispatcherForm.getImageThumb())) {
			if (appUtil.validateBase64Image(registerDispatcherForm.getImage())
					&& appUtil.validateBase64Image(registerDispatcherForm.getImageThumb())) {
				String imageUrl = awsImageUpload.uploadFile(registerDispatcherForm.getImage(),
						userDispatcherDetail.getId(), AppConst.UPLOAD_TYPE.DISPATCHER, AppConst.IMAGE_TYPE.ORIGINAL);
				log.info("saveUserDispatcherImages :: imageUrl " + imageUrl);

				String imageThumbUrl = awsImageUpload.uploadFile(registerDispatcherForm.getImageThumb(),
						userDispatcherDetail.getId(), AppConst.UPLOAD_TYPE.DISPATCHER, AppConst.IMAGE_TYPE.THUMB);
				log.info("saveUserDispatcherImages :: imageThumbUrl " + imageThumbUrl);

				userDispatcherDetail.setImage(imageUrl);
				userDispatcherDetail.setImageThumb(imageThumbUrl);

				userDispatcherDetailRepo.save(userDispatcherDetail);
			}
		}

		return userDispatcherDetail;
	}

	@Transactional
	public boolean welcomeDispatcherEmail(UserEntity userEntity, String password) {
		log.info("welcomeDispatcherEmail");
		WelcomeDispatcherEvent welcomeDispatcherEvent = new WelcomeDispatcherEvent(this, userEntity.getEmail(),
				userEntity.getMobileNumber(), password, userEntity.getUserDispatcherDetail().getStoreName(),
				userEntity.getUserDispatcherDetail().getManagerName());

		applicationEventMulticaster.multicastEvent(welcomeDispatcherEvent);

		return true;
	}

	@Transactional
	public boolean activateDispatcherEmail(UserEntity userEntity, boolean deact) {
		log.info("activateDispatcherEmail");

		if (deact) {
			DeactivateDispatcherEvent deactivateDispatcherEvent = new DeactivateDispatcherEvent(this,
					userEntity.getEmail(), userEntity.getUserDispatcherDetail().getStoreName(),
					userEntity.getUserDispatcherDetail().getManagerName());

			applicationEventMulticaster.multicastEvent(deactivateDispatcherEvent);
		} else {
			ActivateDispatcherEvent activateDispatcherEvent = new ActivateDispatcherEvent(this, userEntity.getEmail(),
					userEntity.getUserDispatcherDetail().getStoreName(),
					userEntity.getUserDispatcherDetail().getManagerName());

			applicationEventMulticaster.multicastEvent(activateDispatcherEvent);
		}

		return true;
	}

	@Transactional
	public UserEntity updateUserDispatcherDetail(RegisterDispatcherForm registerDispatcherForm,
			UserEntity dipatcherUserEntity) {
		log.info("updateUserDispatcherDetail");

		if (userEntityRepo.checkUserExistenceByEmailAndRoleExceptCurrent(registerDispatcherForm.getEmail(),
				AppConst.USER_ROLE.DISPATCHER, dipatcherUserEntity.getId()) > 0)
			throw new AppException(StringConst.EMAIL_ALREADY_EXIST, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		dipatcherUserEntity.setEmail(registerDispatcherForm.getEmail());

		if (userEntityRepo.checkUserExistenceByMobileNumberAndRoleExceptCurrent(registerDispatcherForm.getCountryCode(),
				registerDispatcherForm.getMobileNumber(), AppConst.USER_ROLE.DISPATCHER,
				dipatcherUserEntity.getId()) > 0)
			throw new AppException(StringConst.MOBILE_ALREADY_EXIST, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		dipatcherUserEntity.setCountryCode(registerDispatcherForm.getCountryCode());
		dipatcherUserEntity.setMobileNumber(registerDispatcherForm.getMobileNumber());
		dipatcherUserEntity.getUserDispatcherDetail().setStoreName(registerDispatcherForm.getStoreName());
		dipatcherUserEntity.getUserDispatcherDetail().setManagerName(registerDispatcherForm.getManagerName());
		dipatcherUserEntity.getUserDispatcherDetail().setAddress(registerDispatcherForm.getAddress());
		dipatcherUserEntity.getUserDispatcherDetail().setLat(registerDispatcherForm.getLat());
		dipatcherUserEntity.getUserDispatcherDetail().setLng(registerDispatcherForm.getLng());

		MasterEta masterEta = masterEtaRepo.findByEtaId(registerDispatcherForm.getEtaId());
		if (null == masterEta)
			throw new AppException(StringConst.ETA_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("updateUserDispatcherDetail :: masterEta " + masterEta);
		dipatcherUserEntity.getUserDispatcherDetail().setEta(masterEta);

		dipatcherUserEntity.getUserDispatcherDetail().setDeliveryCharges(registerDispatcherForm.getDeliveryCharges());

		// save user dispatcher images
		dipatcherUserEntity = updateUserDispatcherImages(registerDispatcherForm, dipatcherUserEntity);

		// Here saving the updated detail for UserDispatcherDetail.
		userDispatcherDetailRepo.save(dipatcherUserEntity.getUserDispatcherDetail());

		log.info("updateUserDispatcherDetail :: dipatcherUserEntity.getUserDispatcherDetail() "
				+ dipatcherUserEntity.getUserDispatcherDetail());

		return dipatcherUserEntity;
	}

	@Transactional
	public UserEntity updateUserDispatcherImages(RegisterDispatcherForm registerDispatcherForm,
			UserEntity dipatcherUserEntity) {
		log.info("updateUserDispatcherImages");
		if (null != registerDispatcherForm.getImage() && StringUtils.isNotBlank(registerDispatcherForm.getImage())
				&& null != registerDispatcherForm.getImageThumb()
				&& StringUtils.isNotBlank(registerDispatcherForm.getImageThumb())) {
			if (appUtil.validateBase64Image(registerDispatcherForm.getImage())
					&& appUtil.validateBase64Image(registerDispatcherForm.getImageThumb())) {
				String imageUrl = awsImageUpload.uploadFile(registerDispatcherForm.getImage(),
						dipatcherUserEntity.getUserDispatcherDetail().getId(), AppConst.UPLOAD_TYPE.DISPATCHER,
						AppConst.IMAGE_TYPE.ORIGINAL);
				log.info("updateUserDispatcherImages :: imageUrl " + imageUrl);

				String imageThumbUrl = awsImageUpload.uploadFile(registerDispatcherForm.getImageThumb(),
						dipatcherUserEntity.getUserDispatcherDetail().getId(), AppConst.UPLOAD_TYPE.DISPATCHER,
						AppConst.IMAGE_TYPE.THUMB);
				log.info("updateUserDispatcherImages :: imageThumbUrl " + imageThumbUrl);

				dipatcherUserEntity.getUserDispatcherDetail().setImage(imageUrl);
				dipatcherUserEntity.getUserDispatcherDetail().setImageThumb(imageThumbUrl);
			}
		}

		return dipatcherUserEntity;
	}

	@Transactional
	public boolean saveMasterCategory(MasterCategoryForm categoryForm) {
		log.info("saveMasterCategory");
		MasterCategory masterCategory = new MasterCategory();
		masterCategory.setName(categoryForm.getName());
		masterCategory.setActive(true);

		masterCategoryRepo.save(masterCategory);

		log.info("saveMasterCategory :: masterCategory " + masterCategory);

		return true;
	}

	@Transactional
	public boolean updateMasterCategory(MasterCategoryForm categoryForm, MasterCategory masterCategory) {
		log.info("updateMasterCategory");
		masterCategory.setName(categoryForm.getName());

		// Here saving the updated detail for MasterCategory.
		masterCategoryRepo.save(masterCategory);

		log.info("updateMasterCategory :: masterCategory " + masterCategory);

		return true;
	}

	@Transactional
	public boolean deleteMasterCategory(MasterCategory masterCategory) {
		log.info("deleteMasterCategory");
		masterCategory.setDeleted(true);

		masterCategoryRepo.save(masterCategory);

		log.info("deleteMasterCategory :: masterCategory " + masterCategory);

		return true;
	}

	@Transactional
	public MasterCategoryModel createMasterCategoryModel(MasterCategory masterCategory) {
		log.info("createMasterCategoryModel");
		return new MasterCategoryModel(masterCategory.getId(), masterCategory.getName(), masterCategory.isActive(),
				masterCategory.getCreatedAt());
	}

	@Transactional
	public boolean saveMasterEffect(MasterEffectForm masterEffectForm) {
		log.info("saveMasterEffect");
		MasterEffect masterEffect = new MasterEffect();
		masterEffect.setName(masterEffectForm.getName());
		masterEffect.setActive(true);

		masterEffectRepo.save(masterEffect);

		log.info("saveMasterEffect :: masterEffect " + masterEffect);

		return true;

	}

	@Transactional
	public boolean updateMasterEffect(MasterEffectForm masterEffectForm, MasterEffect masterEffect) {
		log.info("updateMasterEffect");

		masterEffect.setName(masterEffectForm.getName());

		// Here saving the updated detail for MasterEffect.
		masterEffectRepo.save(masterEffect);

		log.info("updateMasterEffect :: masterEffect " + masterEffect);

		return true;
	}

	@Transactional
	public boolean deleteMasterEffect(MasterEffect masterEffect) {
		log.info("deleteMasterEffect");
		masterEffect.setDeleted(true);

		masterEffectRepo.save(masterEffect);

		log.info("deleteMasterEffect :: masterEffect " + masterEffect);

		return true;
	}

	@Transactional
	public MasterEffectModel createMasterEffectModel(MasterEffect masterEffect) {
		log.info("createMasterEffectModel");

		return new MasterEffectModel(masterEffect.getId(), masterEffect.getName(), masterEffect.isActive(),
				masterEffect.getCreatedAt());
	}

	@Transactional
	public boolean saveMasterType(MasterTypeForm masterTypeForm) {
		log.info("saveMasterType");
		MasterType masterType = new MasterType();
		masterType.setName(masterTypeForm.getName());
		masterType.setActive(true);

		masterTypeRepo.save(masterType);

		log.info("saveMasterType :: masterType " + masterType);

		return true;

	}

	@Transactional
	public boolean updateMasterType(MasterTypeForm masterTypeForm, MasterType masterType) {
		log.info("updateMasterType");

		masterType.setName(masterTypeForm.getName());

		// Here saving the updated detail for MasterType.
		masterTypeRepo.save(masterType);

		log.info("updateMasterType :: masterType " + masterType);

		return true;
	}

	@Transactional
	public boolean deleteMasterType(MasterType masterType) {
		log.info("deleteMasterType");
		masterType.setDeleted(true);

		masterTypeRepo.save(masterType);

		log.info("deleteMasterType :: masterType " + masterType);

		return true;
	}

	@Transactional
	public MasterTypeModel createMasterTypeModel(MasterType masterType) {
		log.info("createMasterTypeModel");

		return new MasterTypeModel(masterType.getId(), masterType.getName(), masterType.isActive(),
				masterType.getCreatedAt());
	}

	@Transactional
	public UserDetailModel createDispatcherUserDetailModelWithDriverAndOrdersCount(UserEntity userEntity) {
		log.info("createDispatcherUserDetailModelWithDriverAndOrdersCount");
		UserDispatcherDetailModel userDispatcherDetailModel = authBuilder
				.createDispatcherUserDetailModelWithDriverAndOrdersCount(userEntity.getUserDispatcherDetail());

		return new UserDetailModel(authBuilder.createUserModel(userEntity),
				userEntity.getUserRoleMappings().stream()
						.map(mapper -> new UserRoleModel(mapper.getMasterRole().getId(),
								String.valueOf(mapper.getMasterRole().getRole()), mapper.getApplicationStatus()))
						.collect(Collectors.toList()),
				userDispatcherDetailModel);
	}

	@Transactional
	public boolean saveMasterEta(MasterEtaForm masterEtaForm) {
		log.info("saveMasterEta");
		MasterEta masterEta = new MasterEta();
		masterEta.setName(masterEtaForm.getName());
		masterEta.setActive(true);

		masterEtaRepo.save(masterEta);

		log.info("saveMasterEta :: masterEta " + masterEta);

		return true;

	}

	@Transactional
	public boolean updateMasterEta(MasterEtaForm masterEtaForm, MasterEta masterEta) {
		log.info("updateMasterEta");

		masterEta.setName(masterEtaForm.getName());

		// Here saving the updated detail for MasterEta.
		masterEtaRepo.save(masterEta);

		log.info("updateMasterEta :: masterEta " + masterEta);

		return true;
	}

	@Transactional
	public boolean deleteMasterEta(MasterEta masterEta) {
		log.info("deleteMasterEta");
		masterEta.setDeleted(true);

		masterEtaRepo.save(masterEta);

		log.info("deleteMasterEta :: masterEta " + masterEta);

		return true;
	}

	@Transactional
	public MasterEtaModel createMasterEtaModel(MasterEta masterEta) {
		log.info("createMasterEtaModel");

		return new MasterEtaModel(masterEta.getId(), masterEta.getName(), masterEta.isActive(),
				masterEta.getCreatedAt());
	}

	@Transactional
	public boolean logout(UserEntity userEntity) {
		log.info("logout");
		List<DeviceDetail> deviceDetails = deviceDetailRepo.findByUserIdActive(userEntity.getId());
		log.info("logout :: deviceDetails " + deviceDetails);

		List<DeviceDetail> signDetailToDeviceDetailEntities = new ArrayList<>();

		if (!deviceDetails.isEmpty()) {
			for (DeviceDetail sessionEntity : deviceDetails) {
				sessionEntity.setDeleted(true);
				signDetailToDeviceDetailEntities.add(sessionEntity);
			}
			deviceDetailRepo.save(signDetailToDeviceDetailEntities);

		}

		return true;
	}

	@Transactional
	public UserEntity activateDispatcher(UserEntity userEntity, boolean deact) {
		log.info("activateDispatcher");
		if (deact) {
			userEntity.setActive(false);
			userEntity.getUserDispatcherDetail().setActive(false);
		} else {
			userEntity.setActive(true);
			userEntity.getUserDispatcherDetail().setActive(true);
		}

		userDispatcherDetailRepo.save(userEntity.getUserDispatcherDetail());
		userEntityRepo.save(userEntity);

		return userEntity;
	}

	@Transactional
	public UserDetailModel createCustomerUserDetailModel(UserEntity userEntity) {
		log.info("createCustomerUserDetailModel");
		UserCustomerDetailModel userCustomerDetailModel = authBuilder
				.createUserCustomerDetailModel(userEntity.getUserCustomerDetail());

		log.info("createCustomerUserDetailModel :: userCustomerDetailModel " + userCustomerDetailModel);

		return new UserDetailModel(authBuilder.createUserModel(userEntity),
				userEntity.getUserRoleMappings().stream()
						.map(mapper -> new UserRoleModel(mapper.getMasterRole().getId(),
								String.valueOf(mapper.getMasterRole().getRole()), mapper.getApplicationStatus()))
						.collect(Collectors.toList()),
				userCustomerDetailModel);
	}

	@Transactional
	public UserEntity activateCustomer(UserEntity userEntity, boolean deact) {
		log.info("activateCustomer");
		if (deact) {
			userEntity.setActive(false);
			userEntity.getUserCustomerDetail().setActive(false);
		} else {
			userEntity.setActive(true);
			userEntity.getUserCustomerDetail().setActive(true);
		}

		userCustomerDetailRepo.save(userEntity.getUserCustomerDetail());
		userEntityRepo.save(userEntity);

		return userEntity;
	}

	@Transactional
	public boolean activateCustomerEmail(UserEntity userEntity, boolean deact) {
		log.info("activateDispatcherEmail");
		if (deact) {
			DeactivateCustomerEvent deactivateCustomerEvent = new DeactivateCustomerEvent(this, userEntity.getEmail(),
					userEntity.getUserCustomerDetail().getFirstName(),
					userEntity.getUserCustomerDetail().getLastName());

			applicationEventMulticaster.multicastEvent(deactivateCustomerEvent);
		} else {
			ActivateCustomerEvent activateCustomerEvent = new ActivateCustomerEvent(this, userEntity.getEmail(),
					userEntity.getUserCustomerDetail().getFirstName(),
					userEntity.getUserCustomerDetail().getLastName());

			applicationEventMulticaster.multicastEvent(activateCustomerEvent);
		}

		return true;
	}

	@Transactional
	public OrderModel createOrderModelForCustomer(Order order) {
		log.info("createOrderModelForCustomer");
		UserDetailModel dispatcherUser = dispatcherBuilder
				.createDispatcherUserDetailModel(order.getUserDispatcherDetail().getUser());
		log.info("createOrderModelForCustomer :: dispatcherUser " + dispatcherUser);

		UserDetailModel driverUser = null;
		DriverOrderMapping driverOrderMapping = driverOrderMappingRepo.findByOrderId(order.getId());
		if (null != driverOrderMapping && !driverOrderMapping.isRejected())
			driverUser = driverBuilder.createDriverUserDetailModel(driverOrderMapping.getUserDriverDetail().getUser());
		log.info("createOrderModelForCustomer :: driverUser " + driverUser);

		PromoCodeModel promoCodeModel = null;
		if (null != order.getPromoCode())
			promoCodeModel = dispatcherBuilder.createPromoCodeModel(order.getPromoCode());
		log.info("createOrderModelForCustomer :: promoCodeModel " + promoCodeModel);

		CustomerAddressDetailModel customerAddressDetailModel = null;
		if (null != order.getAddressDetail()) {
			customerAddressDetailModel = dispatcherBuilder.createCustomerAddressModel(order.getAddressDetail());
			log.info("createOrderModelForCustomer :: customerAddressDetailModel " + customerAddressDetailModel);
		}

		List<OrderItemDetailModel> orderItemDetailModels = new ArrayList<>();
		if (null != order.getOrderItemDetails() && !order.getOrderItemDetails().isEmpty()) {
			orderItemDetailModels = order.getOrderItemDetails().stream()
					.map(orderItemDetail -> customerBuilder.createOrderItemDetailModel(orderItemDetail))
					.collect(Collectors.toList());
		}
		log.info("createOrderModelForCustomer ::orderItemDetailModels " + orderItemDetailModels);

		OrderPriceDetailModel orderPriceDetailModel = null;
		if (null != order.getOrderPriceDetail())
			orderPriceDetailModel = customerBuilder.createOrderPriceDetailModel(order.getOrderPriceDetail());
		log.info("createOrderModelForCustomer :: orderPriceDetailModel " + orderPriceDetailModel);

		OrderRatingDetailModel orderRatingDetailModel = null;
		if (null != order.getOrderRatingDetail()) {
			orderRatingDetailModel = customerBuilder.createOrderRatingDetailModel(order.getOrderRatingDetail());
			log.info("createOrderModelForCustomer :: orderRatingDetailModel " + orderRatingDetailModel);
		}

		MasterModel etaModel = null;
		if (null != order.getEta())
			etaModel = masterBuilder.createMasterModel(order.getEta());
		log.info("createOrderModelForCustomer :: etaModel " + etaModel);

		List<AuditOrderStatusModel> auditOrderStatusModels = new ArrayList<>();
		if (null != order.getAuditOrderStatus() && !order.getAuditOrderStatus().isEmpty()) {
			auditOrderStatusModels = order.getAuditOrderStatus().stream()
					.filter(auditOrderStatus -> !auditOrderStatus.isDeleted())
					.map(auditOrderStatus -> userBuilder.createAuditOrderStatusModel(auditOrderStatus))
					.collect(Collectors.toList());
			log.info("createOrderModelForCustomer :: auditOrderStatusModels " + auditOrderStatusModels);
		}

		return new OrderModel(order.getId(), order.getDeliveryType().getId(), customerAddressDetailModel,
				promoCodeModel, orderItemDetailModels, orderPriceDetailModel, orderRatingDetailModel, etaModel,
				order.getOrderStatus().getId(), order.getCancelExpiryTime(), order.getCreatedAt(),
				order.getModifiedAt(), dispatcherUser, driverUser, auditOrderStatusModels);
	}

	@Transactional
	public OrderModel createOrderModelForDispatcher(Order order) {
		log.info("createOrderModelForDispatcher");

		UserDetailModel customerUser = customerBuilder.createCustomerUserDetailModel(order.getUser());
		log.info("createOrderModelForDispatcher :: customerUser " + customerUser);

		UserDetailModel driverUser = null;
		DriverOrderMapping driverOrderMapping = driverOrderMappingRepo.findByOrderId(order.getId());
		if (null != driverOrderMapping && !driverOrderMapping.isRejected())
			driverUser = driverBuilder.createDriverUserDetailModel(driverOrderMapping.getUserDriverDetail().getUser());
		log.info("createOrderModelForDispatcher :: driverUser " + driverUser);

		PromoCodeModel promoCodeModel = null;
		if (null != order.getPromoCode())
			promoCodeModel = dispatcherBuilder.createPromoCodeModel(order.getPromoCode());
		log.info("createOrderModelForDispatcher :: promoCodeModel " + promoCodeModel);

		CustomerAddressDetailModel customerAddressDetailModel = null;
		if (null != order.getAddressDetail()) {
			customerAddressDetailModel = dispatcherBuilder.createCustomerAddressModel(order.getAddressDetail());
			log.info("createOrderModelForDispatcher :: customerAddressDetailModel " + customerAddressDetailModel);
		}

		List<OrderItemDetailModel> orderItemDetailModels = new ArrayList<>();
		if (null != order.getOrderItemDetails() && !order.getOrderItemDetails().isEmpty()) {
			orderItemDetailModels = order.getOrderItemDetails().stream()
					.map(orderItemDetail -> customerBuilder.createOrderItemDetailModel(orderItemDetail))
					.collect(Collectors.toList());
		}
		log.info("createOrderModelForDispatcher ::orderItemDetailModels " + orderItemDetailModels);

		OrderPriceDetailModel orderPriceDetailModel = null;
		if (null != order.getOrderPriceDetail()) {
			orderPriceDetailModel = customerBuilder.createOrderPriceDetailModel(order.getOrderPriceDetail());
			log.info("createOrderModelForDispatcher :: orderPriceDetailModel " + orderPriceDetailModel);
		}

		OrderRatingDetailModel orderRatingDetailModel = null;
		if (null != order.getOrderRatingDetail()) {
			orderRatingDetailModel = customerBuilder.createOrderRatingDetailModel(order.getOrderRatingDetail());
			log.info("createOrderModelForDispatcher :: orderRatingDetailModel " + orderRatingDetailModel);
		}

		MasterModel etaModel = null;
		if (null != order.getEta())
			etaModel = masterBuilder.createMasterModel(order.getEta());
		log.info("createOrderModelForDispatcher :: etaModel " + etaModel);

		List<AuditOrderStatusModel> auditOrderStatusModels = new ArrayList<>();
		if (null != order.getAuditOrderStatus() && !order.getAuditOrderStatus().isEmpty()) {
			auditOrderStatusModels = order.getAuditOrderStatus().stream()
					.filter(auditOrderStatus -> !auditOrderStatus.isDeleted())
					.map(auditOrderStatus -> userBuilder.createAuditOrderStatusModel(auditOrderStatus))
					.collect(Collectors.toList());
			log.info("createOrderModelForDispatcher :: auditOrderStatusModels " + auditOrderStatusModels);
		}

		return new OrderModel(order.getId(), order.getDeliveryType().getId(), customerAddressDetailModel,
				promoCodeModel, orderItemDetailModels, orderPriceDetailModel, orderRatingDetailModel, etaModel,
				order.getOrderStatus().getId(), order.getCreatedAt(), order.getModifiedAt(), customerUser, driverUser,
				auditOrderStatusModels);
	}

}
