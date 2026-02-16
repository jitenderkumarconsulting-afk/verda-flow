package com.org.verdaflow.rest.api.dispatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.org.verdaflow.rest.api.admin.model.UserDetailModel;
import com.org.verdaflow.rest.api.auth.AuthBuilder;
import com.org.verdaflow.rest.api.auth.model.UserDispatcherDetailModel;
import com.org.verdaflow.rest.api.auth.model.UserDriverDetailModel;
import com.org.verdaflow.rest.api.customer.CustomerBuilder;
import com.org.verdaflow.rest.api.customer.model.CustomerAddressDetailModel;
import com.org.verdaflow.rest.api.dispatcher.form.AssignOrderDriverForm;
import com.org.verdaflow.rest.api.dispatcher.form.DriverInventoryDetailForm;
import com.org.verdaflow.rest.api.dispatcher.form.DriverInventoryForm;
import com.org.verdaflow.rest.api.dispatcher.form.ProductForm;
import com.org.verdaflow.rest.api.dispatcher.form.PromoCodeForm;
import com.org.verdaflow.rest.api.dispatcher.form.RegisterDriverForm;
import com.org.verdaflow.rest.api.dispatcher.model.DriverInventoryDetailModel;
import com.org.verdaflow.rest.api.dispatcher.model.DriverInventoryModel;
import com.org.verdaflow.rest.api.dispatcher.model.GraphCountModel;
import com.org.verdaflow.rest.api.dispatcher.model.ProductCountGraphModel;
import com.org.verdaflow.rest.api.dispatcher.model.ProductCountGraphModelForWeekOrMonthOrYear;
import com.org.verdaflow.rest.api.dispatcher.model.ProductEffectMappingModel;
import com.org.verdaflow.rest.api.dispatcher.model.ProductModel;
import com.org.verdaflow.rest.api.dispatcher.model.ProductPriceDetailModel;
import com.org.verdaflow.rest.api.dispatcher.model.PromoCodeModel;
import com.org.verdaflow.rest.api.driver.DriverBuilder;
import com.org.verdaflow.rest.api.driver.model.DriverLocationDetailModel;
import com.org.verdaflow.rest.api.master.MasterBuilder;
import com.org.verdaflow.rest.api.master.model.MasterModel;
import com.org.verdaflow.rest.api.user.UserBuilder;
import com.org.verdaflow.rest.api.user.form.RateUserForm;
import com.org.verdaflow.rest.api.user.model.AuditOrderStatusModel;
import com.org.verdaflow.rest.api.user.model.OrderItemDetailModel;
import com.org.verdaflow.rest.api.user.model.OrderModel;
import com.org.verdaflow.rest.api.user.model.OrderPriceDetailModel;
import com.org.verdaflow.rest.api.user.model.OrderRatingDetailModel;
import com.org.verdaflow.rest.common.enums.ApplicationStatus;
import com.org.verdaflow.rest.common.enums.OrderStatus;
import com.org.verdaflow.rest.common.enums.PriceType;
import com.org.verdaflow.rest.common.enums.ProductAggregateType;
import com.org.verdaflow.rest.common.enums.PromoCodeType;
import com.org.verdaflow.rest.common.model.UserRoleModel;
import com.org.verdaflow.rest.config.common.AppConst;
import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.config.security.jwt.JwtUser;
import com.org.verdaflow.rest.entity.CustomerAddressDetail;
import com.org.verdaflow.rest.entity.DriverInventory;
import com.org.verdaflow.rest.entity.DriverInventoryDetail;
import com.org.verdaflow.rest.entity.DriverLocationDetail;
import com.org.verdaflow.rest.entity.DriverOrderMapping;
import com.org.verdaflow.rest.entity.MasterCategory;
import com.org.verdaflow.rest.entity.MasterEffect;
import com.org.verdaflow.rest.entity.MasterType;
import com.org.verdaflow.rest.entity.Order;
import com.org.verdaflow.rest.entity.Product;
import com.org.verdaflow.rest.entity.ProductAggregate;
import com.org.verdaflow.rest.entity.ProductAggregateDetail;
import com.org.verdaflow.rest.entity.ProductEffectMapping;
import com.org.verdaflow.rest.entity.ProductPriceDetail;
import com.org.verdaflow.rest.entity.PromoCode;
import com.org.verdaflow.rest.entity.UserDriverDetail;
import com.org.verdaflow.rest.entity.UserEntity;
import com.org.verdaflow.rest.entity.UserRoleMapping;
import com.org.verdaflow.rest.error.AppException;
import com.org.verdaflow.rest.event.ActivateDriverEvent;
import com.org.verdaflow.rest.event.DeactivateDriverEvent;
import com.org.verdaflow.rest.event.WelcomeDriverEvent;
import com.org.verdaflow.rest.repo.CartDetailRepo;
import com.org.verdaflow.rest.repo.DriverInventoryDetailRepo;
import com.org.verdaflow.rest.repo.DriverInventoryRepo;
import com.org.verdaflow.rest.repo.DriverLocationDetailRepo;
import com.org.verdaflow.rest.repo.DriverOrderMappingRepo;
import com.org.verdaflow.rest.repo.MasterCategoryRepo;
import com.org.verdaflow.rest.repo.MasterEffectRepo;
import com.org.verdaflow.rest.repo.MasterTypeRepo;
import com.org.verdaflow.rest.repo.OrderRatingDetailRepo;
import com.org.verdaflow.rest.repo.OrderRepo;
import com.org.verdaflow.rest.repo.ProductAggregateDetailRepo;
import com.org.verdaflow.rest.repo.ProductAggregateRepo;
import com.org.verdaflow.rest.repo.ProductEffectMappingRepo;
import com.org.verdaflow.rest.repo.ProductPriceDetailRepo;
import com.org.verdaflow.rest.repo.ProductRepo;
import com.org.verdaflow.rest.repo.PromoCodeRepo;
import com.org.verdaflow.rest.repo.UserDriverDetailRepo;
import com.org.verdaflow.rest.repo.UserEntityRepo;
import com.org.verdaflow.rest.util.AWSImageUpload;
import com.org.verdaflow.rest.util.AppUtil;
import com.org.verdaflow.rest.util.DateUtil;

@Component
public class DispatcherBuilder {
	public static final Logger log = LoggerFactory.getLogger(DispatcherBuilder.class);

	@Autowired
	private AppUtil appUtil;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserEntityRepo userEntityRepo;

	@Autowired
	private AuthBuilder authBuilder;

	@Autowired
	private AWSImageUpload awsImageUpload;

	@Autowired
	private UserDriverDetailRepo userDriverDetailRepo;

	@Autowired
	private ApplicationEventMulticaster applicationEventMulticaster;

	@Autowired
	private MasterCategoryRepo masterCategoryRepo;

	@Autowired
	private MasterTypeRepo masterTypeRepo;

	@Autowired
	private PromoCodeRepo promoCodeRepo;

	@Autowired
	private ProductRepo productRepo;

	@Autowired
	private MasterEffectRepo masterEffectRepo;

	@Autowired
	private ProductEffectMappingRepo productEffectMappingRepo;

	@Autowired
	private MasterBuilder masterBuilder;

	@Autowired
	private ProductPriceDetailRepo productPriceDetailRepo;

	@Autowired
	private DriverOrderMappingRepo driverOrderMappingRepo;

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private CustomerBuilder customerBuilder;

	@Autowired
	private ProductAggregateRepo productAggregateRepo;

	@Autowired
	private ProductAggregateDetailRepo productAggregateDetailRepo;

	@Autowired
	private DriverBuilder driverBuilder;

	@Autowired
	private CartDetailRepo cartDetailRepo;

	@Autowired
	private OrderRatingDetailRepo orderRatingDetailRepo;

	@Autowired
	private UserBuilder userBuilder;

	@Autowired
	private DriverLocationDetailRepo driverLocationDetailRepo;

	@Autowired
	private DriverInventoryRepo driverInventoryRepo;

	@Autowired
	private DriverInventoryDetailRepo driverInventoryDetailRepo;

	@Transactional
	public UserDetailModel createDispatcherUserDetailModel(UserEntity userEntity) {
		log.info("createDispatcherUserDetailModel");
		UserDispatcherDetailModel userDispatcherDetailModel = authBuilder
				.createUserDispatcherDetailModel(userEntity.getUserDispatcherDetail());

		return new UserDetailModel(authBuilder.createUserModel(userEntity),
				userEntity.getUserRoleMappings().stream()
						.map(mapper -> new UserRoleModel(mapper.getMasterRole().getId(),
								String.valueOf(mapper.getMasterRole().getRole()), mapper.getApplicationStatus()))
						.collect(Collectors.toList()),
				userDispatcherDetailModel);
	}

