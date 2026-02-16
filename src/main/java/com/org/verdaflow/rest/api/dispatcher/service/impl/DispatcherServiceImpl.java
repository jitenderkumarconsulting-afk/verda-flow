
package com.org.verdaflow.rest.api.dispatcher.service.impl;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.verdaflow.rest.api.admin.model.OrderListWithUserCustomerModel;
import com.org.verdaflow.rest.api.admin.model.UserDetailModel;
import com.org.verdaflow.rest.api.auth.model.UserDriverDetailModel;
import com.org.verdaflow.rest.api.customer.CustomerBuilder;
import com.org.verdaflow.rest.api.dispatcher.DispatcherBuilder;
import com.org.verdaflow.rest.api.dispatcher.form.AssignOrderDriverForm;
import com.org.verdaflow.rest.api.dispatcher.form.DriverInventoryForm;
import com.org.verdaflow.rest.api.dispatcher.form.ProductForm;
import com.org.verdaflow.rest.api.dispatcher.form.PromoCodeForm;
import com.org.verdaflow.rest.api.dispatcher.form.RegisterDriverForm;
import com.org.verdaflow.rest.api.dispatcher.model.DriverInventoryModel;
import com.org.verdaflow.rest.api.dispatcher.model.GraphCountModel;
import com.org.verdaflow.rest.api.dispatcher.model.OrderListWithUserDriverModel;
import com.org.verdaflow.rest.api.dispatcher.model.ProductCountGraphModel;
import com.org.verdaflow.rest.api.dispatcher.model.ProductCountGraphModelForWeekOrMonthOrYear;
import com.org.verdaflow.rest.api.dispatcher.model.ProductModel;
import com.org.verdaflow.rest.api.dispatcher.model.PromoCodeModel;
import com.org.verdaflow.rest.api.dispatcher.service.DispatcherService;
import com.org.verdaflow.rest.api.driver.DriverBuilder;
import com.org.verdaflow.rest.api.user.form.RateOrderForm;
import com.org.verdaflow.rest.api.user.model.OrderModel;
import com.org.verdaflow.rest.common.enums.ApplicationStatus;
import com.org.verdaflow.rest.common.enums.DeliveryType;
import com.org.verdaflow.rest.common.enums.OrderStatus;
import com.org.verdaflow.rest.config.common.AppConst;
import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.config.security.jwt.JwtUser;
import com.org.verdaflow.rest.dto.PaginatedResponse;
import com.org.verdaflow.rest.entity.DriverInventory;
import com.org.verdaflow.rest.entity.DriverOrderMapping;
import com.org.verdaflow.rest.entity.Order;
import com.org.verdaflow.rest.entity.Product;
import com.org.verdaflow.rest.entity.PromoCode;
import com.org.verdaflow.rest.entity.UserDriverDetail;
import com.org.verdaflow.rest.entity.UserEntity;
import com.org.verdaflow.rest.error.AppException;
import com.org.verdaflow.rest.notification.NotificationBuilder;
import com.org.verdaflow.rest.repo.DriverInventoryRepo;
import com.org.verdaflow.rest.repo.DriverOrderMappingRepo;
import com.org.verdaflow.rest.repo.OrderRepo;
import com.org.verdaflow.rest.repo.ProductAggregateDetailRepo;
import com.org.verdaflow.rest.repo.ProductRepo;
import com.org.verdaflow.rest.repo.PromoCodeRepo;
import com.org.verdaflow.rest.repo.UserDriverDetailRepo;
import com.org.verdaflow.rest.repo.UserEntityRepo;
import com.org.verdaflow.rest.util.AppUtil;
import com.org.verdaflow.rest.util.DateUtil;

@Service
public class DispatcherServiceImpl implements DispatcherService {
	public static final Logger log = LoggerFactory.getLogger(DispatcherServiceImpl.class);

	@Autowired
	private DispatcherBuilder dispatcherBuilder;

	@Autowired
	private UserEntityRepo userEntityRepo;

	@Autowired
	private AppUtil appUtil;

	@Autowired
	private ProductRepo productRepo;

	@Autowired
	private PromoCodeRepo promoCodeRepo;

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private UserDriverDetailRepo userDriverDetailRepo;

	@Autowired
	private CustomerBuilder customerBuilder;

	@Autowired
	private DriverBuilder driverBuilder;

	@Autowired
	private ProductAggregateDetailRepo productAggregateDetailRepo;

	@Autowired
	private NotificationBuilder notificationBuilder;

	@Autowired
	private DriverOrderMappingRepo driverOrderMappingRepo;

	@Autowired
	private DriverInventoryRepo driverInventoryRepo;