	@Transactional
	public UserEntity registerDriver(RegisterDriverForm registerDriverForm, String password, JwtUser jwtUser) {
		log.info("registerDriver");
		UserEntity userEntity = saveUserDetail(registerDriverForm, password);
		log.info("registerDriver :: userEntity " + userEntity);

		userEntity = saveUserDriverDetail(registerDriverForm, userEntity, jwtUser);

		return userEntity;
	}

	@Transactional
	public UserEntity saveUserDetail(RegisterDriverForm registerDriverForm, String password) {
		log.info("saveUserDetail");
		UserEntity userEntity = new UserEntity();
		userEntity.setEmail(registerDriverForm.getEmail());
		userEntity.setCountryCode(registerDriverForm.getCountryCode());
		userEntity.setMobileNumber(registerDriverForm.getMobileNumber());
		userEntity.setPassword(passwordEncoder.encode(password));
		userEntity.setActive(true);
		userEntityRepo.save(userEntity);

		log.info("saveUserDetail :: userEntity " + userEntity);

		// Set the User Role Mapping
		List<UserRoleMapping> userRoleMappings = authBuilder.saveUserRoleMapping(AppConst.USER_ROLE.DRIVER, userEntity,
				ApplicationStatus.APPROVED);
		userEntity.setUserRoleMappings(userRoleMappings);

		return userEntity;
	}