	@Override
	@Transactional
	public UserDetailModel getProfile(JwtUser jwtUser) {
		log.info("getProfile");
		if (null == jwtUser.getUserEntity().getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		return dispatcherBuilder.createDispatcherUserDetailModel(jwtUser.getUserEntity());
	}

	@Override
	@Transactional
	public boolean registerDriver(RegisterDriverForm registerDriverForm, JwtUser jwtUser) {
		log.info("registerDriver");
		if (null == jwtUser.getUserEntity().getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		UserEntity userEntity = userEntityRepo.checkUserExistenceByEmailAndRole(registerDriverForm.getEmail(),
				AppConst.USER_ROLE.DRIVER);
		if (null != userEntity)
			throw new AppException(StringConst.EMAIL_ALREADY_EXIST, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
		log.info("registerDriver :: userEntity " + userEntity);

		userEntity = userEntityRepo.checkUserExistenceByMobileNumberAndRole(registerDriverForm.getCountryCode(),
				registerDriverForm.getMobileNumber(), AppConst.USER_ROLE.DRIVER);
		if (null != userEntity)
			throw new AppException(StringConst.MOBILE_ALREADY_EXIST, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
		log.info("registerDriver :: userEntity " + userEntity);

		String password = appUtil.generateRandomAlphaNumericCode(AppConst.NUMBER.EIGHT);

		userEntity = dispatcherBuilder.registerDriver(registerDriverForm, password, jwtUser);

		dispatcherBuilder.welcomeDriverEmail(userEntity, password);

		return true;
	}

	@Override
	@Transactional
	public UserDetailModel updateDriverDetails(RegisterDriverForm registerDriverForm, JwtUser jwtUser) {
		log.info("updateDriverDetails");
		if (AppConst.NUMBER.ZERO == registerDriverForm.getId())
			throw new AppException(StringConst.UPDATE_DRIVER_ID_INVALID, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		if (null == jwtUser.getUserEntity().getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		UserEntity driverUserEntity = userEntityRepo.findDriverByUserIdAndDispatcherId(registerDriverForm.getId(),
				jwtUser.getUserEntity().getUserDispatcherDetail().getId());
		if (null == driverUserEntity)
			throw new AppException(StringConst.USER_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		log.info("updateDriverDetails :: driverUserEntity " + driverUserEntity);

		driverUserEntity = dispatcherBuilder.updateUserDriverDetail(registerDriverForm, driverUserEntity);

		log.info("updateDriverDetails :: driverUserEntity " + driverUserEntity);

		return driverBuilder.createDriverUserDetailModel(driverUserEntity);
	}

	@Override
	@Transactional
	public PaginatedResponse<UserDetailModel> listDriverUsers(Pageable pageable, String query, int filter,
			JwtUser jwtUser) {
		log.info("listDriverUsers");
		if (null == jwtUser.getUserEntity().getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		Page<UserEntity> userEntities = null;

		if (null == query || StringUtils.isBlank(query)) {
			if (AppConst.NUMBER.ZERO == filter) {
				// all driver users
				userEntities = userEntityRepo.findAllDriverUsersByDispatcherId(pageable,
						jwtUser.getUserEntity().getUserDispatcherDetail().getId());
			} else if (AppConst.NUMBER.ONE == filter) {
				// all approved driver users
				userEntities = userEntityRepo.findAllDriverUsersByDispatcherIdAndApplicationStatus(pageable,
						jwtUser.getUserEntity().getUserDispatcherDetail().getId(), ApplicationStatus.APPROVED);
			} else if (AppConst.NUMBER.TWO == filter) {
				// all pending driver users
				userEntities = userEntityRepo.findAllDriverUsersByDispatcherIdAndApplicationStatus(pageable,
						jwtUser.getUserEntity().getUserDispatcherDetail().getId(), ApplicationStatus.PENDING);
			} else if (AppConst.NUMBER.THREE == filter) {
				// all rejected driver users
				userEntities = userEntityRepo.findAllDriverUsersByDispatcherIdAndApplicationStatus(pageable,
						jwtUser.getUserEntity().getUserDispatcherDetail().getId(), ApplicationStatus.REJECTED);
			} else {
				throw new AppException(StringConst.INVALID_FILTER, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
			}
		} else {
			String searchQuery = new StringBuilder(StringConst.PERCENTILE).append(appUtil.sanatizeQuery(query))
					.append(StringConst.PERCENTILE).toString();

			if (AppConst.NUMBER.ZERO == filter) {
				// all driver users
				userEntities = userEntityRepo.findAllDriverUsersByDispatcherIdSearch(pageable,
						jwtUser.getUserEntity().getUserDispatcherDetail().getId(), searchQuery);
			} else if (AppConst.NUMBER.ONE == filter) {
				// all approved driver users
				userEntities = userEntityRepo.findAllDriverUsersByDispatcherIdAndApplicationStatusSearch(pageable,
						jwtUser.getUserEntity().getUserDispatcherDetail().getId(), ApplicationStatus.APPROVED,
						searchQuery);
			} else if (AppConst.NUMBER.TWO == filter) {
				// all pending driver users
				userEntities = userEntityRepo.findAllDriverUsersByDispatcherIdAndApplicationStatusSearch(pageable,
						jwtUser.getUserEntity().getUserDispatcherDetail().getId(), ApplicationStatus.PENDING,
						searchQuery);
			} else if (AppConst.NUMBER.THREE == filter) {
				// all rejected driver users
				userEntities = userEntityRepo.findAllDriverUsersByDispatcherIdAndApplicationStatusSearch(pageable,
						jwtUser.getUserEntity().getUserDispatcherDetail().getId(), ApplicationStatus.REJECTED,
						searchQuery);
			} else {
				throw new AppException(StringConst.INVALID_FILTER, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
			}
		}
		log.info("listDriverUsers :: userEntities " + userEntities);

		List<UserDetailModel> userDetailModels = new ArrayList<>();
		if (!userEntities.getContent().isEmpty()) {
			userDetailModels = userEntities.getContent().stream().filter(predicate -> !predicate.isDeleted())
					.map(userEntity -> driverBuilder.createDriverUserDetailModel(userEntity))
					.collect(Collectors.toList());
		}

		log.info("listDriverUsers :: userDetailModels " + userDetailModels);

		int nextPage;
		if (userEntities.getContent().isEmpty()
				|| userEntities.getTotalPages() == pageable.getPageNumber() + AppConst.NUMBER.ONE
				|| userEntities.getTotalPages() == AppConst.NUMBER.ONE) {
			nextPage = -AppConst.NUMBER.ONE;
		} else {
			nextPage = (pageable.getPageNumber() + AppConst.NUMBER.ONE) + AppConst.NUMBER.ONE;
		}
		log.info("listDriverUsers :: nextPage " + nextPage);
		return new PaginatedResponse<>(userDetailModels, (int) userEntities.getTotalPages(),
				userEntities.getTotalElements(), nextPage);
	}

	@Override
	@Transactional
	public UserDetailModel driverDetails(int driverId, JwtUser jwtUser) {
		log.info("driverDetails");
		if (null == jwtUser.getUserEntity().getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		UserEntity userEntityDriver = userEntityRepo.findDriverByUserIdAndDispatcherId(driverId,
				jwtUser.getUserEntity().getUserDispatcherDetail().getId());
		if (null == userEntityDriver)
			throw new AppException(StringConst.DRIVER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("driverDetails :: userEntityDriver " + userEntityDriver);

		return driverBuilder.createDriverUserDetailModel(userEntityDriver);

	}

	@Override
	@Transactional
	public boolean addProduct(ProductForm productForm, JwtUser jwtUser) {
		log.info("addProduct");
		if (null == jwtUser.getUserEntity().getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		if (AppConst.PRODUCT_GROUP.VAPE_OIL_OR_CARTRIDGES != productForm.getGroupType()
				&& AppConst.PRODUCT_GROUP.EDIBLES != productForm.getGroupType())
			throw new AppException(StringConst.GROUP_TYPE_INVALID, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		if (productRepo.checkProductExistenceByNameAndDispatcherId(productForm.getName(),
				jwtUser.getUserEntity().getUserDispatcherDetail().getId()) > 0)
			throw new AppException(StringConst.PRODUCT_ALREADY_EXIST, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		dispatcherBuilder.saveProductDetails(productForm, jwtUser);

		return true;
	}

	@Override
	@Transactional
	public ProductModel updateProduct(ProductForm productForm, JwtUser jwtUser) {
		log.info("updateProduct");
		if (AppConst.NUMBER.ZERO == productForm.getId())
			throw new AppException(StringConst.UPDATE_PRODUCT_ID_INVALID, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		if (null == jwtUser.getUserEntity().getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		Product product = productRepo.findProductByIdAndDispatcherId(productForm.getId(),
				jwtUser.getUserEntity().getUserDispatcherDetail().getId());
		if (null == product)
			throw new AppException(StringConst.PRODUCT_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("updateProduct :: product " + product);

		if (productRepo.checkProductExistenceByNameAndDispatcherIdExceptCurrent(productForm.getName(), product.getId(),
				jwtUser.getUserEntity().getUserDispatcherDetail().getId()) > 0)
			throw new AppException(StringConst.PRODUCT_ALREADY_EXIST, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		product = dispatcherBuilder.updateProductDetails(productForm, product, jwtUser);

		return dispatcherBuilder.createProductModel(product);
	}

	@Override
	@Transactional
	public PaginatedResponse<ProductModel> listProducts(Pageable pageable, String query, int filter, JwtUser jwtUser) {
		log.info("listProducts");
		if (null == jwtUser.getUserEntity().getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		Page<Product> products;
		if (null == query || StringUtils.isBlank(query)) {
			if (AppConst.NUMBER.ZERO == filter) {
				// all products
				products = productRepo.findAllProductsByDispatcherId(pageable,
						jwtUser.getUserEntity().getUserDispatcherDetail().getId());
			} else if (AppConst.NUMBER.ONE == filter) {
				// all vape oil/cartridges products
				products = productRepo.findAllProductsByDispatcherIdAndGroupType(pageable,
						jwtUser.getUserEntity().getUserDispatcherDetail().getId(),
						AppConst.PRODUCT_GROUP.VAPE_OIL_OR_CARTRIDGES);
			} else if (AppConst.NUMBER.TWO == filter) {
				// all edibles products
				products = productRepo.findAllProductsByDispatcherIdAndGroupType(pageable,
						jwtUser.getUserEntity().getUserDispatcherDetail().getId(), AppConst.PRODUCT_GROUP.EDIBLES);
			} else {
				throw new AppException(StringConst.INVALID_FILTER, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
			}
		} else {
			String searchQuery = new StringBuilder(StringConst.PERCENTILE).append(appUtil.sanatizeQuery(query))
					.append(StringConst.PERCENTILE).toString();

			if (AppConst.NUMBER.ZERO == filter) {
				// all products
				products = productRepo.findAllProductsByDispatcherIdSearch(pageable,
						jwtUser.getUserEntity().getUserDispatcherDetail().getId(), searchQuery);
			} else if (AppConst.NUMBER.ONE == filter) {
				// all vape oil/cartridges products
				products = productRepo.findAllProductsByDispatcherIdAndGroupTypeSearch(pageable,
						jwtUser.getUserEntity().getUserDispatcherDetail().getId(),
						AppConst.PRODUCT_GROUP.VAPE_OIL_OR_CARTRIDGES, searchQuery);
			} else if (AppConst.NUMBER.TWO == filter) {
				// all edibles products
				products = productRepo.findAllProductsByDispatcherIdAndGroupTypeSearch(pageable,
						jwtUser.getUserEntity().getUserDispatcherDetail().getId(), AppConst.PRODUCT_GROUP.EDIBLES,
						searchQuery);
			} else {
				throw new AppException(StringConst.INVALID_FILTER, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
			}
		}
		log.info("listProducts :: products " + products);

		List<ProductModel> productModels = new ArrayList<>();
		if (!products.getContent().isEmpty()) {
			productModels = products.getContent().stream().filter(predicate -> !predicate.isDeleted())
					.map(product -> dispatcherBuilder.createProductModel(product)).collect(Collectors.toList());
		}
		log.info("listProducts :: productModels " + productModels);

		int nextPage;
		if (products.getContent().isEmpty()
				|| products.getTotalPages() == pageable.getPageNumber() + AppConst.NUMBER.ONE
				|| products.getTotalPages() == AppConst.NUMBER.ONE) {
			nextPage = -AppConst.NUMBER.ONE;
		} else {
			nextPage = (pageable.getPageNumber() + AppConst.NUMBER.ONE) + AppConst.NUMBER.ONE;
		}
		log.info("listProducts :: nextPage " + nextPage);
		return new PaginatedResponse<>(productModels, (int) products.getTotalPages(), products.getTotalElements(),
				nextPage);
	}

	@Override
	@Transactional
	public ProductModel productDetails(int productId, JwtUser jwtUser) {
		log.info("productDetails");
		if (null == jwtUser.getUserEntity().getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		Product product = productRepo.findProductByIdAndDispatcherId(productId,
				jwtUser.getUserEntity().getUserDispatcherDetail().getId());
		if (null == product)
			throw new AppException(StringConst.PRODUCT_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("productDetails :: product " + product);

		return dispatcherBuilder.createProductModel(product);

	}

	@Override
	@Transactional
	public boolean deleteProduct(int productId, JwtUser jwtUser) {
		log.info("deleteProduct");
		if (null == jwtUser.getUserEntity().getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		Product product = productRepo.findProductByIdAndDispatcherId(productId,
				jwtUser.getUserEntity().getUserDispatcherDetail().getId());
		if (null == product)
			throw new AppException(StringConst.PRODUCT_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("deleteProduct :: product " + product);

		dispatcherBuilder.deleteProduct(product);

		return true;
	}

	@Override
	@Transactional
	public boolean addPromoCode(PromoCodeForm promoCodeForm, JwtUser jwtUser) {
		log.info("addPromoCode");
		promoCodeForm.setName(promoCodeForm.getName().trim().toUpperCase());

		if (null == jwtUser.getUserEntity().getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		if (promoCodeRepo.checkPromoCodeExistenceByNameAndDispatcherId(promoCodeForm.getName(),
				jwtUser.getUserEntity().getUserDispatcherDetail().getId()) > 0)
			throw new AppException(StringConst.PROMOCODE_ALREADY_EXIST, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		dispatcherBuilder.savePromoCodeDetails(promoCodeForm, jwtUser);

		return true;
	}

	@Override
	@Transactional
	public PromoCodeModel updatePromoCode(PromoCodeForm promoCodeForm, JwtUser jwtUser) {
		log.info("updatePromoCode");
		if (AppConst.NUMBER.ZERO == promoCodeForm.getId())
			throw new AppException(StringConst.UPDATE_PROMOCODE_ID_INVALID, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		promoCodeForm.setName(promoCodeForm.getName().trim().toUpperCase());

		if (null == jwtUser.getUserEntity().getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		PromoCode promoCode = promoCodeRepo.findPromoCodeByIdAndDispatcherId(promoCodeForm.getId(),
				jwtUser.getUserEntity().getUserDispatcherDetail().getId());
		if (null == promoCode)
			throw new AppException(StringConst.PROMOCODE_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("updatePromoCode :: promoCode " + promoCode);

		if (promoCodeRepo.checkPromoCodeExistenceByNameAndDispatcherIdExceptCurrent(promoCodeForm.getName(),
				jwtUser.getUserEntity().getUserDispatcherDetail().getId(), promoCode.getId()) > 0)
			throw new AppException(StringConst.PROMOCODE_ALREADY_EXIST, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		promoCode = dispatcherBuilder.updatePromoCodeDetails(promoCodeForm, promoCode, jwtUser);

		return dispatcherBuilder.createPromoCodeModel(promoCode);
	}

	@Override
	@Transactional
	public boolean deletePromoCode(int promoCodeId, JwtUser jwtUser) {
		log.info("deletePromoCode");
		if (null == jwtUser.getUserEntity().getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		PromoCode promoCode = promoCodeRepo.findPromoCodeByIdAndDispatcherId(promoCodeId,
				jwtUser.getUserEntity().getUserDispatcherDetail().getId());
		if (null == promoCode)
			throw new AppException(StringConst.PROMOCODE_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("deletePromoCode :: promoCode " + promoCode);

		dispatcherBuilder.deletePromoCode(promoCode, jwtUser);

		return true;
	}

	@Override
	@Transactional
	public PaginatedResponse<PromoCodeModel> listPromoCodes(Pageable pageable, String query, JwtUser jwtUser) {
		log.info("listPromoCodes");
		if (null == jwtUser.getUserEntity().getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		Page<PromoCode> promoCodes;
		if (null == query || StringUtils.isBlank(query)) {
			promoCodes = promoCodeRepo.findAllPromoCodesByDispatcherId(pageable,
					jwtUser.getUserEntity().getUserDispatcherDetail().getId());
		} else {
			String searchQuery = new StringBuilder(StringConst.PERCENTILE).append(appUtil.sanatizeQuery(query))
					.append(StringConst.PERCENTILE).toString();
			promoCodes = promoCodeRepo.findAllPromoCodesByDispatcherIdSearch(pageable,
					jwtUser.getUserEntity().getUserDispatcherDetail().getId(), searchQuery);
		}

		log.info("listPromoCodes :: promoCodes " + promoCodes);

		List<PromoCodeModel> promoCodeModels = new ArrayList<>();
		if (!promoCodes.getContent().isEmpty()) {
			promoCodeModels = promoCodes.getContent().stream().filter(predicate -> !predicate.isDeleted())
					.map(promoCode -> dispatcherBuilder.createPromoCodeModel(promoCode)).collect(Collectors.toList());
		}

		log.info("listPromoCodes :: promoCodeModels " + promoCodeModels);

		int nextPage;
		if (promoCodes.getContent().isEmpty()
				|| promoCodes.getTotalPages() == pageable.getPageNumber() + AppConst.NUMBER.ONE
				|| promoCodes.getTotalPages() == AppConst.NUMBER.ONE) {
			nextPage = -AppConst.NUMBER.ONE;
		} else {
			nextPage = (pageable.getPageNumber() + AppConst.NUMBER.ONE) + AppConst.NUMBER.ONE;
		}
		log.info("listPromoCodes :: nextPage " + nextPage);
		return new PaginatedResponse<>(promoCodeModels, (int) promoCodes.getTotalPages(), promoCodes.getTotalElements(),
				nextPage);

	}

	@Override
	@Transactional
	public UserDetailModel activateDriver(int driverUserId, boolean deact, JwtUser jwtUser) {
		log.info("activateDriver");
		UserEntity driverUserEntity = userEntityRepo.findDriverByUserIdAndDispatcherId(driverUserId,
				jwtUser.getUserEntity().getUserDispatcherDetail().getId());
		if (null == driverUserEntity)
			throw new AppException(StringConst.USER_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("activateDriver :: driverUserEntity " + driverUserEntity);

		if (null == driverUserEntity.getUserDriverDetail())
			throw new AppException(StringConst.DRIVER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		if (deact
				&& orderRepo.findNewAndPendingOrdersCountForDriver(driverUserEntity.getUserDriverDetail().getId()) > 0)
			throw new AppException(StringConst.DRIVER_DEACTIVATE_REJECT, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		driverUserEntity = dispatcherBuilder.activateDriver(driverUserEntity, deact);

		dispatcherBuilder.activateDriverEmail(driverUserEntity, deact);

		return driverBuilder.createDriverUserDetailModel(driverUserEntity);
	}

	@Override
	@Transactional
	public PromoCodeModel promoCodeDetails(int promoCodeId, JwtUser jwtUser) {
		log.info("promoCodeDetails");
		if (null == jwtUser.getUserEntity().getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		PromoCode promoCode = promoCodeRepo.findPromoCodeByIdAndDispatcherId(promoCodeId,
				jwtUser.getUserEntity().getUserDispatcherDetail().getId());
		if (null == promoCode)
			throw new AppException(StringConst.PROMOCODE_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("deletePromoCode :: promoCode " + promoCode);

		return dispatcherBuilder.createPromoCodeModel(promoCode);
	}

	@Override
	@Transactional
	public PaginatedResponse<OrderModel> listOrders(Pageable pageable, int filter, String query, JwtUser jwtUser) {
		log.info("listOrders");
		if (null == jwtUser.getUserEntity().getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		Page<Order> orders = null;
		if (null == query || StringUtils.isBlank(query)) {
			if (AppConst.NUMBER.ZERO == filter) {
				// All orders
				orders = orderRepo.findAllByDispatcherId(pageable,
						jwtUser.getUserEntity().getUserDispatcherDetail().getId());
			} else if (AppConst.NUMBER.ONE == filter) {
				// All new and pending orders
				orders = orderRepo.findAllNewAndPendingOrdersForDispatcher(pageable,
						jwtUser.getUserEntity().getUserDispatcherDetail().getId());
			} else if (AppConst.NUMBER.TWO == filter) {
				// All complete orders
				orders = orderRepo.findAllCompletedOrdersForDispatcher(pageable,
						jwtUser.getUserEntity().getUserDispatcherDetail().getId());
			} else {
				throw new AppException(StringConst.INVALID_FILTER, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
			}
		} else {
			String searchQuery = new StringBuilder(StringConst.PERCENTILE).append(appUtil.sanatizeQuery(query))
					.append(StringConst.PERCENTILE).toString();
			if (AppConst.NUMBER.ZERO == filter) {
				// All orders
				orders = orderRepo.findAllByDispatcherIdSearch(pageable,
						jwtUser.getUserEntity().getUserDispatcherDetail().getId(), searchQuery);
			} else if (AppConst.NUMBER.ONE == filter) {
				// All new and pending orders
				orders = orderRepo.findAllNewAndPendingOrdersForDispatcherSearch(pageable,
						jwtUser.getUserEntity().getUserDispatcherDetail().getId(), searchQuery);
			} else if (AppConst.NUMBER.TWO == filter) {
				// All complete orders
				orders = orderRepo.findAllCompletedOrdersForDispatcherSearch(pageable,
						jwtUser.getUserEntity().getUserDispatcherDetail().getId(), searchQuery);
			} else {
				throw new AppException(StringConst.INVALID_FILTER, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
			}
		}

		List<OrderModel> orderModels = new ArrayList<>();

		if (!orders.getContent().isEmpty()) {
			log.info("listOrders :: orders " + orders);
			orderModels = orders.getContent().stream().filter(predicate -> !predicate.isDeleted())
					.map(order -> dispatcherBuilder.createOrderModel(order)).collect(Collectors.toList());
		}
		log.info("listOrders :: orderModels " + orderModels);

		int nextPage;
		if (orders.getContent().isEmpty() || orders.getTotalPages() == pageable.getPageNumber() + AppConst.NUMBER.ONE
				|| orders.getTotalPages() == AppConst.NUMBER.ONE) {
			nextPage = -AppConst.NUMBER.ONE;
		} else {
			nextPage = (pageable.getPageNumber() + AppConst.NUMBER.ONE) + AppConst.NUMBER.ONE;
		}
		return new PaginatedResponse<>(orderModels, (int) orders.getTotalPages(), orders.getTotalElements(), nextPage);
	}

	@Override
	@Transactional
	public OrderListWithUserDriverModel listOrdersForDriver(Pageable pageable, int driverUserId, int filter,
			String query, JwtUser jwtUser) {
		log.info("listOrdersForDriver");
		if (null == jwtUser.getUserEntity().getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		UserEntity driverUserEntity = userEntityRepo.findDriverByUserIdAndDispatcherId(driverUserId,
				jwtUser.getUserEntity().getUserDispatcherDetail().getId());
		if (null == driverUserEntity || null == driverUserEntity.getUserDriverDetail())
			throw new AppException(StringConst.DRIVER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		/*
		 * Page<Order> orders = orderRepo.findAllByDriverIdAndDispatcherId(pageable,
		 * driverUserEntity.getUserDriverDetail().getId(),
		 * jwtUser.getUserEntity().getUserDispatcherDetail().getId());
		 */

		Page<Order> orders = null;
		if (null == query || StringUtils.isBlank(query)) {
			if (AppConst.NUMBER.ZERO == filter) {
				// All orders
				orders = orderRepo.findAllByDriverId(pageable, driverUserEntity.getUserDriverDetail().getId());
			} else if (AppConst.NUMBER.ONE == filter) {
				// All new orders
				orders = orderRepo.findAllNewOrdersForDriver(pageable, driverUserEntity.getUserDriverDetail().getId());
			} else if (AppConst.NUMBER.TWO == filter) {
				// All pending orders
				orders = orderRepo.findAllPendingOrdersForDriver(pageable,
						driverUserEntity.getUserDriverDetail().getId());
			} else if (AppConst.NUMBER.THREE == filter) {
				// All completed orders
				orders = orderRepo.findAllCompletedOrdersForDriver(pageable,
						driverUserEntity.getUserDriverDetail().getId());
			} else if (AppConst.NUMBER.FOUR == filter) {
				// All new and pending orders
				orders = orderRepo.findAllNewAndPendingOrdersForDriver(pageable,
						driverUserEntity.getUserDriverDetail().getId());
			} else {
				throw new AppException(StringConst.INVALID_FILTER, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
			}
		} else {
			String searchQuery = new StringBuilder(StringConst.PERCENTILE).append(appUtil.sanatizeQuery(query))
					.append(StringConst.PERCENTILE).toString();
			if (AppConst.NUMBER.ZERO == filter) {
				// All orders
				orders = orderRepo.findAllByDriverIdSearch(pageable, driverUserEntity.getUserDriverDetail().getId(),
						searchQuery);
			} else if (AppConst.NUMBER.ONE == filter) {
				// All new orders
				orders = orderRepo.findAllNewOrdersForDriverSearch(pageable,
						driverUserEntity.getUserDriverDetail().getId(), searchQuery);
			} else if (AppConst.NUMBER.TWO == filter) {
				// All pending orders
				orders = orderRepo.findAllPendingOrdersForDriverSearch(pageable,
						driverUserEntity.getUserDriverDetail().getId(), searchQuery);
			} else if (AppConst.NUMBER.THREE == filter) {
				// All completed orders
				orders = orderRepo.findAllCompletedOrdersForDriverSearch(pageable,
						driverUserEntity.getUserDriverDetail().getId(), searchQuery);
			} else if (AppConst.NUMBER.FOUR == filter) {
				// All new and pending orders
				orders = orderRepo.findAllNewAndPendingOrdersForDriverSearch(pageable,
						driverUserEntity.getUserDriverDetail().getId(), searchQuery);
			} else {
				throw new AppException(StringConst.INVALID_FILTER, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
			}
		}

		List<OrderModel> orderModels = new ArrayList<>();

		if (!orders.getContent().isEmpty()) {
			log.info("listOrdersForDriver :: orders " + orders);
			orderModels = orders.getContent().stream().filter(predicate -> !predicate.isDeleted())
					.map(order -> dispatcherBuilder.createOrderModel(order)).collect(Collectors.toList());
		}
		log.info("listOrdersForDriver :: orderModels " + orderModels);

		int nextPage;
		if (orders.getContent().isEmpty() || orders.getTotalPages() == pageable.getPageNumber() + AppConst.NUMBER.ONE
				|| orders.getTotalPages() == AppConst.NUMBER.ONE) {
			nextPage = -AppConst.NUMBER.ONE;
		} else {
			nextPage = (pageable.getPageNumber() + AppConst.NUMBER.ONE) + AppConst.NUMBER.ONE;
		}
		return new OrderListWithUserDriverModel(
				new PaginatedResponse<>(orderModels, (int) orders.getTotalPages(), orders.getTotalElements(), nextPage),
				driverBuilder.createDriverUserDetailModel(driverUserEntity));
	}

	@Override
	@Transactional
	public OrderListWithUserCustomerModel listOrdersForCustomer(Pageable pageable, int customerUserId, int filter,
			JwtUser jwtUser) {
		log.info("listOrdersForCustomer");
		if (null == jwtUser.getUserEntity().getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		UserEntity customerUserEntity = userEntityRepo.findByUserIdAndRole(customerUserId, AppConst.USER_ROLE.CUSTOMER);
		if (null == customerUserEntity || null == customerUserEntity.getUserCustomerDetail())
			throw new AppException(StringConst.CUSTOMER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		Page<Order> orders = null;
		if (AppConst.NUMBER.ZERO == filter) {
			// All orders
			orders = orderRepo.findAllByUserIdAndDispatcherId(pageable, customerUserEntity.getId(),
					jwtUser.getUserEntity().getUserDispatcherDetail().getId());
		} else if (AppConst.NUMBER.ONE == filter) {
			// All new orders
			orders = orderRepo.findAllNewOrdersForCustomerByDispatcherId(pageable, customerUserEntity.getId(),
					jwtUser.getUserEntity().getUserDispatcherDetail().getId());
		} else if (AppConst.NUMBER.TWO == filter) {
			// All pending orders
			orders = orderRepo.findAllPendingOrdersForCustomerByDispatcherId(pageable, customerUserEntity.getId(),
					jwtUser.getUserEntity().getUserDispatcherDetail().getId());
		} else if (AppConst.NUMBER.THREE == filter) {
			// All completed orders
			orders = orderRepo.findAllCompletedOrdersForCustomerByDispatcherId(pageable, customerUserEntity.getId(),
					jwtUser.getUserEntity().getUserDispatcherDetail().getId());
		} else {
			throw new AppException(StringConst.INVALID_FILTER, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
		}

		List<OrderModel> orderModels = new ArrayList<>();

		if (!orders.getContent().isEmpty()) {
			log.info("listOrdersForCustomer :: orders " + orders);
			orderModels = orders.getContent().stream().filter(predicate -> !predicate.isDeleted())
					.map(order -> dispatcherBuilder.createOrderModel(order)).collect(Collectors.toList());
		}
		log.info("listOrdersForCustomer :: orderModels " + orderModels);

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
	public OrderModel confirmOrder(int orderId, JwtUser jwtUser) {
		log.info("confirmOrder");
		if (null == jwtUser.getUserEntity().getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		Order order = orderRepo.findByOrderIdAndDispatcherId(orderId,
				jwtUser.getUserEntity().getUserDispatcherDetail().getId());
		if (order == null)
			throw new AppException(StringConst.ORDER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		log.info("confirmOrder :: order " + order);

		if (OrderStatus.PLACED != order.getOrderStatus())
			throw new AppException(StringConst.ORDER_CONFIRMED_REJECT, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		order = dispatcherBuilder.confirmOrder(order);
		log.info("confirmOrder :: order " + order);

		customerBuilder.saveAuditOrderStatus(order, jwtUser.getUserEntity(), OrderStatus.CONFIRMED);

		notificationBuilder.createOrderConfirmedNotification(order, jwtUser.getUserEntity());

		return dispatcherBuilder.createOrderModel(order);
	}

	@Override
	@Transactional
	public OrderModel prepareOrder(int orderId, JwtUser jwtUser) {
		log.info("prepareOrder");
		if (null == jwtUser.getUserEntity().getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		Order order = orderRepo.findByOrderIdAndDispatcherId(orderId,
				jwtUser.getUserEntity().getUserDispatcherDetail().getId());
		if (order == null)
			throw new AppException(StringConst.ORDER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		log.info("prepareOrder :: order " + order);

		if (OrderStatus.CONFIRMED != order.getOrderStatus())
			throw new AppException(StringConst.ORDER_PREPARED_REJECT, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		order = dispatcherBuilder.prepareOrder(order);
		log.info("prepareOrder :: order " + order);

		customerBuilder.saveAuditOrderStatus(order, jwtUser.getUserEntity(), OrderStatus.PREPARED);

		notificationBuilder.createOrderPreparedNotification(order, jwtUser.getUserEntity());

		return dispatcherBuilder.createOrderModel(order);
	}

	@Override
	@Transactional
	public PaginatedResponse<UserDriverDetailModel> listDriversForAssigningOrder(Pageable pageable, int orderId,
			String query, JwtUser jwtUser) {
		log.info("listDriversForAssigningOrder");
		if (null == jwtUser.getUserEntity().getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		Order order = orderRepo.findByOrderIdAndDispatcherId(orderId,
				jwtUser.getUserEntity().getUserDispatcherDetail().getId());
		if (order == null)
			throw new AppException(StringConst.ORDER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("listDriversForAssigningOrder :: order " + order);

		DriverOrderMapping driverOrderMapping = driverOrderMappingRepo.findByOrderId(order.getId());
		log.info("listDriversForAssigningOrder :: driverOrderMapping " + driverOrderMapping);

		final int assignedDriverId = (null != driverOrderMapping && !driverOrderMapping.isRejected())
				? driverOrderMapping.getUserDriverDetail().getId()
				: AppConst.NUMBER.ZERO;
		log.info("listDriversForAssigningOrder :: assignedDriverId " + assignedDriverId);

		Page<UserDriverDetail> userDriverDetails = null;

		List<UserDriverDetailModel> userDriverDetailModels = new ArrayList<>();
		if (null == query || StringUtils.isBlank(query)) {
			userDriverDetails = userDriverDetailRepo.findAllActiveDriversByDispatcherId(pageable,
					jwtUser.getUserEntity().getUserDispatcherDetail().getId());
		} else {
			String searchQuery = new StringBuilder(StringConst.PERCENTILE).append(appUtil.sanatizeQuery(query))
					.append(StringConst.PERCENTILE).toString();
			userDriverDetails = userDriverDetailRepo.findAllActiveDriversByDispatcherIdSearch(pageable,
					jwtUser.getUserEntity().getUserDispatcherDetail().getId(), searchQuery);
		}

		if (!userDriverDetails.getContent().isEmpty()) {
			log.info("listDriversForAssigningOrder :: userDriverDetails " + userDriverDetails);
			userDriverDetailModels = userDriverDetails.getContent().stream().filter(predicate -> !predicate.isDeleted())
					.map(userDriverDetail -> dispatcherBuilder
							.callCreateUserDriverDetailModelForAssignOrder(userDriverDetail, assignedDriverId))
					.collect(Collectors.toList());
		}
		log.info("listDriversForAssigningOrder :: userDriverDetailModels " + userDriverDetailModels);

		int nextPage;
		if (userDriverDetails.getContent().isEmpty()
				|| userDriverDetails.getTotalPages() == pageable.getPageNumber() + AppConst.NUMBER.ONE
				|| userDriverDetails.getTotalPages() == AppConst.NUMBER.ONE) {
			nextPage = -AppConst.NUMBER.ONE;
		} else {
			nextPage = (pageable.getPageNumber() + AppConst.NUMBER.ONE) + AppConst.NUMBER.ONE;
		}
		return new PaginatedResponse<>(userDriverDetailModels, (int) userDriverDetails.getTotalPages(),
				userDriverDetails.getTotalElements(), nextPage);
	}

	@Override
	@Transactional
	public UserDriverDetailModel assignDriverToOrderFromWeb(AssignOrderDriverForm assignOrderDriverForm,
			JwtUser jwtUser) {
		log.info("assignDriverToOrderFromWeb");
		if (null == jwtUser.getUserEntity().getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		Order order = orderRepo.findByOrderIdAndDispatcherId(assignOrderDriverForm.getOrderId(),
				jwtUser.getUserEntity().getUserDispatcherDetail().getId());
		if (null == order)
			throw new AppException(StringConst.ORDER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("assignDriverToOrderFromWeb :: order " + order);

		UserDriverDetail userDriverDetail = userDriverDetailRepo.findDriverByIdAndDispatcherId(
				assignOrderDriverForm.getDriverId(), jwtUser.getUserEntity().getUserDispatcherDetail().getId());
		if (null == userDriverDetail)
			throw new AppException(StringConst.DRIVER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("assignDriverToOrderFromWeb :: userDriverDetail " + userDriverDetail);

		if (DeliveryType.PICKUP == order.getDeliveryType())
			throw new AppException(StringConst.ORDER_ASSIGNED_DELIVERY_TYPE_REJECT,
					AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		if (OrderStatus.PREPARED != order.getOrderStatus() && OrderStatus.REASSIGN_DRIVER != order.getOrderStatus()
				&& OrderStatus.DRIVER_ASSIGNED != order.getOrderStatus())
			throw new AppException(StringConst.ORDER_ASSIGNED_STATUS_REJECT, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		if (OrderStatus.DRIVER_ASSIGNED == order.getOrderStatus() && null != order.getDriverOrderMapping()
				&& userDriverDetail.getId() == order.getDriverOrderMapping().getUserDriverDetail().getId())
			throw new AppException(StringConst.ORDER_ASSIGNED_SAME_DRIVER_REJECT,
					AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		order = dispatcherBuilder.assignDriverToOrder(order, assignOrderDriverForm, userDriverDetail);

		customerBuilder.saveAuditOrderStatus(order, jwtUser.getUserEntity(), OrderStatus.DRIVER_ASSIGNED);

		notificationBuilder.createOrderDriverAssignedNotification(order, jwtUser.getUserEntity(), userDriverDetail);

		return dispatcherBuilder
				.createUserDriverDetailModelForAssignOrder(order.getDriverOrderMapping().getUserDriverDetail(), true);
	}

	@Override
	@Transactional
	public OrderModel assignDriverToOrderFromApp(AssignOrderDriverForm assignOrderDriverForm, JwtUser jwtUser) {
		log.info("assignDriverToOrderFromApp");
		if (null == jwtUser.getUserEntity().getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		Order order = orderRepo.findByOrderIdAndDispatcherId(assignOrderDriverForm.getOrderId(),
				jwtUser.getUserEntity().getUserDispatcherDetail().getId());
		if (null == order)
			throw new AppException(StringConst.ORDER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("assignDriverToOrderFromApp :: order " + order);

		UserDriverDetail userDriverDetail = userDriverDetailRepo.findDriverByIdAndDispatcherId(
				assignOrderDriverForm.getDriverId(), jwtUser.getUserEntity().getUserDispatcherDetail().getId());
		if (null == userDriverDetail)
			throw new AppException(StringConst.DRIVER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("assignDriverToOrderFromApp :: userDriverDetail " + userDriverDetail);

		if (DeliveryType.PICKUP == order.getDeliveryType())
			throw new AppException(StringConst.ORDER_ASSIGNED_DELIVERY_TYPE_REJECT,
					AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		if (OrderStatus.PREPARED != order.getOrderStatus() && OrderStatus.REASSIGN_DRIVER != order.getOrderStatus()
				&& OrderStatus.DRIVER_ASSIGNED != order.getOrderStatus())
			throw new AppException(StringConst.ORDER_ASSIGNED_STATUS_REJECT, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		if (OrderStatus.DRIVER_ASSIGNED == order.getOrderStatus() && null != order.getDriverOrderMapping()
				&& userDriverDetail.getId() == order.getDriverOrderMapping().getUserDriverDetail().getId())
			throw new AppException(StringConst.ORDER_ASSIGNED_SAME_DRIVER_REJECT,
					AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		order = dispatcherBuilder.assignDriverToOrder(order, assignOrderDriverForm, userDriverDetail);

		customerBuilder.saveAuditOrderStatus(order, jwtUser.getUserEntity(), OrderStatus.DRIVER_ASSIGNED);

		notificationBuilder.createOrderDriverAssignedNotification(order, jwtUser.getUserEntity(), userDriverDetail);

		return dispatcherBuilder.createOrderModel(order);
	}

	@Override
	@Transactional
	public OrderModel completeOrder(int orderId, JwtUser jwtUser) {
		log.info("completeOrder");
		if (null == jwtUser.getUserEntity().getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		Order order = orderRepo.findByOrderIdAndDispatcherId(orderId,
				jwtUser.getUserEntity().getUserDispatcherDetail().getId());
		if (order == null)
			throw new AppException(StringConst.ORDER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		log.info("completeOrder :: order " + order);

		if (DeliveryType.DELIVERY == order.getDeliveryType()
				|| (DeliveryType.PICKUP == order.getDeliveryType() && OrderStatus.PREPARED != order.getOrderStatus()))
			throw new AppException(StringConst.ORDER_COMPLETED_REJECT, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		order = dispatcherBuilder.completeOrder(order);
		log.info("completeOrder :: order " + order);

		dispatcherBuilder.saveProductAggregatesForProductSold(order, jwtUser.getUserEntity());

		customerBuilder.saveAuditOrderStatus(order, jwtUser.getUserEntity(), OrderStatus.COMPLETED);

		notificationBuilder.createOrderCompletedPickupNotification(order, jwtUser.getUserEntity());

		return dispatcherBuilder.createOrderModel(order);
	}

	@Override
	@Transactional
	public OrderModel cancelOrder(int orderId, JwtUser jwtUser) {
		log.info("cancelOrder");
		if (null == jwtUser.getUserEntity().getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		Order order = orderRepo.findByOrderIdAndDispatcherId(orderId,
				jwtUser.getUserEntity().getUserDispatcherDetail().getId());
		if (order == null)
			throw new AppException(StringConst.ORDER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		log.info("cancelOrder :: order " + order);

		if (OrderStatus.PLACED != order.getOrderStatus())
			throw new AppException(StringConst.ORDER_CANCELLED_REJECT, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		order = dispatcherBuilder.cancelOrder(order);
		log.info("cancelOrder :: order " + order);

		customerBuilder.saveAuditOrderStatus(order, jwtUser.getUserEntity(), OrderStatus.CANCELLED_BY_STORE);

		notificationBuilder.createOrderCancelledByStoreNotification(order, jwtUser.getUserEntity());

		return dispatcherBuilder.createOrderModel(order);
	}

	@Override
	@Transactional
	public GraphCountModel graphProductSold(long startDate, long endDate, int filter, JwtUser jwtUser) {
		log.info("graphProductSold");
		if (null == jwtUser.getUserEntity().getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

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
			List<ProductCountGraphModel> productCountGraphModels = productAggregateDetailRepo
					.getProductsCountByDispatcherIdDateWise(fromDate, toDate,
							jwtUser.getUserEntity().getUserDispatcherDetail().getId());
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
								jwtUser.getUserEntity().getUserDispatcherDetail().getId());
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
								jwtUser.getUserEntity().getUserDispatcherDetail().getId());
				log.info("graphProductSold :: productCountGraphModelForWeekOrMonthOrYears "
						+ productCountGraphModelForWeekOrMonthOrYears);

				graphCountModel = dispatcherBuilder.createProductsCountGraphModelMonthly(
						productCountGraphModelForWeekOrMonthOrYears, fromDate, toDate, filter);
			} else if (AppConst.NUMBER.THREE == filter) {
				// Product count Yearly
				productCountGraphModelForWeekOrMonthOrYears = productAggregateDetailRepo
						.getProductsCountByDispatcherIdYearly(fromDate, toDate,
								jwtUser.getUserEntity().getUserDispatcherDetail().getId());
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

	@Override
	@Transactional
	public boolean rateOrder(RateOrderForm rateOrderForm, JwtUser jwtUser) {
		log.info("rateOrder");
		if (null == jwtUser.getUserEntity().getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		Order order = orderRepo.findByOrderIdAndDispatcherId(rateOrderForm.getOrderId(),
				jwtUser.getUserEntity().getUserDispatcherDetail().getId());
		if (order == null)
			throw new AppException(StringConst.ORDER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("rateOrder :: order " + order);

		if (OrderStatus.COMPLETED != order.getOrderStatus() || null == order.getOrderRatingDetail())
			throw new AppException(StringConst.RATING_SUBMIT_REJECT, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		if (!order.getOrderRatingDetail().isCustomerPendingByDispatcher())
			throw new AppException(StringConst.RATING_SUBMIT_REJECT, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		if (order.getOrderRatingDetail().isCustomerPendingByDispatcher() && null == rateOrderForm.getRateCustomerForm())
			throw new AppException(StringConst.RATING_INVALID, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		if (order.getOrderRatingDetail().isCustomerPendingByDispatcher())
			dispatcherBuilder.rateCustomer(rateOrderForm.getRateCustomerForm(), order, jwtUser.getUserEntity());

		return true;
	}

	@Override
	@Transactional
	public DriverInventoryModel addOrUpdateDriverInventory(DriverInventoryForm driverInventoryForm, JwtUser jwtUser) {
		log.info("addOrUpdateDriverInventory");
		if (null == jwtUser.getUserEntity().getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		UserDriverDetail userDriverDetail = userDriverDetailRepo.findDriverByIdAndDispatcherId(
				driverInventoryForm.getDriverId(), jwtUser.getUserEntity().getUserDispatcherDetail().getId());
		if (null == userDriverDetail)
			throw new AppException(StringConst.DRIVER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("addOrUpdateDriverInventory :: userDriverDetail " + userDriverDetail);

		Product product = productRepo.findProductByIdAndDispatcherId(driverInventoryForm.getProductId(),
				jwtUser.getUserEntity().getUserDispatcherDetail().getId());
		if (null == product)
			throw new AppException(StringConst.PRODUCT_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("addOrUpdateDriverInventory :: product " + product);

		DriverInventory driverInventory = dispatcherBuilder.saveOrUpdateDriverInventory(driverInventoryForm,
				userDriverDetail, product);

		return dispatcherBuilder.createDriverInventoryModel(driverInventory);
	}

	@Override
	@Transactional
	public PaginatedResponse<DriverInventoryModel> listDriverInventories(Pageable pageable, int driverUserId,
			int filter, String query, JwtUser jwtUser) {
		log.info("listDriverInventories");
		if (null == jwtUser.getUserEntity().getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		UserEntity driverUserEntity = userEntityRepo.findDriverByUserIdAndDispatcherId(driverUserId,
				jwtUser.getUserEntity().getUserDispatcherDetail().getId());
		if (null == driverUserEntity || null == driverUserEntity.getUserDriverDetail())
			throw new AppException(StringConst.DRIVER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		Page<Product> products = null;
		if (null == query || StringUtils.isBlank(query)) {
			if (AppConst.NUMBER.ZERO == filter) {
				// All driver inventories
				products = productRepo.findAllProductsByDispatcherId(pageable,
						jwtUser.getUserEntity().getUserDispatcherDetail().getId());
			} else if (AppConst.NUMBER.ONE == filter) {
				// All in stock driver inventories
				products = productRepo.findAllInStockDriverInventoryProductsByDispatcherIdAndDriverId(pageable,
						jwtUser.getUserEntity().getUserDispatcherDetail().getId(),
						driverUserEntity.getUserDriverDetail().getId());
			} else if (AppConst.NUMBER.TWO == filter) {
				// All out of stock driver inventories
				products = productRepo.findAllOutOfStockDriverInventoryProductsByDispatcherIdAndDriverId(pageable,
						jwtUser.getUserEntity().getUserDispatcherDetail().getId(),
						driverUserEntity.getUserDriverDetail().getId());
			} else {
				throw new AppException(StringConst.INVALID_FILTER, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
			}
		} else {
			String searchQuery = new StringBuilder(StringConst.PERCENTILE).append(appUtil.sanatizeQuery(query))
					.append(StringConst.PERCENTILE).toString();

			if (AppConst.NUMBER.ZERO == filter) {
				// All driver inventories
				products = productRepo.findAllProductsByDispatcherIdSearch(pageable,
						jwtUser.getUserEntity().getUserDispatcherDetail().getId(), searchQuery);
			} else if (AppConst.NUMBER.ONE == filter) {
				// All in stock driver inventories
				products = productRepo.findAllInStockDriverInventoryProductsByDispatcherIdAndDriverIdSearch(pageable,
						jwtUser.getUserEntity().getUserDispatcherDetail().getId(),
						driverUserEntity.getUserDriverDetail().getId(), searchQuery);
			} else if (AppConst.NUMBER.TWO == filter) {
				// All out of stock driver inventories
				products = productRepo.findAllOutOfStockDriverInventoryProductsByDispatcherIdAndDriverIdSearch(pageable,
						jwtUser.getUserEntity().getUserDispatcherDetail().getId(),
						driverUserEntity.getUserDriverDetail().getId(), searchQuery);
			} else {
				throw new AppException(StringConst.INVALID_FILTER, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
			}
		}

		List<DriverInventoryModel> driverInventoryModels = new ArrayList<>();

		if (!products.getContent().isEmpty()) {
			log.info("listDriverInventories :: products " + products);
			driverInventoryModels = products.getContent().stream().filter(predicate -> !predicate.isDeleted())
					.map(product -> dispatcherBuilder.createDriverInventoryModelFromProduct(product,
							driverUserEntity.getUserDriverDetail().getId()))
					.collect(Collectors.toList());
		}
		log.info("listDriverInventories :: driverInventoryModels " + driverInventoryModels);

		int nextPage;
		if (products.getContent().isEmpty()
				|| products.getTotalPages() == pageable.getPageNumber() + AppConst.NUMBER.ONE
				|| products.getTotalPages() == AppConst.NUMBER.ONE) {
			nextPage = -AppConst.NUMBER.ONE;
		} else {
			nextPage = (pageable.getPageNumber() + AppConst.NUMBER.ONE) + AppConst.NUMBER.ONE;
		}
		return new PaginatedResponse<>(driverInventoryModels, (int) products.getTotalPages(),
				products.getTotalElements(), nextPage);
	}

	@Override
	@Transactional
	public DriverInventoryModel driverInventory(int driverUserId, int productId, JwtUser jwtUser) {
		log.info("driverInventory");
		if (null == jwtUser.getUserEntity().getUserDispatcherDetail())
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		UserEntity driverUserEntity = userEntityRepo.findDriverByUserIdAndDispatcherId(driverUserId,
				jwtUser.getUserEntity().getUserDispatcherDetail().getId());
		if (null == driverUserEntity || null == driverUserEntity.getUserDriverDetail())
			throw new AppException(StringConst.DRIVER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		Product product = productRepo.findProductByIdAndDispatcherId(productId,
				jwtUser.getUserEntity().getUserDispatcherDetail().getId());
		if (null == product)
			throw new AppException(StringConst.PRODUCT_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("driverInventory :: product " + product);

		return dispatcherBuilder.createDriverInventoryModelFromProduct(product,
				driverUserEntity.getUserDriverDetail().getId());
	}

}