	@Transactional
	public UserEntity saveUserDriverDetail(RegisterDriverForm registerDriverForm, UserEntity userEntity,
			JwtUser jwtUser) {
		log.info("saveUserDriverDetail");
		UserDriverDetail userDriverDetail = new UserDriverDetail();
		userDriverDetail.setName(registerDriverForm.getName());
		if (DateUtil.getCurrentYear() < registerDriverForm.getYear())
			throw new AppException(StringConst.YEAR_CANNOT_BE_FUTURE, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
		String year = Integer.toString(registerDriverForm.getYear());
		userDriverDetail.setYear(year);
		userDriverDetail.setModel(registerDriverForm.getModel());
		userDriverDetail.setMake(registerDriverForm.getMake());
		userDriverDetail.setApplicationStatus(ApplicationStatus.APPROVED);
		userDriverDetail.setOnline(false);
		userDriverDetail.setActive(true);
		userDriverDetail.setUser(userEntity);
		userDriverDetail.setUserDispatcherDetail(jwtUser.getUserEntity().getUserDispatcherDetail());
		userDriverDetailRepo.save(userDriverDetail);

		log.info("saveUserDriverDetail :: userDriverDetail " + userDriverDetail);

		// save user driver images
		userDriverDetail = saveUserDriverImages(registerDriverForm, userDriverDetail);

		// save user driver location details
		userDriverDetail = saveUserDriverLocationDetails(userDriverDetail);

		userEntity.setUserDriverDetail(userDriverDetail);

		return userEntity;
	}

	@Transactional
	public UserDriverDetail saveUserDriverImages(RegisterDriverForm registerDriverForm,
			UserDriverDetail userDriverDetail) {
		log.info("saveUserDriverImages");
		if (null != registerDriverForm.getImage() && StringUtils.isNotBlank(registerDriverForm.getImage())
				&& null != registerDriverForm.getImageThumb()
				&& StringUtils.isNotBlank(registerDriverForm.getImageThumb())) {
			if (appUtil.validateBase64Image(registerDriverForm.getImage())
					&& appUtil.validateBase64Image(registerDriverForm.getImageThumb())) {
				String imageUrl = awsImageUpload.uploadFile(registerDriverForm.getImage(), userDriverDetail.getId(),
						AppConst.UPLOAD_TYPE.DRIVER, AppConst.IMAGE_TYPE.ORIGINAL);
				log.info("saveUserDriverImages :: imageUrl " + imageUrl);

				String imageThumbUrl = awsImageUpload.uploadFile(registerDriverForm.getImageThumb(),
						userDriverDetail.getId(), AppConst.UPLOAD_TYPE.DRIVER, AppConst.IMAGE_TYPE.THUMB);
				log.info("saveUserDriverImages :: imageThumbUrl " + imageThumbUrl);

				userDriverDetail.setImage(imageUrl);
				userDriverDetail.setImageThumb(imageThumbUrl);
			}
		}

		if (null != registerDriverForm.getVehicleRegistrationPhoto()
				&& StringUtils.isNotBlank(registerDriverForm.getVehicleRegistrationPhoto())
				&& appUtil.validateBase64Image(registerDriverForm.getVehicleRegistrationPhoto())) {
			String vehicleRegistrationPhotoUrl = awsImageUpload.uploadFile(
					registerDriverForm.getVehicleRegistrationPhoto(), userDriverDetail.getId(),
					AppConst.UPLOAD_TYPE.DRIVER, AppConst.IMAGE_TYPE.ORIGINAL);
			log.info("saveUserDriverImages :: vehicleRegistrationPhotoUrl " + vehicleRegistrationPhotoUrl);

			userDriverDetail.setVehicleRegistrationPhoto(vehicleRegistrationPhotoUrl);
		}

		if (null != registerDriverForm.getDriverLicensePhoto()
				&& StringUtils.isNotBlank(registerDriverForm.getDriverLicensePhoto())
				&& appUtil.validateBase64Image(registerDriverForm.getDriverLicensePhoto())) {
			String driverLicensePhotoUrl = awsImageUpload.uploadFile(registerDriverForm.getDriverLicensePhoto(),
					userDriverDetail.getId(), AppConst.UPLOAD_TYPE.DRIVER, AppConst.IMAGE_TYPE.ORIGINAL);
			log.info("saveUserDriverImages :: driverLicensePhotoUrl " + driverLicensePhotoUrl);

			userDriverDetail.setDriverLicensePhoto(driverLicensePhotoUrl);
		}

		if (null != registerDriverForm.getCarLicensePlatePhoto()
				&& StringUtils.isNotBlank(registerDriverForm.getCarLicensePlatePhoto())
				&& appUtil.validateBase64Image(registerDriverForm.getCarLicensePlatePhoto())) {
			String carLicensePlatePhotoUrl = awsImageUpload.uploadFile(registerDriverForm.getCarLicensePlatePhoto(),
					userDriverDetail.getId(), AppConst.UPLOAD_TYPE.DRIVER, AppConst.IMAGE_TYPE.ORIGINAL);
			log.info("saveUserDriverImages :: carLicensePlatePhotoUrl " + carLicensePlatePhotoUrl);

			userDriverDetail.setCarLicensePlatePhoto(carLicensePlatePhotoUrl);
		}

		userDriverDetailRepo.save(userDriverDetail);

		return userDriverDetail;
	}

	@Transactional
	public UserDriverDetail saveUserDriverLocationDetails(UserDriverDetail userDriverDetail) {
		DriverLocationDetail driverLocationDetail = new DriverLocationDetail();
		driverLocationDetail.setUserDriverDetail(userDriverDetail);
		driverLocationDetail.setLat(AppConst.DOUBLE_ZERO);
		driverLocationDetail.setLng(AppConst.DOUBLE_ZERO);
		driverLocationDetail.setRotation(AppConst.DOUBLE_ZERO);

		driverLocationDetailRepo.save(driverLocationDetail);
		log.info("saveUserDriverLocationDetails :: driverLocationDetail " + driverLocationDetail);

		userDriverDetail.setDriverLocationDetail(driverLocationDetail);

		return userDriverDetail;
	}

	@Transactional
	public boolean welcomeDriverEmail(UserEntity userEntity, String password) {
		log.info("welcomeDriverEmail");
		WelcomeDriverEvent welcomeDriverEvent = new WelcomeDriverEvent(this, userEntity.getEmail(),
				userEntity.getMobileNumber(), password, userEntity.getUserDriverDetail().getName());

		applicationEventMulticaster.multicastEvent(welcomeDriverEvent);

		return true;
	}

	@Transactional
	public boolean activateDriverEmail(UserEntity userEntity, boolean deact) {
		log.info("activateDriverEmail");

		if (deact) {
			DeactivateDriverEvent deactivateDriverEvent = new DeactivateDriverEvent(this, userEntity.getEmail(),
					userEntity.getUserDriverDetail().getName());

			applicationEventMulticaster.multicastEvent(deactivateDriverEvent);
		} else {
			ActivateDriverEvent activateDriverEvent = new ActivateDriverEvent(this, userEntity.getEmail(),
					userEntity.getUserDriverDetail().getName());

			applicationEventMulticaster.multicastEvent(activateDriverEvent);
		}

		return true;
	}

	public UserEntity updateUserDriverDetail(RegisterDriverForm registerDriverForm, UserEntity driverUserEntity) {
		log.info("updateUserDriverDetail");

		if (userEntityRepo.checkUserExistenceByEmailAndRoleExceptCurrent(registerDriverForm.getEmail(),
				AppConst.USER_ROLE.DRIVER, driverUserEntity.getId()) > 0)
			throw new AppException(StringConst.EMAIL_ALREADY_EXIST, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
		driverUserEntity.setEmail(registerDriverForm.getEmail());

		if (userEntityRepo.checkUserExistenceByMobileNumberAndRoleExceptCurrent(registerDriverForm.getCountryCode(),
				registerDriverForm.getMobileNumber(), AppConst.USER_ROLE.DRIVER, driverUserEntity.getId()) > 0)
			throw new AppException(StringConst.MOBILE_ALREADY_EXIST, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
		driverUserEntity.setCountryCode(registerDriverForm.getCountryCode());
		driverUserEntity.setMobileNumber(registerDriverForm.getMobileNumber());

		driverUserEntity.getUserDriverDetail().setName(registerDriverForm.getName());
		if (DateUtil.getCurrentYear() < registerDriverForm.getYear())
			throw new AppException(StringConst.YEAR_CANNOT_BE_FUTURE, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
		String year = Integer.toString(registerDriverForm.getYear());
		driverUserEntity.getUserDriverDetail().setYear(year);
		driverUserEntity.getUserDriverDetail().setModel(registerDriverForm.getModel());
		driverUserEntity.getUserDriverDetail().setMake(registerDriverForm.getMake());

		// save user driver images
		driverUserEntity = updateUserDriverImages(registerDriverForm, driverUserEntity);

		// Here saving the updated detail for UserDriverDetail.
		userDriverDetailRepo.save(driverUserEntity.getUserDriverDetail());

		log.info("updateUserDriverDetail :: driverUserEntity.getUserDriverDetail() "
				+ driverUserEntity.getUserDriverDetail());

		return driverUserEntity;
	}

	@Transactional
	public UserEntity updateUserDriverImages(RegisterDriverForm registerDriverForm, UserEntity driverUserEntity) {
		log.info("updateUserDriverImages");
		if (null != registerDriverForm.getImage() && StringUtils.isNotBlank(registerDriverForm.getImage())
				&& null != registerDriverForm.getImageThumb()
				&& StringUtils.isNotBlank(registerDriverForm.getImageThumb())) {
			if (appUtil.validateBase64Image(registerDriverForm.getImage())
					&& appUtil.validateBase64Image(registerDriverForm.getImageThumb())) {
				String imageUrl = awsImageUpload.uploadFile(registerDriverForm.getImage(),
						driverUserEntity.getUserDriverDetail().getId(), AppConst.UPLOAD_TYPE.DRIVER,
						AppConst.IMAGE_TYPE.ORIGINAL);
				log.info("updateUserDriverImages :: imageUrl " + imageUrl);

				String imageThumbUrl = awsImageUpload.uploadFile(registerDriverForm.getImageThumb(),
						driverUserEntity.getUserDriverDetail().getId(), AppConst.UPLOAD_TYPE.DRIVER,
						AppConst.IMAGE_TYPE.THUMB);
				log.info("updateUserDriverImages :: imageThumbUrl " + imageThumbUrl);

				driverUserEntity.getUserDriverDetail().setImage(imageUrl);
				driverUserEntity.getUserDriverDetail().setImageThumb(imageThumbUrl);
			}
		}

		if (null != registerDriverForm.getVehicleRegistrationPhoto()
				&& StringUtils.isNotBlank(registerDriverForm.getVehicleRegistrationPhoto())) {
			if (appUtil.validateBase64Image(registerDriverForm.getVehicleRegistrationPhoto())) {
				String vehicleRegistrationPhotoUrl = awsImageUpload.uploadFile(
						registerDriverForm.getVehicleRegistrationPhoto(),
						driverUserEntity.getUserDriverDetail().getId(), AppConst.UPLOAD_TYPE.DRIVER,
						AppConst.IMAGE_TYPE.ORIGINAL);
				log.info("updateUserDriverImages :: vehicleRegistrationPhotoUrl " + vehicleRegistrationPhotoUrl);

				driverUserEntity.getUserDriverDetail().setVehicleRegistrationPhoto(vehicleRegistrationPhotoUrl);
			}
		} else {
			driverUserEntity.getUserDriverDetail().setVehicleRegistrationPhoto(null);
		}

		if (null != registerDriverForm.getDriverLicensePhoto()
				&& StringUtils.isNotBlank(registerDriverForm.getDriverLicensePhoto())) {
			if (appUtil.validateBase64Image(registerDriverForm.getDriverLicensePhoto())) {
				String driverLicensePhotoUrl = awsImageUpload.uploadFile(registerDriverForm.getDriverLicensePhoto(),
						driverUserEntity.getUserDriverDetail().getId(), AppConst.UPLOAD_TYPE.DRIVER,
						AppConst.IMAGE_TYPE.ORIGINAL);
				log.info("updateUserDriverImages :: driverLicensePhotoUrl " + driverLicensePhotoUrl);

				driverUserEntity.getUserDriverDetail().setDriverLicensePhoto(driverLicensePhotoUrl);

			}
		} else {
			driverUserEntity.getUserDriverDetail().setDriverLicensePhoto(null);
		}

		if (null != registerDriverForm.getCarLicensePlatePhoto()
				&& StringUtils.isNotBlank(registerDriverForm.getCarLicensePlatePhoto())) {
			if (appUtil.validateBase64Image(registerDriverForm.getCarLicensePlatePhoto())) {
				String carLicensePlatePhotoUrl = awsImageUpload.uploadFile(registerDriverForm.getCarLicensePlatePhoto(),
						driverUserEntity.getUserDriverDetail().getId(), AppConst.UPLOAD_TYPE.DRIVER,
						AppConst.IMAGE_TYPE.ORIGINAL);
				log.info("updateUserDriverImages :: carLicensePlatePhotoUrl " + carLicensePlatePhotoUrl);

				driverUserEntity.getUserDriverDetail().setCarLicensePlatePhoto(carLicensePlatePhotoUrl);
			}
		} else {
			driverUserEntity.getUserDriverDetail().setCarLicensePlatePhoto(null);
		}

		return driverUserEntity;
	}

	@Transactional
	public boolean saveProductDetails(ProductForm productForm, JwtUser jwtUser) {
		log.info("saveProductDetails");
		Product product = new Product();
		product.setName(productForm.getName());
		product.setGroupType(productForm.getGroupType());
		product.setDescription(productForm.getDescription());
		product.setThc(productForm.getThc());

		if (AppConst.PRODUCT_GROUP.VAPE_OIL_OR_CARTRIDGES == productForm.getGroupType())
			product.setCbd(productForm.getCbd());
		else
			product.setCbd(AppConst.FLOAT_ZERO);

		MasterCategory masterCategory = masterCategoryRepo.findByCategoryId(productForm.getCategoryId());
		if (null == masterCategory)
			throw new AppException(StringConst.CATEGORY_ID_INVALID, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
		product.setMasterCategory(masterCategory);
		log.info("saveProductDetails :: masterCategory " + masterCategory);

		MasterType masterType = masterTypeRepo.findByTypeId(productForm.getTypeId());
		if (null == masterType)
			throw new AppException(StringConst.TYPE_ID_INVALID, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
		product.setType(masterType);
		log.info("saveProductDetails :: masterType " + masterType);

		if (AppConst.NUMBER.ZERO != productForm.getPromoCodeId()) {
			PromoCode promoCode = promoCodeRepo.findByPromoCodeId(productForm.getPromoCodeId());
			if (null == promoCode)
				throw new AppException(StringConst.PROMOCODE_INVALID, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
			log.info("saveProductDetails :: promoCode " + promoCode);

			product.setPromoCode(promoCode);
		}

		product.setActive(true);
		product.setUserDispatcherDetail(jwtUser.getUserEntity().getUserDispatcherDetail());

		productRepo.save(product);
		log.info("saveProductDetails :: product " + product);

		// save product price details
		product = saveProductPriceDetails(productForm, product);

		// save product effect mappings details
		product = saveProductEffectMappings(productForm, product);

		// save product images
		product = saveProductImages(productForm, product);

		return true;
	}

	@Transactional
	public Product saveProductEffectMappings(ProductForm productForm, Product product) {
		log.info("saveProductEffectMappings");
		List<ProductEffectMapping> productEffectMappings = new ArrayList<>();

		if (null != productForm.getProductEffectMappings() && !productForm.getProductEffectMappings().isEmpty()) {
			productForm.getProductEffectMappings().forEach(mapping -> {
				ProductEffectMapping productEffectMapping = new ProductEffectMapping();
				productEffectMapping.setProduct(product);
				productEffectMapping.setValue(mapping.getValue());

				MasterEffect masterEffect = masterEffectRepo.findByEffectId(mapping.getId());
				if (null == masterEffect)
					throw new AppException(StringConst.EFFECT_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
				productEffectMapping.setMasterEffect(masterEffect);
				log.info("saveProductEffectMappings :: masterEffect " + masterEffect);

				productEffectMappings.add(productEffectMapping);
			});
		}

		log.info("saveProductEffectMappings :: productEffectMappings " + productEffectMappings);
		productEffectMappingRepo.save(productEffectMappings);

		product.setProductEffectMappings(productEffectMappings);

		return product;
	}

	@Transactional
	public Product saveProductPriceDetails(ProductForm productForm, Product product) {
		log.info("saveProductPriceDetails");
		List<ProductPriceDetail> productPriceDetails = new ArrayList<>();

		if (null != productForm.getProductPriceDetails() && !productForm.getProductPriceDetails().isEmpty()) {
			productForm.getProductPriceDetails().forEach(mapping -> {
				ProductPriceDetail productPriceDetail = new ProductPriceDetail();
				productPriceDetail.setProduct(product);
				productPriceDetail.setPrice(mapping.getPrice());
				productPriceDetail.setAvailable(true);

				if (null == PriceType.valueOf(mapping.getPriceType()))
					throw new AppException(StringConst.PRICE_TYPE_INVALID, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

				productPriceDetail.setPriceType(mapping.getPriceType());

				productPriceDetails.add(productPriceDetail);
			});
		}
		log.info("saveProductEffectMappings :: productPriceDetails " + productPriceDetails);
		productPriceDetailRepo.save(productPriceDetails);

		product.setProductPriceDetails(productPriceDetails);

		return product;
	}

	@Transactional
	public Product saveProductImages(ProductForm productForm, Product product) {
		log.info("saveProductImages");
		if (null != productForm.getImage() && StringUtils.isNotBlank(productForm.getImage())
				&& null != productForm.getImageThumb() && StringUtils.isNotBlank(productForm.getImageThumb())) {
			if (appUtil.validateBase64Image(productForm.getImage())
					&& appUtil.validateBase64Image(productForm.getImageThumb())) {
				String imageUrl = awsImageUpload.uploadFile(productForm.getImage(), product.getId(),
						AppConst.UPLOAD_TYPE.PRODUCT, AppConst.IMAGE_TYPE.ORIGINAL);
				log.info("saveProductImages :: imageUrl " + imageUrl);

				String imageThumbUrl = awsImageUpload.uploadFile(productForm.getImageThumb(), product.getId(),
						AppConst.UPLOAD_TYPE.PRODUCT, AppConst.IMAGE_TYPE.THUMB);
				log.info("saveProductImages :: imageThumbUrl " + imageThumbUrl);

				product.setImage(imageUrl);
				product.setImageThumb(imageThumbUrl);

				productRepo.save(product);
				log.info("saveProductImages::product" + product);
			}
		}

		return product;
	}

	@Transactional
	public Product updateProductDetails(ProductForm productForm, Product product, JwtUser jwtUser) {
		log.info("saveUpdateProduct");
		product.setName(productForm.getName());

		if (AppConst.PRODUCT_GROUP.VAPE_OIL_OR_CARTRIDGES != productForm.getGroupType()
				&& AppConst.PRODUCT_GROUP.EDIBLES != productForm.getGroupType())
			throw new AppException(StringConst.GROUP_TYPE_INVALID, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		product.setGroupType(productForm.getGroupType());
		product.setDescription(productForm.getDescription());
		product.setThc(productForm.getThc());

		if (AppConst.PRODUCT_GROUP.VAPE_OIL_OR_CARTRIDGES == productForm.getGroupType())
			product.setCbd(productForm.getCbd());
		else
			product.setCbd(AppConst.FLOAT_ZERO);

		MasterCategory masterCategory = masterCategoryRepo.findByCategoryId(productForm.getCategoryId());
		if (null == masterCategory)
			throw new AppException(StringConst.CATEGORY_ID_INVALID, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		product.setMasterCategory(masterCategory);

		MasterType masterType = masterTypeRepo.findByTypeId(productForm.getTypeId());
		if (null == masterType)
			throw new AppException(StringConst.TYPE_ID_INVALID, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		product.setType(masterType);

		if (AppConst.NUMBER.ZERO != productForm.getPromoCodeId()) {
			PromoCode promoCode = promoCodeRepo.findPromoCodeByIdAndDispatcherId(productForm.getPromoCodeId(),
					jwtUser.getUserEntity().getUserDispatcherDetail().getId());
			if (null == promoCode)
				throw new AppException(StringConst.PROMOCODE_INVALID, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

			product.setPromoCode(promoCode);
		} else {
			product.setPromoCode(null);
		}

		// save product price details
		product = updateProductPriceDetails(productForm, product);

		// save product effect mappings details
		product = updateProductEffectMappings(productForm, product);

		// save product images
		product = updateProductImages(productForm, product);

		productRepo.save(product);
		log.info("saveUpdateProduct :: product " + product);

		return product;
	}

	@Transactional
	public Product updateProductPriceDetails(ProductForm productForm, Product product) {
		log.info("updateProductPriceDetails :: product.getProductPriceDetails() " + product.getProductPriceDetails());
		List<ProductPriceDetail> productPriceDetails = new ArrayList<>();

		product.getProductPriceDetails().stream().forEach(priceDetail -> {
			priceDetail.setAvailable(false);
		});
		productPriceDetailRepo.save(product.getProductPriceDetails());

		if (null != productForm.getProductPriceDetails() && !productForm.getProductPriceDetails().isEmpty()) {
			productForm.getProductPriceDetails().forEach(mapping -> {
				if (null == PriceType.valueOf(mapping.getPriceType()))
					throw new AppException(StringConst.PRICE_TYPE_INVALID, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

				ProductPriceDetail productPriceDetail = productPriceDetailRepo
						.findByPriceTypeAndProductId(mapping.getPriceType(), product.getId());
				if (null == productPriceDetail) {
					productPriceDetail = new ProductPriceDetail();
					productPriceDetail.setProduct(product);
					productPriceDetail.setPriceType(mapping.getPriceType());
				}

				productPriceDetail.setPrice(mapping.getPrice());
				productPriceDetail.setAvailable(true);

				productPriceDetails.add(productPriceDetail);
			});
		}
		log.info("updateProductPriceDetails :: productPriceDetails " + productPriceDetails);
		productPriceDetailRepo.save(productPriceDetails);

		product.setProductPriceDetails(productPriceDetails);

		return product;
	}

	@Transactional
	public Product updateProductEffectMappings(ProductForm productForm, Product product) {
		log.info("updateProductEffectMappings :: product.getProductEffectMappings() "
				+ product.getProductEffectMappings());
		productEffectMappingRepo.delete(product.getProductEffectMappings());
		product.getProductEffectMappings().clear();

		// save product effect mappings details
		product = saveProductEffectMappings(productForm, product);

		return product;
	}

	@Transactional
	public Product updateProductImages(ProductForm productForm, Product product) {
		log.info("updateProductImages");
		if (null != productForm.getImage() && StringUtils.isNotBlank(productForm.getImage())
				&& null != productForm.getImageThumb() && StringUtils.isNotBlank(productForm.getImageThumb())) {
			if (appUtil.validateBase64Image(productForm.getImage())
					&& appUtil.validateBase64Image(productForm.getImageThumb())) {
				String imageUrl = awsImageUpload.uploadFile(productForm.getImage(), product.getId(),
						AppConst.UPLOAD_TYPE.PRODUCT, AppConst.IMAGE_TYPE.ORIGINAL);
				log.info("updateProductImages :: imageUrl " + imageUrl);

				String imageThumbUrl = awsImageUpload.uploadFile(productForm.getImageThumb(), product.getId(),
						AppConst.UPLOAD_TYPE.PRODUCT, AppConst.IMAGE_TYPE.THUMB);
				log.info("updateProductImages :: imageThumbUrl " + imageThumbUrl);

				product.setImage(imageUrl);
				product.setImageThumb(imageThumbUrl);

				productRepo.save(product);
				log.info("updateProductImages::product" + product);
			}
		}

		return product;
	}

	@Transactional
	public ProductModel createProductModel(Product product) {
		log.info("createProductModel");
		String imageUrl = null;
		if (null != product.getImage() && StringUtils.isNotBlank(product.getImage()))
			imageUrl = AppUtil.AWS_S3_BASE_URL_AND_BUCKET + product.getImage();
		log.info("createProductModel :: imageUrl " + imageUrl);

		String imageThumbUrl = null;
		if (null != product.getImageThumb() && StringUtils.isNotBlank(product.getImageThumb()))
			imageThumbUrl = AppUtil.AWS_S3_BASE_URL_AND_BUCKET + product.getImageThumb();
		log.info("createProductModel :: imageThumbUrl " + imageThumbUrl);

		MasterModel categoryModel = masterBuilder.createMasterModel(product.getMasterCategory());
		MasterModel typeModel = masterBuilder.createMasterModel(product.getType());

		PromoCodeModel promoCodeModel = null;
		if (null != product.getPromoCode() && !product.getPromoCode().isDeleted()) {
			promoCodeModel = createPromoCodeModel(product.getPromoCode());
		}

		List<ProductEffectMappingModel> productEffectMappingModels = new ArrayList<>();
		if (null != product.getProductEffectMappings() && !product.getProductEffectMappings().isEmpty()) {
			productEffectMappingModels = product.getProductEffectMappings().stream()
					.filter(predicate -> !predicate.isDeleted())
					.map(productEffectMapping -> createProductEffectMappingModel(productEffectMapping))
					.collect(Collectors.toList());
		}
		log.info("createProductModel :: productEffectMappingModels " + productEffectMappingModels);

		List<ProductPriceDetailModel> productPriceDetailModels = new ArrayList<>();
		if (null != product.getProductPriceDetails() && !product.getProductPriceDetails().isEmpty()) {
			productPriceDetailModels = product.getProductPriceDetails().stream()
					.filter(productPriceDetail -> productPriceDetail.isAvailable())
					.map(productPriceDetail -> createProductPriceDetailModel(productPriceDetail))
					.collect(Collectors.toList());
		}
		log.info("createProductModel :: productPriceDetailModels " + productPriceDetailModels);

		int soldCount = 0;
		int likedCount = 0;

		ProductAggregate soldProductAggregate = productAggregateRepo
				.findByProductIdAndProductAggregateType(product.getId(), ProductAggregateType.SOLD);
		log.info("createProductModel :: soldProductAggregate " + soldProductAggregate);
		if (null != soldProductAggregate)
			soldCount = soldProductAggregate.getCount();

		ProductAggregate likedProductAggregate = productAggregateRepo
				.findByProductIdAndProductAggregateType(product.getId(), ProductAggregateType.LIKED);
		log.info("createProductModel :: likedProductAggregate " + likedProductAggregate);
		if (null != likedProductAggregate)
			likedCount = likedProductAggregate.getCount();

		return new ProductModel(product.getId(), product.getName(), product.getGroupType(), product.getDescription(),
				imageUrl, imageThumbUrl, product.getThc(), product.getCbd(), product.getUserDispatcherDetail().getId(),
				categoryModel, typeModel, promoCodeModel, productEffectMappingModels, productPriceDetailModels,
				product.isActive(), product.getCreatedAt(), soldCount, likedCount);
	}

	@Transactional
	public ProductEffectMappingModel createProductEffectMappingModel(ProductEffectMapping productEffectMapping) {
		log.info("createProductEffectMappingModel");
		MasterModel effect = masterBuilder.createMasterModel(productEffectMapping.getMasterEffect());

		return new ProductEffectMappingModel(productEffectMapping.getId(), productEffectMapping.getProduct().getId(),
				effect, productEffectMapping.getValue());
	}

	@Transactional
	public ProductPriceDetailModel createProductPriceDetailModel(ProductPriceDetail productPriceDetail) {
		log.info("createProductPriceDetailModel");
		return new ProductPriceDetailModel(productPriceDetail.getId(), productPriceDetail.getProduct().getId(),
				productPriceDetail.getPriceType(), productPriceDetail.getPrice());
	}

	@Transactional
	public boolean deleteProduct(Product product) {
		log.info("deleteProduct");
		product.setDeleted(true);
		productRepo.save(product);

		log.info("deleteProduct :: product " + product);

		return true;
	}

	@Transactional
	public PromoCode savePromoCodeDetails(PromoCodeForm promoCodeForm, JwtUser jwtUser) {
		log.info("savePromoCodeDetails");
		PromoCode promoCode = new PromoCode();

		log.info("savePromoCodeDetails :: currentDate " + DateUtil.getCurrentDate());
		log.info("savePromoCodeDetails :: startDate " + promoCodeForm.getStartDate());
		log.info("savePromoCodeDetails :: endDate " + promoCodeForm.getEndDate());

		if (AppConst.NUMBER.ZERO == promoCodeForm.getStartDate())
			throw new AppException(StringConst.PROMOCODE_DATE_CURRENT_OR_FUTURE_INVALID,
					AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		Date startDate = DateUtil.convertMillisecondsToDate(promoCodeForm.getStartDate());
		log.info("savePromoCodeDetails :: startDate " + startDate);
		if (null == startDate || DateUtil.isPastDate(startDate))
			throw new AppException(StringConst.PROMOCODE_DATE_CURRENT_OR_FUTURE_INVALID,
					AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		promoCode.setStartDate(startDate);

		log.info("savePromoCodeDetails :: endDate " + promoCodeForm.getEndDate());
		if (AppConst.NUMBER.ZERO == promoCodeForm.getEndDate())
			throw new AppException(StringConst.PROMOCODE_DATE_CURRENT_OR_FUTURE_INVALID,
					AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		Date endDate = DateUtil.convertMillisecondsToDate(promoCodeForm.getEndDate());
		log.info("savePromoCodeDetails :: endDate " + endDate);
		if (null == endDate || DateUtil.isPastDateTime(endDate))
			throw new AppException(StringConst.PROMOCODE_DATE_CURRENT_OR_FUTURE_INVALID,
					AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		promoCode.setEndDate(endDate);

		if (endDate.before(startDate))
			throw new AppException(StringConst.PROMOCODE_END_DATE_NOT_BEFORE_START_DATE,
					AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		promoCode.setName(promoCodeForm.getName());
		promoCode.setDiscount(promoCodeForm.getDiscount());
		promoCode.setActive(true);
		promoCode.setUserDispatcherDetail(jwtUser.getUserEntity().getUserDispatcherDetail());

		PromoCodeType promoCodeType = PromoCodeType.valueOf(promoCodeForm.getPromoCodeType());
		if (null == promoCodeType)
			throw new AppException(StringConst.PROMOCODE_TYPE_INVALID, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
		promoCode.setPromoCodeType(promoCodeType);

		// save promoCode here
		promoCodeRepo.save(promoCode);

		log.info("savePromoCodeDetails :: promoCode " + promoCode);

		return promoCode;
	}

	@Transactional
	public PromoCode updatePromoCodeDetails(PromoCodeForm promoCodeForm, PromoCode promoCode, JwtUser jwtUser) {
		log.info("updatePromoCodeDetails");

		promoCode.setName(promoCodeForm.getName());
		promoCode.setDiscount(promoCodeForm.getDiscount());

		if (AppConst.NUMBER.ZERO == promoCodeForm.getStartDate())
			throw new AppException(StringConst.PROMOCODE_DATE_CURRENT_OR_FUTURE_INVALID,
					AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		Date startDate = DateUtil.convertMillisecondsToDate(promoCodeForm.getStartDate());
		if (null == startDate
				|| (!DateUtil.compareDateOnly(startDate, promoCode.getStartDate()) && DateUtil.isPastDate(startDate)))
			throw new AppException(StringConst.PROMOCODE_DATE_CURRENT_OR_FUTURE_INVALID,
					AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
		promoCode.setStartDate(startDate);

		if (AppConst.NUMBER.ZERO == promoCodeForm.getEndDate()) {
			throw new AppException(StringConst.PROMOCODE_DATE_CURRENT_OR_FUTURE_INVALID,
					AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
		}

		Date endDate = DateUtil.convertMillisecondsToDate(promoCodeForm.getEndDate());
		if (null == endDate
				|| (!DateUtil.compareDateOnly(endDate, promoCode.getEndDate()) && DateUtil.isPastDateTime(endDate))) {
			throw new AppException(StringConst.PROMOCODE_DATE_CURRENT_OR_FUTURE_INVALID,
					AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
		}
		promoCode.setEndDate(endDate);

		if (promoCode.getEndDate().before(promoCode.getStartDate()))
			throw new AppException(StringConst.PROMOCODE_END_DATE_NOT_BEFORE_START_DATE,
					AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		PromoCodeType promoCodeType = PromoCodeType.valueOf(promoCodeForm.getPromoCodeType());
		if (null == promoCodeType)
			throw new AppException(StringConst.PROMOCODE_TYPE_INVALID, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		promoCode.setPromoCodeType(promoCodeType);

		// update promoCode here
		promoCodeRepo.save(promoCode);

		log.info("updatePromoCodeDetails :: promoCode " + promoCode);

		return promoCode;
	}

	@Transactional
	public boolean deletePromoCode(PromoCode promoCode, JwtUser jwtUser) {
		log.info("deletePromoCode");

		promoCode.setDeleted(true);
		// delete the promo code here
		promoCodeRepo.save(promoCode);
		log.info("deletePromoCode::promoCode" + promoCode);

		return true;
	}

	@Transactional
	public PromoCodeModel createPromoCodeModel(PromoCode promoCode) {
		log.info("createPromoCodeModel");
		return new PromoCodeModel(promoCode.getId(), promoCode.getUserDispatcherDetail().getId(), promoCode.getName(),
				promoCode.getDiscount(), promoCode.getStartDate(), promoCode.getEndDate(),
				promoCode.getPromoCodeType().getId(), promoCode.isActive(), promoCode.getCreatedAt());
	}

	@Transactional
	public UserEntity activateDriver(UserEntity userEntity, boolean deact) {
		log.info("activateDriver");
		if (deact) {
			userEntity.setActive(false);
			userEntity.getUserDriverDetail().setActive(false);
		} else {
			userEntity.setActive(true);
			userEntity.getUserDriverDetail().setActive(true);
		}

		userDriverDetailRepo.save(userEntity.getUserDriverDetail());
		userEntityRepo.save(userEntity);

		return userEntity;
	}

	@Transactional
	public OrderModel createOrderModel(Order order) {
		log.info("createOrderModel");

		UserDetailModel customerUser = customerBuilder.createCustomerUserDetailModel(order.getUser());
		log.info("createOrderModel :: customerUser " + customerUser);

		UserDetailModel driverUser = null;
		DriverOrderMapping driverOrderMapping = driverOrderMappingRepo.findByOrderId(order.getId());
		if (null != driverOrderMapping && !driverOrderMapping.isRejected())
			driverUser = driverBuilder.createDriverUserDetailModel(driverOrderMapping.getUserDriverDetail().getUser());
		log.info("createOrderModel :: driverUser " + driverUser);

		PromoCodeModel promoCodeModel = null;
		if (null != order.getPromoCode())
			promoCodeModel = createPromoCodeModel(order.getPromoCode());
		log.info("createOrderModel :: promoCodeModel " + promoCodeModel);

		CustomerAddressDetailModel customerAddressDetailModel = null;
		if (null != order.getAddressDetail()) {
			customerAddressDetailModel = createCustomerAddressModel(order.getAddressDetail());
			log.info("createOrderModel :: customerAddressDetailModel " + customerAddressDetailModel);
		}

		List<OrderItemDetailModel> orderItemDetailModels = new ArrayList<>();
		if (null != order.getOrderItemDetails() && !order.getOrderItemDetails().isEmpty()) {
			orderItemDetailModels = order.getOrderItemDetails().stream()
					.map(orderItemDetail -> customerBuilder.createOrderItemDetailModel(orderItemDetail))
					.collect(Collectors.toList());
		}
		log.info("createOrderModel ::orderItemDetailModels " + orderItemDetailModels);

		OrderPriceDetailModel orderPriceDetailModel = null;
		if (null != order.getOrderPriceDetail()) {
			orderPriceDetailModel = customerBuilder.createOrderPriceDetailModel(order.getOrderPriceDetail());
			log.info("createOrderModel :: orderPriceDetailModel " + orderPriceDetailModel);
		}

		OrderRatingDetailModel orderRatingDetailModel = null;
		if (null != order.getOrderRatingDetail()) {
			orderRatingDetailModel = customerBuilder.createOrderRatingDetailModel(order.getOrderRatingDetail());
			log.info("createOrderModel :: orderRatingDetailModel " + orderRatingDetailModel);
		}

		MasterModel etaModel = null;
		if (null != order.getEta())
			etaModel = masterBuilder.createMasterModel(order.getEta());
		log.info("createOrderModel :: etaModel " + etaModel);

		List<AuditOrderStatusModel> auditOrderStatusModels = new ArrayList<>();
		if (null != order.getAuditOrderStatus() && !order.getAuditOrderStatus().isEmpty()) {
			auditOrderStatusModels = order.getAuditOrderStatus().stream()
					.filter(auditOrderStatus -> !auditOrderStatus.isDeleted()
							&& OrderStatus.PROCESSING != auditOrderStatus.getOrderStatus())
					.map(auditOrderStatus -> userBuilder.createAuditOrderStatusModel(auditOrderStatus))
					.collect(Collectors.toList());
			log.info("createOrderModel :: auditOrderStatusModels " + auditOrderStatusModels);
		}

		return new OrderModel(order.getId(), order.getDeliveryType().getId(), customerAddressDetailModel,
				promoCodeModel, orderItemDetailModels, orderPriceDetailModel, orderRatingDetailModel, etaModel,
				order.getOrderStatus().getId(), order.getCreatedAt(), order.getModifiedAt(), customerUser, driverUser,
				auditOrderStatusModels);
	}

	@Transactional
	public CustomerAddressDetailModel createCustomerAddressModel(CustomerAddressDetail customerAddressDetail) {
		log.info("createCustomerAddressModel");
		return new CustomerAddressDetailModel(customerAddressDetail.getId(), customerAddressDetail.getName(),
				customerAddressDetail.getCountryCode(), customerAddressDetail.getPhoneNumber(),
				customerAddressDetail.getAddress(), customerAddressDetail.getLat(), customerAddressDetail.getLng(),
				customerAddressDetail.getCreatedAt());
	}

	@Transactional
	public Order confirmOrder(Order order) {
		log.info("confirmOrder");
		order.setOrderStatus(OrderStatus.CONFIRMED);

		orderRepo.save(order);
		log.info("confirmOrder :: order " + order);

		return order;
	}

	@Transactional
	public Order prepareOrder(Order order) {
		log.info("prepareOrder");
		order.setOrderStatus(OrderStatus.PREPARED);

		orderRepo.save(order);
		log.info("prepareOrder :: order " + order);

		return order;
	}

	@Transactional
	public UserDriverDetailModel callCreateUserDriverDetailModelForAssignOrder(UserDriverDetail userDriverDetail,
			int assignedDriverId) {
		log.info("callCreateUserDriverDetailModelForAssignOrder");
		boolean isAssigned = false;

		if (AppConst.NUMBER.ZERO != assignedDriverId && assignedDriverId == userDriverDetail.getId())
			isAssigned = true;

		log.info("callCreateUserDriverDetailModelForAssignOrder :: isAssigned " + isAssigned);

		return createUserDriverDetailModelForAssignOrder(userDriverDetail, isAssigned);
	}

	@Transactional
	public UserDriverDetailModel createUserDriverDetailModelForAssignOrder(UserDriverDetail userDriverDetail,
			boolean isAssigned) {
		log.info("createUserDriverDetailModelForAssignOrder");
		int assignedIncompleteOrdersCount = orderRepo.findNewAndPendingOrdersCountForDriver(userDriverDetail.getId());
		log.info("createUserDriverDetailModelForAssignOrder :: assignedIncompleteOrdersCount"
				+ assignedIncompleteOrdersCount);

		String imageUrl = null;
		if (null != userDriverDetail.getImage() && StringUtils.isNotBlank(userDriverDetail.getImage()))
			imageUrl = AppUtil.AWS_S3_BASE_URL_AND_BUCKET + userDriverDetail.getImage();
		log.info("createUserDriverDetailModelForAssignOrder :: imageUrl" + imageUrl);

		String imageThumbUrl = null;
		if (null != userDriverDetail.getImageThumb() && StringUtils.isNotBlank(userDriverDetail.getImageThumb()))
			imageThumbUrl = AppUtil.AWS_S3_BASE_URL_AND_BUCKET + userDriverDetail.getImageThumb();
		log.info("createUserDriverDetailModelForAssignOrder :: imageThumbUrl" + imageThumbUrl);

		DriverLocationDetailModel driverLocationDetailModel = null;
		if (null != userDriverDetail.getDriverLocationDetail())
			driverLocationDetailModel = driverBuilder
					.createDriverLocationDetailModel(userDriverDetail.getDriverLocationDetail());

		return new UserDriverDetailModel(userDriverDetail.getId(), userDriverDetail.getName(), imageUrl, imageThumbUrl,
				userDriverDetail.isOnline(), assignedIncompleteOrdersCount, isAssigned, driverLocationDetailModel);
	}

	@Transactional
	public Order assignDriverToOrder(Order order, AssignOrderDriverForm assignOrderDriverForm,
			UserDriverDetail userDriverDetail) {
		log.info("assignDriverToOrder");
		DriverOrderMapping driverOrderMapping = driverOrderMappingRepo
				.findByOrderId(assignOrderDriverForm.getOrderId());
		if (null == driverOrderMapping) {
			driverOrderMapping = new DriverOrderMapping();
			driverOrderMapping.setOrder(order);
		}

		driverOrderMapping.setRejected(false);
		driverOrderMapping.setUserDriverDetail(userDriverDetail);
		driverOrderMappingRepo.save(driverOrderMapping);

		log.info("assignDriverToOrder :: driverOrderMapping " + driverOrderMapping);

		order.setOrderStatus(OrderStatus.DRIVER_ASSIGNED);
		orderRepo.save(order);

		log.info("assignDriverToOrder :: order " + order);

		order.setDriverOrderMapping(driverOrderMapping);

		return order;
	}

	@Transactional
	public Order completeOrder(Order order) {
		log.info("completeOrder");

		order.setOrderStatus(OrderStatus.COMPLETED);
		orderRepo.save(order);
		log.info("completeOrder :: order " + order);

		order.getOrderRatingDetail().setDispatcherPendingByCustomer(true);
		order.getOrderRatingDetail().setCustomerPendingByDispatcher(true);
		orderRatingDetailRepo.save(order.getOrderRatingDetail());
		log.info("completeOrder :: order.getOrderRatingDetail() " + order.getOrderRatingDetail());

		return order;
	}

	@Transactional
	public boolean saveProductAggregatesForProductSold(Order order, UserEntity userEntity) {
		log.info("saveProductAggregates");
		order.getOrderItemDetails().forEach(orderItemDetail -> saveProductAggregates(orderItemDetail.getProduct(),
				ProductAggregateType.SOLD, userEntity));

		return true;
	}

	@Transactional
	public boolean saveProductAggregates(Product product, ProductAggregateType productAggregateType,
			UserEntity userEntity) {
		log.info("saveProductAggregates");
		ProductAggregate productAggregate = productAggregateRepo.findByProductIdAndProductAggregateType(product.getId(),
				productAggregateType);
		log.info("saveProductAggregates :: productAggregate " + productAggregate);

		if (null == productAggregate) {
			productAggregate = new ProductAggregate();
			productAggregate.setProduct(product);
			productAggregate.setProductAggregateType(productAggregateType);
			productAggregate.setCount(AppConst.NUMBER.ONE);
		} else {
			int aggregateCount = productAggregate.getCount() + AppConst.NUMBER.ONE;
			productAggregate.setCount(aggregateCount);
		}

		productAggregateRepo.save(productAggregate);
		log.info("saveProductAggregates :: productAggregate " + productAggregate);

		saveProductAggregateDetails(productAggregate, userEntity);

		return true;
	}

	@Transactional
	public boolean saveProductAggregateDetails(ProductAggregate productAggregate, UserEntity userEntity) {
		log.info("saveProductAggregateDetails");
		ProductAggregateDetail productAggregateDetail = new ProductAggregateDetail();
		productAggregateDetail.setUser(userEntity);
		productAggregateDetail.setProductAggregate(productAggregate);

		productAggregateDetailRepo.save(productAggregateDetail);
		log.info("saveProductAggregateDetails :: productAggregateDetail " + productAggregateDetail);

		return true;

	}

	@Transactional
	public Order cancelOrder(Order order) {
		log.info("cancelOrder");
		order.setOrderStatus(OrderStatus.CANCELLED_BY_STORE);

		orderRepo.save(order);
		log.info("cancelOrder :: order " + order);

		return order;
	}

	@Transactional
	public GraphCountModel<Date> createProductsCountGraphModelDateWise(
			List<ProductCountGraphModel> productCountGraphModels, Date startDate, Date endDate, int filter) {
		log.info("createProductsCountGraphModelDateWise");
		List<Long> counts = new ArrayList<>();
		List<Date> intervals = new ArrayList<>();
		Long maxValue = 10L;

		log.info("createProductsCountGraphModelDateWise :: currentDate  " + DateUtil.getCurrentDate());

		DateTime endDateTime = new DateTime(endDate);
		DateTime startDateTime = new DateTime(startDate);
		startDateTime = startDateTime.hourOfDay().setCopy(endDateTime.hourOfDay().get());
		startDateTime = startDateTime.minuteOfHour().setCopy(endDateTime.minuteOfHour().get());
		startDateTime = startDateTime.secondOfMinute().setCopy(endDateTime.secondOfMinute().get());

		while (startDateTime.isBefore(startDate.getTime())) {
			startDateTime = startDateTime.dayOfMonth().addToCopy(1);
		}

		log.info("createProductsCountGraphModelDateWise :: startDateTime  " + startDateTime);
		log.info("createProductsCountGraphModelDateWise :: endDateTime  " + endDateTime);

		// Here generating the dates between startDate and endDate
		while (DateTimeComparator.getDateOnlyInstance().compare(startDateTime, endDateTime) <= 0) {
			final Date date = startDateTime.toDate();

			Optional<ProductCountGraphModel> productCountGraphModel = productCountGraphModels.stream()
					.filter(model -> DateUtil.compareDateOnly(date, model.getDate())).findFirst();

			intervals.add(date);
			counts.add(productCountGraphModel.isPresent() ? productCountGraphModel.get().getCount() : 0L);

			startDateTime = startDateTime.dayOfMonth().addToCopy(1);
		}

		log.info("createProductsCountGraphModelDateWise :: counts  " + counts);
		log.info("createProductsCountGraphModelDateWise :: intervals  " + intervals);

		if (!counts.isEmpty())
			maxValue = appUtil.getRoundOfMaximumCount(Collections.max(counts));
		log.info("createProductsCountGraphModelDateWise :: maxValue " + maxValue);

		return new GraphCountModel<Date>(counts, intervals, maxValue);
	}

	@Transactional
	public GraphCountModel<Integer> createProductsCountGraphModelWeekly(
			List<ProductCountGraphModelForWeekOrMonthOrYear> productCountGraphModelForWeekOrMonthOrYears,
			Date startDate, Date endDate, int filter) {
		log.info("createProductsCountGraphModelWeekly");
		List<Long> counts = new ArrayList<>();
		List<Integer> intervals = new ArrayList<>();
		Long maxValue = 10L;

		DateTime startDateTime = new DateTime(startDate);
		int startWeek = startDateTime.getWeekOfWeekyear();

		DateTime endDateTime = new DateTime(endDate);
		int endWeek = endDateTime.getWeekOfWeekyear();

		// Here generating the weeks between startWeek and endWeek
		while (startWeek <= endWeek) {
			final Integer week = startWeek;

			Optional<ProductCountGraphModelForWeekOrMonthOrYear> productCountGraphModelForWeekOrMonthOrYear = productCountGraphModelForWeekOrMonthOrYears
					.stream().filter(model -> model.getTime().equals(week)).findFirst();

			intervals.add(week);
			counts.add(productCountGraphModelForWeekOrMonthOrYear.isPresent()
					? productCountGraphModelForWeekOrMonthOrYear.get().getCount()
					: 0L);

			startWeek++;
		}

		log.info("createProductsCountGraphModelWeekly :: counts  " + counts);
		log.info("createProductsCountGraphModelWeekly :: intervals  " + intervals);

		if (!counts.isEmpty())
			maxValue = appUtil.getRoundOfMaximumCount(Collections.max(counts));
		log.info("createProductsCountGraphModelWeekly :: maxValue " + maxValue);

		return new GraphCountModel<Integer>(counts, intervals, maxValue);
	}

	@Transactional
	public GraphCountModel<Integer> createProductsCountGraphModelMonthly(
			List<ProductCountGraphModelForWeekOrMonthOrYear> productCountGraphModelForWeekOrMonthOrYears,
			Date startDate, Date endDate, int filter) {
		log.info("createProductsCountGraphModelMonthly");
		List<Long> count = new ArrayList<>();
		List<Integer> intervals = new ArrayList<>();
		Long maxValue = 10L;

		DateTime startDateTime = new DateTime(startDate);
		int startMonth = startDateTime.getMonthOfYear();

		DateTime endDateTime = new DateTime(endDate);
		int endMonth = endDateTime.getMonthOfYear();

		// Here generating the months between startMonth and endMonth
		while (startMonth <= endMonth) {
			final Integer month = startMonth;

			Optional<ProductCountGraphModelForWeekOrMonthOrYear> productCountGraphModelForWeekOrMonthOrYear = productCountGraphModelForWeekOrMonthOrYears
					.stream().filter(model -> model.getTime().equals(month)).findFirst();

			intervals.add(month);
			count.add(productCountGraphModelForWeekOrMonthOrYear.isPresent()
					? productCountGraphModelForWeekOrMonthOrYear.get().getCount()
					: 0L);

			startMonth++;
		}

		log.info("createProductsCountGraphModelMonthly :: counts  " + count);
		log.info("createProductsCountGraphModelMonthly :: intervals  " + intervals);

		if (!count.isEmpty())
			maxValue = appUtil.getRoundOfMaximumCount(Collections.max(count));
		log.info("createProductsCountGraphModelMonthly :: maxValue " + maxValue);

		return new GraphCountModel<Integer>(count, intervals, maxValue);
	}

	@Transactional
	public GraphCountModel<Integer> createProductsCountGraphModelYearly(
			List<ProductCountGraphModelForWeekOrMonthOrYear> productCountGraphModelForWeekOrMonthOrYears,
			Date startDate, Date endDate, int filter) {
		log.info("createProductsCountGraphModelYearly");
		List<Long> count = new ArrayList<>();
		List<Integer> intervals = new ArrayList<>();
		Long maxValue = 10L;

		DateTime startDateTime = new DateTime(startDate);
		int startYear = startDateTime.getYear();

		DateTime endDateTime = new DateTime(endDate);
		int endYear = endDateTime.getYear();

		// Here generating the years between startYear and endYear
		while (startYear <= endYear) {
			final Integer year = startYear;

			Optional<ProductCountGraphModelForWeekOrMonthOrYear> productCountGraphModelForWeekOrMonthOrYear = productCountGraphModelForWeekOrMonthOrYears
					.stream().filter(model -> model.getTime().equals(year)).findFirst();

			intervals.add(year);
			count.add(productCountGraphModelForWeekOrMonthOrYear.isPresent()
					? productCountGraphModelForWeekOrMonthOrYear.get().getCount()
					: 0L);

			startYear++;
		}

		log.info("createProductsCountGraphModelYearly :: counts  " + count);
		log.info("createProductsCountGraphModelYearly :: intervals  " + intervals);

		if (!count.isEmpty())
			maxValue = appUtil.getRoundOfMaximumCount(Collections.max(count));
		log.info("createProductsCountGraphModelYearly :: maxValue " + maxValue);

		return new GraphCountModel<Integer>(count, intervals, maxValue);
	}

	@Transactional
	public boolean rateCustomer(RateUserForm rateUserForm, Order order, UserEntity userEntity) {
		log.info("rateCustomer");
		if (AppConst.FLOAT_ZERO == rateUserForm.getRating())
			throw new AppException(StringConst.RATING_INVALID, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		userBuilder.rateUser(rateUserForm, order, order.getUser(), userEntity);

		order.getOrderRatingDetail().setCustomerPendingByDispatcher(false);
		order.getOrderRatingDetail().setCustomerRatingByDispatcher(rateUserForm.getRating());
		orderRatingDetailRepo.save(order.getOrderRatingDetail());
		log.info("rateCustomer :: order.getOrderRatingDetail() " + order.getOrderRatingDetail());

		return true;
	}

	@Transactional
	public DriverInventory saveOrUpdateDriverInventory(DriverInventoryForm driverInventoryForm,
			UserDriverDetail userDriverDetail, Product product) {
		log.info("saveOrUpdateDriverInventory");
		DriverInventory driverInventory = driverInventoryRepo
				.findByDriverIdAndProductId(driverInventoryForm.getDriverId(), driverInventoryForm.getProductId());
		log.info("saveOrUpdateDriverInventory :: driverInventory " + driverInventory);

		if (null == driverInventory) {
			driverInventory = new DriverInventory();
			driverInventory.setUserDriverDetail(userDriverDetail);
			driverInventory.setProduct(product);
		}

		driverInventoryRepo.save(driverInventory);
		log.info("saveOrUpdateDriverInventory :: driverInventory " + driverInventory);

		driverInventory = saveOrUpdateDriverInventoryDetails(driverInventoryForm, product, driverInventory);

		return driverInventory;
	}

	@Transactional
	public DriverInventory saveOrUpdateDriverInventoryDetails(DriverInventoryForm driverInventoryForm, Product product,
			DriverInventory driverInventory) {
		log.info("saveOrUpdateDriverInventoryDetails :: driverInventory.getDriverInventoryDetails() "
				+ driverInventory.getDriverInventoryDetails());
		List<DriverInventoryDetail> driverInventoryDetails = null != driverInventory.getDriverInventoryDetails()
				? driverInventory.getDriverInventoryDetails()
				: new ArrayList<>();

		driverInventoryDetails.stream().forEach(driverInventoryDetail -> {
			driverInventoryDetail.setQuantity(AppConst.NUMBER.ZERO);
		});

		driverInventoryDetailRepo.save(driverInventoryDetails);
		log.info("saveOrUpdateDriverInventoryDetails :: driverInventoryDetails " + driverInventoryDetails);

		driverInventoryDetails.clear();
		driverInventory.setInStock(false);

		product.getProductPriceDetails().stream().forEach(priceDetail -> {
			DriverInventoryDetail driverInventoryDetail = driverInventoryDetailRepo
					.findByDriverInventoryIdAndProductPriceDetailId(driverInventory.getId(), priceDetail.getId());

			if (null == driverInventoryDetail) {
				driverInventoryDetail = new DriverInventoryDetail();
				driverInventoryDetail.setDriverInventory(driverInventory);
				driverInventoryDetail.setProductPriceDetail(priceDetail);
			}

			Optional<DriverInventoryDetailForm> driverInventoryDetailForm = driverInventoryForm
					.getDriverInventoryDetails().stream()
					.filter(detailForm -> priceDetail.getPriceType() == detailForm.getPriceType()).findAny();

			if (driverInventoryDetailForm.isPresent()) {
				driverInventoryDetail.setQuantity(driverInventoryDetailForm.get().getQuantity());

				if (driverInventoryDetail.getQuantity() > AppConst.NUMBER.ZERO)
					driverInventory.setInStock(true);

				driverInventoryDetails.add(driverInventoryDetail);
			}
		});

		driverInventoryDetailRepo.save(driverInventoryDetails);
		log.info("saveOrUpdateDriverInventoryDetails :: driverInventoryDetails " + driverInventoryDetails);

		driverInventory.setDriverInventoryDetails(driverInventoryDetails);

		driverInventoryRepo.save(driverInventory);
		log.info("saveOrUpdateDriverInventoryDetails :: driverInventory " + driverInventory);

		return driverInventory;
	}

	@Transactional
	public DriverInventoryModel createDriverInventoryModel(DriverInventory driverInventory) {
		log.info("createDriverInventoryModel");
		List<DriverInventoryDetailModel> driverInventoryDetailModels = new ArrayList<>();

		if (null != driverInventory.getDriverInventoryDetails()
				&& !driverInventory.getDriverInventoryDetails().isEmpty()) {
			driverInventoryDetailModels = driverInventory.getDriverInventoryDetails().stream()
					.filter(driverInventoryDetail -> driverInventoryDetail.getQuantity() > AppConst.NUMBER.ZERO)
					.map(driverInventoryDetail -> createDriverInventoryDetailModel(driverInventoryDetail))
					.collect(Collectors.toList());
		}

		ProductModel productModel = createProductModel(driverInventory.getProduct());

		return new DriverInventoryModel(driverInventory.getUserDriverDetail().getId(), productModel,
				driverInventory.isInStock(), driverInventoryDetailModels);
	}

	@Transactional
	public DriverInventoryModel createDriverInventoryModelFromProduct(Product product, int driverId) {
		log.info("createDriverInventoryModelFromProduct");
		DriverInventory driverInventory = driverInventoryRepo.findByDriverIdAndProductId(driverId, product.getId());

		List<DriverInventoryDetailModel> driverInventoryDetailModels = new ArrayList<>();

		if (null != driverInventory && null != driverInventory.getDriverInventoryDetails()
				&& !driverInventory.getDriverInventoryDetails().isEmpty()) {
			driverInventoryDetailModels = driverInventory.getDriverInventoryDetails().stream()
					.filter(driverInventoryDetail -> !driverInventoryDetail.isDeleted()
							&& driverInventoryDetail.getQuantity() > AppConst.NUMBER.ZERO
							&& driverInventoryDetail.getProductPriceDetail().isAvailable())
					.map(driverInventoryDetail -> createDriverInventoryDetailModel(driverInventoryDetail))
					.collect(Collectors.toList());
		}

		ProductModel productModel = createProductModel(product);
		Boolean isInStock = (null == driverInventory || driverInventoryDetailModels.isEmpty()) ? false
				: driverInventory.isInStock();

		return new DriverInventoryModel(driverId, productModel, isInStock, driverInventoryDetailModels);

	}

	@Transactional
	public DriverInventoryDetailModel createDriverInventoryDetailModel(DriverInventoryDetail driverInventoryDetail) {
		log.info("createDriverInventoryDetailModel");
		ProductPriceDetailModel productPriceDetailModel = createProductPriceDetailModel(
				driverInventoryDetail.getProductPriceDetail());

		return new DriverInventoryDetailModel(driverInventoryDetail.getDriverInventory().getId(),
				productPriceDetailModel, productPriceDetailModel.getPriceType(), driverInventoryDetail.getQuantity());
	}

}
