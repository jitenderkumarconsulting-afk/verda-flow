package com.org.verdaflow.rest.api.customer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.org.verdaflow.rest.api.admin.model.UserDetailModel;
import com.org.verdaflow.rest.api.auth.AuthBuilder;
import com.org.verdaflow.rest.api.auth.model.UserCustomerDetailModel;
import com.org.verdaflow.rest.api.auth.model.UserDispatcherDetailModel;
import com.org.verdaflow.rest.api.customer.form.CustomerAddressDetailForm;
import com.org.verdaflow.rest.api.customer.form.OrderPriceDetailForm;
import com.org.verdaflow.rest.api.customer.form.PlaceOrderForm;
import com.org.verdaflow.rest.api.customer.form.UpdateUserCustomerDetailForm;
import com.org.verdaflow.rest.api.customer.model.ApplyPromoCodeModel;
import com.org.verdaflow.rest.api.customer.model.CartDetailModel;
import com.org.verdaflow.rest.api.customer.model.CartModel;
import com.org.verdaflow.rest.api.customer.model.CustomerAddressDetailModel;
import com.org.verdaflow.rest.api.dispatcher.DispatcherBuilder;
import com.org.verdaflow.rest.api.dispatcher.model.ProductEffectMappingModel;
import com.org.verdaflow.rest.api.dispatcher.model.ProductModel;
import com.org.verdaflow.rest.api.dispatcher.model.ProductPriceDetailModel;
import com.org.verdaflow.rest.api.dispatcher.model.PromoCodeModel;
import com.org.verdaflow.rest.api.driver.DriverBuilder;
import com.org.verdaflow.rest.api.master.MasterBuilder;
import com.org.verdaflow.rest.api.master.model.MasterModel;
import com.org.verdaflow.rest.api.user.UserBuilder;
import com.org.verdaflow.rest.api.user.form.RateUserForm;
import com.org.verdaflow.rest.api.user.model.AuditOrderStatusModel;
import com.org.verdaflow.rest.api.user.model.AuditRatingModel;
import com.org.verdaflow.rest.api.user.model.OrderItemDetailModel;
import com.org.verdaflow.rest.api.user.model.OrderModel;
import com.org.verdaflow.rest.api.user.model.OrderPriceDetailModel;
import com.org.verdaflow.rest.api.user.model.OrderRatingDetailModel;
import com.org.verdaflow.rest.api.user.model.UserRatingDetailModel;
import com.org.verdaflow.rest.common.enums.DeliveryType;
import com.org.verdaflow.rest.common.enums.OrderStatus;
import com.org.verdaflow.rest.common.enums.ProductAggregateType;
import com.org.verdaflow.rest.common.model.CountModel;
import com.org.verdaflow.rest.common.model.UserRoleModel;
import com.org.verdaflow.rest.config.common.AppConst;
import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.config.security.jwt.JwtUser;
import com.org.verdaflow.rest.entity.AuditOrderStatus;
import com.org.verdaflow.rest.entity.AuditPromoCode;
import com.org.verdaflow.rest.entity.AuditRating;
import com.org.verdaflow.rest.entity.CartDetail;
import com.org.verdaflow.rest.entity.CustomerAddressDetail;
import com.org.verdaflow.rest.entity.DriverOrderMapping;
import com.org.verdaflow.rest.entity.FavoriteDispatcherMapping;
import com.org.verdaflow.rest.entity.FavoriteOrderMapping;
import com.org.verdaflow.rest.entity.FavoriteProductMapping;
import com.org.verdaflow.rest.entity.Order;
import com.org.verdaflow.rest.entity.OrderItemDetail;
import com.org.verdaflow.rest.entity.OrderPriceDetail;
import com.org.verdaflow.rest.entity.OrderRatingDetail;
import com.org.verdaflow.rest.entity.Product;
import com.org.verdaflow.rest.entity.ProductAggregateDetail;
import com.org.verdaflow.rest.entity.ProductEffectMapping;
import com.org.verdaflow.rest.entity.ProductPriceDetail;
import com.org.verdaflow.rest.entity.PromoCode;
import com.org.verdaflow.rest.entity.UserCustomerDetail;
import com.org.verdaflow.rest.entity.UserDispatcherDetail;
import com.org.verdaflow.rest.entity.UserEntity;
import com.org.verdaflow.rest.error.AppException;
import com.org.verdaflow.rest.repo.AuditOrderStatusRepo;
import com.org.verdaflow.rest.repo.AuditPromoCodeRepo;
import com.org.verdaflow.rest.repo.CartDetailRepo;
import com.org.verdaflow.rest.repo.CustomerAddressDetailRepo;
import com.org.verdaflow.rest.repo.DriverOrderMappingRepo;
import com.org.verdaflow.rest.repo.FavoriteDispatcherMappingRepo;
import com.org.verdaflow.rest.repo.FavoriteOrderMappingRepo;
import com.org.verdaflow.rest.repo.FavoriteProductMappingRepo;
import com.org.verdaflow.rest.repo.OrderItemDetailRepo;
import com.org.verdaflow.rest.repo.OrderPriceDetailRepo;
import com.org.verdaflow.rest.repo.OrderRatingDetailRepo;
import com.org.verdaflow.rest.repo.OrderRepo;
import com.org.verdaflow.rest.repo.ProductAggregateDetailRepo;
import com.org.verdaflow.rest.repo.ProductAggregateRepo;
import com.org.verdaflow.rest.repo.ProductPriceDetailRepo;
import com.org.verdaflow.rest.repo.ProductRepo;
import com.org.verdaflow.rest.repo.UserCustomerDetailRepo;
import com.org.verdaflow.rest.util.AWSImageUpload;
import com.org.verdaflow.rest.util.AppUtil;
import com.org.verdaflow.rest.util.DateUtil;

@Component
public class CustomerBuilder {
	public static final Logger log = LoggerFactory.getLogger(CustomerBuilder.class);

	@Autowired
	private UserCustomerDetailRepo userCustomerDetailRepo;

	@Autowired
	private AppUtil appUtil;

	@Autowired
	private AWSImageUpload awsImageUpload;

	@Autowired
	private MasterBuilder masterBuilder;

	@Autowired
	private DispatcherBuilder dispatcherBuilder;

	@Autowired
	private CustomerAddressDetailRepo customerAddressDetailRepo;

	@Autowired
	private FavoriteDispatcherMappingRepo favoriteDispatcherMappingRepo;

	@Autowired
	private FavoriteProductMappingRepo favoriteProductMappingRepo;

	@Autowired
	private CartDetailRepo cartDetailRepo;

	@Autowired
	private Environment env;

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private ProductRepo productRepo;

	@Autowired
	private OrderItemDetailRepo orderItemDetailRepo;

	@Autowired
	private OrderPriceDetailRepo orderPriceDetailRepo;

	@Autowired
	private DriverOrderMappingRepo driverOrderMappingRepo;

	@Autowired
	private AuthBuilder authBuilder;

	@Autowired
	private AuditPromoCodeRepo auditPromoCodeRepo;

	@Autowired
	private FavoriteOrderMappingRepo favoriteOrderMappingRepo;

	@Autowired
	private ProductPriceDetailRepo productPriceDetailRepo;

	@Autowired
	private DriverBuilder driverBuilder;

	@Autowired
	private ProductAggregateRepo productAggregateRepo;

	@Autowired
	private ProductAggregateDetailRepo productAggregateDetailRepo;

	@Autowired
	private AuditOrderStatusRepo auditOrderStatusRepo;

	@Autowired
	private OrderRatingDetailRepo orderRatingDetailRepo;

	@Autowired
	private UserBuilder userBuilder;

	@Transactional
	public UserDetailModel createCustomerUserDetailModel(UserEntity userEntity) {
		log.info("createCustomerUserDetailModel");
		UserCustomerDetailModel userCustomerDetailModel = createUserCustomerDetailModel(
				userEntity.getUserCustomerDetail());

		return new UserDetailModel(authBuilder.createUserModel(userEntity),
				userEntity.getUserRoleMappings().stream()
						.map(mapper -> new UserRoleModel(mapper.getMasterRole().getId(),
								String.valueOf(mapper.getMasterRole().getRole()), mapper.getApplicationStatus()))
						.collect(Collectors.toList()),
				userCustomerDetailModel);
	}

	@Transactional
	public UserCustomerDetail updateUserCustomerDetail(UpdateUserCustomerDetailForm updateUserCustomerDetailForm,
			UserCustomerDetail userCustomerDetail) {
		log.info("updateUserCustomerDetail");
		userCustomerDetail.setFirstName(updateUserCustomerDetailForm.getFirstName());
		userCustomerDetail.setLastName(updateUserCustomerDetailForm.getLastName());

		// save user customer images
		userCustomerDetail = updateUserCustomerImages(updateUserCustomerDetailForm, userCustomerDetail);

		// Here saving the updated detail for UserCustomerDetail.
		userCustomerDetailRepo.save(userCustomerDetail);

		log.info("updateUserCustomerDetail :: userCustomerDetail " + userCustomerDetail);

		return userCustomerDetail;
	}

	@Transactional
	public UserCustomerDetail updateUserCustomerImages(UpdateUserCustomerDetailForm updateUserCustomerDetailForm,
			UserCustomerDetail userCustomerDetail) {
		log.info("updateUserCustomerImages");
		if (null != updateUserCustomerDetailForm.getImage()
				&& StringUtils.isNotBlank(updateUserCustomerDetailForm.getImage())
				&& null != updateUserCustomerDetailForm.getImageThumb()
				&& StringUtils.isNotBlank(updateUserCustomerDetailForm.getImageThumb())) {
			if (appUtil.validateBase64Image(updateUserCustomerDetailForm.getImage())
					&& appUtil.validateBase64Image(updateUserCustomerDetailForm.getImageThumb())) {
				String imageUrl = awsImageUpload.uploadFile(updateUserCustomerDetailForm.getImage(),
						userCustomerDetail.getId(), AppConst.UPLOAD_TYPE.CUSTOMER, AppConst.IMAGE_TYPE.ORIGINAL);
				log.info("updateUserCustomerImages :: imageUrl " + imageUrl);

				String imageThumbUrl = awsImageUpload.uploadFile(updateUserCustomerDetailForm.getImageThumb(),
						userCustomerDetail.getId(), AppConst.UPLOAD_TYPE.CUSTOMER, AppConst.IMAGE_TYPE.THUMB);
				log.info("updateUserCustomerImages :: imageThumbUrl " + imageThumbUrl);

				userCustomerDetail.setImage(imageUrl);
				userCustomerDetail.setImageThumb(imageThumbUrl);
			}
		} else {
			userCustomerDetail.setImage(null);
			userCustomerDetail.setImageThumb(null);
		}

		return userCustomerDetail;
	}

	@Transactional
	public UserDispatcherDetailModel createUserDispatcherDetailModel(UserDispatcherDetail userDispatcherDetail,
			boolean isFav) {
		log.info("createUserDispatcherDetailModel");
		String imageUrl = null;
		if (null != userDispatcherDetail.getImage() && StringUtils.isNotBlank(userDispatcherDetail.getImage()))
			imageUrl = AppUtil.AWS_S3_BASE_URL_AND_BUCKET + userDispatcherDetail.getImage();
		log.info("createUserDispatcherDetailModel :: imageUrl " + imageUrl);

		String imageThumbUrl = null;
		if (null != userDispatcherDetail.getImageThumb()
				&& StringUtils.isNotBlank(userDispatcherDetail.getImageThumb()))
			imageThumbUrl = AppUtil.AWS_S3_BASE_URL_AND_BUCKET + userDispatcherDetail.getImageThumb();
		log.info("createUserDispatcherDetailModel :: imageThumbUrl " + imageThumbUrl);

		MasterModel etaModel = null;
		if (null != userDispatcherDetail.getEta())
			etaModel = masterBuilder.createMasterModel(userDispatcherDetail.getEta());
		log.info("createUserDispatcherDetailModel :: etaModel " + etaModel);

		UserRatingDetailModel userRatingDetail = null;
		if (null != userDispatcherDetail.getUser().getUserRatingDetail())
			userRatingDetail = userBuilder
					.createUserRatingDetailModel(userDispatcherDetail.getUser().getUserRatingDetail());
		log.info("createUserDispatcherDetailModel :: userRatingDetail " + userRatingDetail);

		return new UserDispatcherDetailModel(userDispatcherDetail.getId(), userDispatcherDetail.getStoreName(),
				userDispatcherDetail.getManagerName(), userDispatcherDetail.getAddress(), userDispatcherDetail.getLat(),
				userDispatcherDetail.getLng(), imageUrl, imageThumbUrl, etaModel,
				userDispatcherDetail.getApplicationStatus(), userDispatcherDetail.isActive(), isFav,
				userDispatcherDetail.getDistance(), userDispatcherDetail.getDeliveryCharges(), userRatingDetail);
	}

	@Transactional
	public List<UserDispatcherDetailModel> createUserDispatcherDetailModelsList(
			Page<UserDispatcherDetail> userDispatcherDetails, UserEntity userEntity) {
		log.info("createUserDispatcherDetailModelsList");
		List<UserDispatcherDetailModel> userDispatcherDetailModels = new ArrayList<>();

		if (!userDispatcherDetails.getContent().isEmpty()) {
			log.info("createUserDispatcherDetailModelsList :: userDispatcherDetails " + userDispatcherDetails);

			for (UserDispatcherDetail userDispatcherDetail : userDispatcherDetails) {
				if (!userDispatcherDetail.isDeleted()) {
					boolean isFav = false;
					FavoriteDispatcherMapping favoriteDispatcherMapping = favoriteDispatcherMappingRepo
							.findByUserIdAndDispatcherId(userEntity.getId(), userDispatcherDetail.getId());

					if (null != favoriteDispatcherMapping) {
						log.info("createUserDispatcherDetailModelsList :: favoriteDispatcherMapping "
								+ favoriteDispatcherMapping);
						isFav = favoriteDispatcherMapping.isFav();
					}

					userDispatcherDetailModels.add(createUserDispatcherDetailModel(userDispatcherDetail, isFav));
				}
			}
		}

		return userDispatcherDetailModels;
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
		if (null != product.getPromoCode()) {
			promoCodeModel = createPromoCodeModel(product.getPromoCode());
		}

		List<ProductEffectMappingModel> productEffectMappingModels = new ArrayList<>();
		if (null != product.getProductEffectMappings() && !product.getProductEffectMappings().isEmpty())
			for (ProductEffectMapping productEffectMapping : product.getProductEffectMappings()) {
				productEffectMappingModels.add(dispatcherBuilder.createProductEffectMappingModel(productEffectMapping));
			}

		List<ProductPriceDetailModel> productPriceDetailModels = new ArrayList<>();
		if (null != product.getProductPriceDetails())
			product.getProductPriceDetails().stream().filter(productPriceDetail -> productPriceDetail.isAvailable())
					.forEach(productPriceDetail -> {
						productPriceDetailModels
								.add(dispatcherBuilder.createProductPriceDetailModel(productPriceDetail));
					});

		return new ProductModel(product.getId(), product.getName(), product.getGroupType(), product.getDescription(),
				imageUrl, imageThumbUrl, product.getThc(), product.getCbd(), product.getUserDispatcherDetail().getId(),
				categoryModel, typeModel, promoCodeModel, productEffectMappingModels, productPriceDetailModels);
	}

	@Transactional
	public ProductModel createProductModel(Product product, boolean isFav) {
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
		if (null != product.getPromoCode()) {
			promoCodeModel = createPromoCodeModel(product.getPromoCode());
		}

		List<ProductEffectMappingModel> productEffectMappingModels = new ArrayList<>();
		if (null != product.getProductEffectMappings() && !product.getProductEffectMappings().isEmpty())
			for (ProductEffectMapping productEffectMapping : product.getProductEffectMappings()) {
				productEffectMappingModels.add(dispatcherBuilder.createProductEffectMappingModel(productEffectMapping));
			}

		List<ProductPriceDetailModel> productPriceDetailModels = new ArrayList<>();
		if (null != product.getProductPriceDetails())
			product.getProductPriceDetails().stream().filter(productPriceDetail -> productPriceDetail.isAvailable())
					.forEach(productPriceDetail -> {
						productPriceDetailModels
								.add(dispatcherBuilder.createProductPriceDetailModel(productPriceDetail));
					});

		return new ProductModel(product.getId(), product.getName(), product.getGroupType(), product.getDescription(),
				imageUrl, imageThumbUrl, product.getThc(), product.getCbd(), product.getUserDispatcherDetail().getId(),
				categoryModel, typeModel, promoCodeModel, productEffectMappingModels, productPriceDetailModels, isFav);
	}

	@Transactional
	public List<ProductModel> createProductModelsList(Page<Product> products, UserEntity userEntity) {
		log.info("createProductModelsList");
		List<ProductModel> productModels = new ArrayList<>();

		if (!products.getContent().isEmpty()) {
			log.info("createProductModelsList :: products " + products);

			for (Product product : products) {
				if (!product.isDeleted()) {
					boolean isFav = false;
					FavoriteProductMapping favoriteProductMapping = favoriteProductMappingRepo
							.findByUserIdAndProductId(userEntity.getId(), product.getId());

					if (null != favoriteProductMapping) {
						log.info("createProductModelsList :: favoriteProductMapping " + favoriteProductMapping);
						isFav = favoriteProductMapping.isFav();
					}

					productModels.add(createProductModel(product, isFav));
				}
			}
		}

		return productModels;
	}

	@Transactional
	public PromoCodeModel createPromoCodeModel(PromoCode promoCode) {
		log.info("createPromoCodeModel");
		return new PromoCodeModel(promoCode.getId(), promoCode.getUserDispatcherDetail().getId(), promoCode.getName(),
				promoCode.getDiscount(), promoCode.getStartDate(), promoCode.getEndDate(),
				promoCode.getPromoCodeType().getId(), promoCode.isActive());
	}

	@Transactional
	public CustomerAddressDetail createAddress(CustomerAddressDetailForm customerAddressDetailForm,
			UserCustomerDetail userCustomerDetail, boolean isDefault) {
		log.info("createAddress");
		CustomerAddressDetail customerAddressDetail = new CustomerAddressDetail();
		customerAddressDetail.setName(customerAddressDetailForm.getName());
		customerAddressDetail.setCountryCode(customerAddressDetailForm.getCountryCode());
		customerAddressDetail.setPhoneNumber(customerAddressDetailForm.getPhoneNumber());
		customerAddressDetail.setAddress(customerAddressDetailForm.getAddress());
		customerAddressDetail.setLat(customerAddressDetailForm.getLat());
		customerAddressDetail.setLng(customerAddressDetailForm.getLng());
		customerAddressDetail.setUserCustomerDetail(userCustomerDetail);
		customerAddressDetail.setDefault(isDefault);

		// save customer address details
		customerAddressDetailRepo.save(customerAddressDetail);
		log.info("createAddress :: customerAddressDetail " + customerAddressDetail);

		return customerAddressDetail;
	}

	@Transactional
	public CustomerAddressDetail updateAddress(CustomerAddressDetailForm customerAddressDetailForm,
			CustomerAddressDetail customerAddressDetail, JwtUser jwtUser) {
		log.info("updateAddress");

		customerAddressDetail.setName(customerAddressDetailForm.getName());
		customerAddressDetail.setCountryCode(customerAddressDetailForm.getCountryCode());
		customerAddressDetail.setPhoneNumber(customerAddressDetailForm.getPhoneNumber());
		customerAddressDetail.setAddress(customerAddressDetailForm.getAddress());
		if (AppConst.DOUBLE_ZERO != customerAddressDetailForm.getLat()) {
			customerAddressDetail.setLat(customerAddressDetailForm.getLat());
		}
		if (AppConst.DOUBLE_ZERO != customerAddressDetailForm.getLng()) {
			customerAddressDetail.setLng(customerAddressDetailForm.getLng());
		}

		// save update customer address
		customerAddressDetailRepo.save(customerAddressDetail);
		log.info("updateAddress :: customerAddressDetail " + customerAddressDetail);

		return customerAddressDetail;
	}

	@Transactional
	public boolean deleteAddress(CustomerAddressDetail customerAddressDetail) {
		log.info("deleteAddress");
		customerAddressDetail.setDeleted(true);
		customerAddressDetailRepo.save(customerAddressDetail);

		log.info("deleteAddress :: customerAddressDetail " + customerAddressDetail);

		return true;
	}

	@Transactional
	public CustomerAddressDetailModel createCustomerAddressModel(CustomerAddressDetail customerAddressDetail) {
		log.info("createCustomerAddressModel");
		return new CustomerAddressDetailModel(customerAddressDetail.getId(), customerAddressDetail.getName(),
				customerAddressDetail.getCountryCode(), customerAddressDetail.getPhoneNumber(),
				customerAddressDetail.getAddress(), customerAddressDetail.getLat(), customerAddressDetail.getLng(),
				customerAddressDetail.getCreatedAt(), customerAddressDetail.isDefault());
	}

	@Transactional
	public FavoriteDispatcherMapping favoriteDispatcher(UserDispatcherDetail userDispatcherDetail, boolean unfav,
			UserEntity userEntity) {
		log.info("favoriteDispatcher");
		FavoriteDispatcherMapping favoriteDispatcherMapping = favoriteDispatcherMappingRepo
				.findByUserIdAndDispatcherId(userEntity.getId(), userDispatcherDetail.getId());

		if ((null == favoriteDispatcherMapping && unfav)
				|| (null != favoriteDispatcherMapping && !favoriteDispatcherMapping.isFav() && unfav))
			throw new AppException(StringConst.DISPATCHER_UNFAVORITE_REJECT, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
		else if (null != favoriteDispatcherMapping && favoriteDispatcherMapping.isFav() && !unfav)
			throw new AppException(StringConst.DISPATCHER_FAVORITE_REJECT, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		if (null == favoriteDispatcherMapping) {
			favoriteDispatcherMapping = new FavoriteDispatcherMapping();
			favoriteDispatcherMapping.setUser(userEntity);
			favoriteDispatcherMapping.setUserDispatcher(userDispatcherDetail);
		}

		if (unfav)
			favoriteDispatcherMapping.setFav(false);
		else
			favoriteDispatcherMapping.setFav(true);

		favoriteDispatcherMappingRepo.save(favoriteDispatcherMapping);
		log.info("favoriteDispatcher :: favoriteDispatcherMapping " + favoriteDispatcherMapping);

		return favoriteDispatcherMapping;
	}

	@Transactional
	public FavoriteProductMapping favoriteProduct(Product product, boolean unfav, UserEntity userEntity) {
		log.info("favoriteProduct");
		FavoriteProductMapping favoriteProductMapping = favoriteProductMappingRepo
				.findByUserIdAndProductId(userEntity.getId(), product.getId());

		if ((null == favoriteProductMapping && unfav)
				|| (null != favoriteProductMapping && !favoriteProductMapping.isFav() && unfav))
			throw new AppException(StringConst.PRODUCT_UNFAVORITE_REJECT, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
		else if (null != favoriteProductMapping && favoriteProductMapping.isFav() && !unfav)
			throw new AppException(StringConst.PRODUCT_FAVORITE_REJECT, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		if (null == favoriteProductMapping) {
			favoriteProductMapping = new FavoriteProductMapping();
			favoriteProductMapping.setUser(userEntity);
			favoriteProductMapping.setProduct(product);
		}

		if (unfav)
			favoriteProductMapping.setFav(false);
		else
			favoriteProductMapping.setFav(true);

		favoriteProductMappingRepo.save(favoriteProductMapping);
		log.info("favoriteProduct :: favoriteProductMapping " + favoriteProductMapping);

		return favoriteProductMapping;
	}

	@Transactional
	public boolean saveCartDetails(Product product, ProductPriceDetail productPriceDetail, UserEntity userEntity) {
		log.info("addCartDetails");
		CartDetail cartDetail = new CartDetail();
		cartDetail.setProduct(product);
		cartDetail.setProductPriceDetail(productPriceDetail);
		cartDetail.setUser(userEntity);

		// save customer cart details
		cartDetailRepo.save(cartDetail);
		log.info("saveCartDetails :: cartDetail " + cartDetail);

		return true;
	}

	@Transactional
	public CartModel createCartModel(List<CartDetailModel> cartDetailModels,
			CustomerAddressDetail customerAddressDetail, UserDispatcherDetail userDispatcherDetail) {
		log.info("createCartModel");
		CustomerAddressDetailModel customerAddressDetailModel = null;
		if (null != customerAddressDetail)
			customerAddressDetailModel = createCustomerAddressModel(customerAddressDetail);

		UserDispatcherDetailModel userDispatcherDetailModel = null;
		if (null != userDispatcherDetail)
			userDispatcherDetailModel = authBuilder.createUserDispatcherDetailModel(userDispatcherDetail);

		return new CartModel(cartDetailModels, customerAddressDetailModel,
				BigDecimal.valueOf(Double.valueOf(env.getProperty(StringConst.TAX))), userDispatcherDetailModel);
	}

	@Transactional
	public CartDetailModel createCartDetailModel(CartDetail cartDetail) {
		log.info("createCartDetailModel");
		ProductModel productModel = createProductModel(cartDetail.getProduct());

		ProductPriceDetailModel productPriceDetailModel = dispatcherBuilder
				.createProductPriceDetailModel(cartDetail.getProductPriceDetail());

		return new CartDetailModel(cartDetail.getId(), productModel, productPriceDetailModel, cartDetail.getCreatedAt(),
				cartDetail.getModifiedAt(), !cartDetail.getProduct().isDeleted(),
				cartDetail.getProductPriceDetail().isAvailable());
	}

	@Transactional
	public boolean deleteCartsDetails(List<CartDetail> cartDetails) {
		log.info("deleteCartsDetails");
		cartDetails.forEach(cartDetail -> cartDetail.setDeleted(true));
		cartDetailRepo.save(cartDetails);

		log.info("deleteCartsDetails :: cartDetails " + cartDetails);

		return true;
	}

	@Transactional
	public boolean deleteCartDetails(CartDetail cartDetail) {
		log.info("deleteCartProduct");
		cartDetail.setDeleted(true);
		cartDetailRepo.save(cartDetail);

		log.info("deleteCartDetails :: cartDetail " + cartDetail);

		return true;
	}

	@Transactional
	public CartDetail updateCartDetails(CartDetail cartDetail, ProductPriceDetail productPriceDetail) {
		log.info("updateCartDetails");

		cartDetail.setProductPriceDetail(productPriceDetail);

		// save customer cart details
		cartDetailRepo.save(cartDetail);
		log.info("updateCartDetails :: cartDetail " + cartDetail);

		return cartDetail;
	}

	@Transactional
	public CountModel createCountModel(int count) {
		log.info("createCountModel");

		return new CountModel(count);
	}

	@Transactional
	public boolean emptyCart(List<CartDetail> cartDetails) {
		log.info("emptyCartDetails");
		cartDetails.forEach(cartDetail -> cartDetail.setDeleted(true));

		cartDetailRepo.save(cartDetails);
		log.info("emptyCartDetails :: cartDetails " + cartDetails);

		return true;
	}

	@Transactional
	public CartDetailModel createTemporaryCartDetailModel(Product product, ProductPriceDetail productPriceDetail) {
		log.info("createTemporaryCartDetailModel");
		ProductModel productModel = createProductModel(product);

		ProductPriceDetailModel productPriceDetailModel = dispatcherBuilder
				.createProductPriceDetailModel(productPriceDetail);

		return new CartDetailModel(AppConst.NUMBER.ZERO, productModel, productPriceDetailModel,
				DateUtil.getCurrentDate(), DateUtil.getCurrentDate(), !product.isDeleted(),
				productPriceDetail.isAvailable());
	}

	@Transactional
	public Order saveOrder(PlaceOrderForm placeOrderForm, UserDispatcherDetail userDispatcherDetail,
			PromoCode promoCode, CustomerAddressDetail customerAddressDetail, DeliveryType deliveryType,
			UserEntity userEntity) {
		log.info("saveOrder");
		Order order = new Order();
		order.setUser(userEntity);
		order.setUserDispatcherDetail(userDispatcherDetail);
		order.setPromoCode(promoCode);
		order.setAddressDetail(customerAddressDetail);
		order.setDeliveryType(deliveryType);
		order.setEta(userDispatcherDetail.getEta());
		order.setOrderStatus(OrderStatus.PROCESSING);
		order.setCancelExpiryTime(DateUtil
				.getExtendedDate((long) Long.valueOf(env.getProperty(StringConst.ORDER_CANCEL_EXPIRY_MINUTES))));
		order.setPlaced(false);
		orderRepo.save(order);

		order = saveOrderItemDetails(placeOrderForm, order);
		log.info("saveOrder :: order " + order);

		order = saveOrderPriceDetail(placeOrderForm.getOrderPriceDetail(), order);
		log.info("saveOrder :: order " + order);

		order = saveOrderRatingDetail(order);
		log.info("saveOrder :: order " + order);

		if (null != promoCode)
			saveAuditPromoCode(promoCode, userEntity, order);

		return order;
	}

	@Transactional
	public Order saveOrderPriceDetail(OrderPriceDetailForm orderPriceDetailForm, Order order) {
		log.info("saveOrderPriceDetail");
		OrderPriceDetail orderPriceDetail = new OrderPriceDetail();
		orderPriceDetail.setOrder(order);
		orderPriceDetail.setSubTotal(orderPriceDetailForm.getSubTotal());
		orderPriceDetail.setTax(orderPriceDetailForm.getTax());
		orderPriceDetail.setDeliveryCharges(orderPriceDetailForm.getDeliveryCharges());
		orderPriceDetail.setTotal(orderPriceDetailForm.getTotal());

		orderPriceDetailRepo.save(orderPriceDetail);
		log.info("saveOrderPriceDetail :: orderPriceDetail " + orderPriceDetail);

		order.setOrderPriceDetail(orderPriceDetail);
		log.info("saveOrderPriceDetail :: order " + order);

		return order;
	}

	@Transactional
	public Order saveOrderItemDetails(PlaceOrderForm placeOrderForm, Order order) {
		log.info("saveOrderItemDetails");
		List<OrderItemDetail> orderItemDetails = new ArrayList<>();

		if (null != placeOrderForm.getOrderItemDetails()) {
			placeOrderForm.getOrderItemDetails().forEach(itemDetails -> {
				OrderItemDetail orderItemDetail = new OrderItemDetail();
				orderItemDetail.setOrder(order);

				Product product = productRepo.findByProductId(itemDetails.getProductId());
				if (null == product)
					throw new AppException(StringConst.PRODUCT_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
				log.info("saveOrderItemDetails :: product " + product);

				orderItemDetail.setProduct(product);

				ProductPriceDetail productPriceDetail = productPriceDetailRepo
						.findByProductPriceDetailIdAndProductId(itemDetails.getProductPriceDetailId(), product.getId());
				if (null == productPriceDetail || !productPriceDetail.isAvailable())
					throw new AppException(StringConst.PRODUCT_PRICE_DETAIL_NOT_FOUND,
							AppConst.EXCEPTION_CAT.NOT_FOUND_404);
				log.info("saveOrderItemDetails :: productPriceDetail " + productPriceDetail);

				orderItemDetail.setPriceType(productPriceDetail.getPriceType());
				orderItemDetail.setPrice(productPriceDetail.getPrice());
				orderItemDetail.setDiscounted(itemDetails.isDiscounted());

				if (itemDetails.isDiscounted())
					orderItemDetail.setDiscountedPrice(itemDetails.getDiscountedPrice());

				orderItemDetails.add(orderItemDetail);
			});
		}

		orderItemDetailRepo.save(orderItemDetails);
		log.info("saveOrderItemDetails :: orderItemDetails " + orderItemDetails);

		order.setOrderItemDetails(orderItemDetails);
		log.info("saveOrderItemDetails :: order " + order);

		return order;
	}

	@Transactional
	public Order saveOrderRatingDetail(Order order) {
		log.info("saveOrderRatingDetail");
		OrderRatingDetail orderRatingDetail = new OrderRatingDetail();
		orderRatingDetail.setOrder(order);
		orderRatingDetail.setDispatcherPendingByCustomer(false);
		orderRatingDetail.setDriverPendingByCustomer(false);
		orderRatingDetail.setCustomerPendingByDispatcher(false);
		orderRatingDetail.setCustomerPendingByDriver(false);

		orderRatingDetailRepo.save(orderRatingDetail);
		log.info("saveOrderRatingDetail :: orderRatingDetail " + orderRatingDetail);

		order.setOrderRatingDetail(orderRatingDetail);
		log.info("saveOrderRatingDetail :: order " + order);

		return order;
	}

	@Transactional
	public boolean saveAuditPromoCode(PromoCode promoCode, UserEntity userEntity, Order order) {
		log.info("saveAuditPromoCode");
		AuditPromoCode auditPromoCode = new AuditPromoCode();
		auditPromoCode.setPromoCode(promoCode);
		auditPromoCode.setUser(userEntity);
		auditPromoCode.setOrder(order);

		auditPromoCodeRepo.save(auditPromoCode);

		log.info("saveAuditPromoCode :: auditPromoCode " + auditPromoCode);

		return true;
	}

	@Transactional
	public OrderModel checkFavAndCreateOrderModel(Order order, UserEntity userEntity) {
		log.info("checkFavAndCreateOrderModel");
		boolean isFav = false;
		FavoriteOrderMapping favoriteOrderMapping = favoriteOrderMappingRepo.findByUserIdAndOrderId(userEntity.getId(),
				order.getId());

		if (null != favoriteOrderMapping) {
			log.info("checkFavAndCreateOrderModel :: favoriteOrderMapping " + favoriteOrderMapping);
			isFav = favoriteOrderMapping.isFav();
		}

		return createOrderModel(order, isFav, userEntity);
	}

	@Transactional
	public OrderModel createOrderModel(Order order, boolean isFav, UserEntity userEntity) {
		log.info("createOrderModel");
		UserDetailModel dispatcherUser = createDispatcherUserDetailModel(order.getUserDispatcherDetail().getUser(),
				userEntity);
		log.info("createOrderModel :: dispatcherUser " + dispatcherUser);

		UserDetailModel driverUser = null;
		DriverOrderMapping driverOrderMapping = driverOrderMappingRepo.findByOrderId(order.getId());
		if (null != driverOrderMapping && !driverOrderMapping.isRejected()
				&& OrderStatus.DRIVER_ASSIGNED != order.getOrderStatus())
			driverUser = driverBuilder.createDriverUserDetailModel(driverOrderMapping.getUserDriverDetail().getUser());
		log.info("createOrderModel :: driverUser " + driverUser);

		PromoCodeModel promoCodeModel = null;
		if (null != order.getPromoCode())
			promoCodeModel = dispatcherBuilder.createPromoCodeModel(order.getPromoCode());
		log.info("createOrderModel :: promoCodeModel " + promoCodeModel);

		CustomerAddressDetailModel customerAddressDetailModel = null;
		if (null != order.getAddressDetail()) {
			customerAddressDetailModel = createCustomerAddressModel(order.getAddressDetail());
			log.info("createOrderModel :: customerAddressDetailModel " + customerAddressDetailModel);
		}

		List<OrderItemDetailModel> orderItemDetailModels = new ArrayList<>();
		if (null != order.getOrderItemDetails() && !order.getOrderItemDetails().isEmpty()) {
			orderItemDetailModels = order.getOrderItemDetails().stream()
					.map(orderItemDetail -> createOrderItemDetailModel(orderItemDetail)).collect(Collectors.toList());
		}
		log.info("createOrderModel ::orderItemDetailModels " + orderItemDetailModels);

		OrderPriceDetailModel orderPriceDetailModel = null;
		if (null != order.getOrderPriceDetail())
			orderPriceDetailModel = createOrderPriceDetailModel(order.getOrderPriceDetail());
		log.info("createOrderModel :: orderPriceDetailModel " + orderPriceDetailModel);

		OrderRatingDetailModel orderRatingDetailModel = null;
		if (null != order.getOrderRatingDetail()) {
			orderRatingDetailModel = createOrderRatingDetailModel(order.getOrderRatingDetail());
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
							&& OrderStatus.DRIVER_ASSIGNED != auditOrderStatus.getOrderStatus()
							&& OrderStatus.REASSIGN_DRIVER != auditOrderStatus.getOrderStatus())
					.map(auditOrderStatus -> userBuilder.createAuditOrderStatusModel(auditOrderStatus))
					.collect(Collectors.toList());
			log.info("createAuditOrderStatusModel :: auditOrderStatusModels " + auditOrderStatusModels);
		}
		return new OrderModel(order.getId(), order.getDeliveryType().getId(), customerAddressDetailModel,
				promoCodeModel, orderItemDetailModels, orderPriceDetailModel, orderRatingDetailModel, etaModel,
				order.getOrderStatus().getId(), order.getCancelExpiryTime(), order.getCreatedAt(),
				order.getModifiedAt(), dispatcherUser, driverUser, isFav, auditOrderStatusModels);

	}

	@Transactional
	public OrderItemDetailModel createOrderItemDetailModel(OrderItemDetail orderItemDetail) {
		log.info("createOrderItemDetailModel");
		ProductModel productModel = createProductModel(orderItemDetail.getProduct());

		return new OrderItemDetailModel(orderItemDetail.getId(), productModel, orderItemDetail.getPriceType(),
				orderItemDetail.getPrice(), orderItemDetail.getDiscountedPrice(), orderItemDetail.isDiscounted());
	}

	@Transactional
	public OrderPriceDetailModel createOrderPriceDetailModel(OrderPriceDetail orderPriceDetail) {
		log.info("createOrderPriceDetailModel");

		return new OrderPriceDetailModel(orderPriceDetail.getId(), orderPriceDetail.getOrder().getId(),
				orderPriceDetail.getSubTotal(), orderPriceDetail.getTax(), orderPriceDetail.getDeliveryCharges(),
				orderPriceDetail.getTotal());
	}

	@Transactional
	public OrderRatingDetailModel createOrderRatingDetailModel(OrderRatingDetail orderRatingDetail) {
		log.info("createOrderRatingDetailModel");

		return new OrderRatingDetailModel(orderRatingDetail.getId(), orderRatingDetail.getOrder().getId(),
				orderRatingDetail.isDispatcherPendingByCustomer(), orderRatingDetail.isDriverPendingByCustomer(),
				orderRatingDetail.isCustomerPendingByDispatcher(), orderRatingDetail.isCustomerPendingByDriver(),
				orderRatingDetail.getDispatcherRatingByCustomer(), orderRatingDetail.getDriverRatingByCustomer(),
				orderRatingDetail.getCustomerRatingByDispatcher(), orderRatingDetail.getCustomerRatingByDriver());
	}

	@Transactional
	public AuditRatingModel createAuditRatingModel(AuditRating auditRating) {
		log.info("createAuditRatingModel");
		UserDetailModel byUserDetailModel = null;
		if (null != auditRating.getByUser())
			byUserDetailModel = userBuilder.createUserDetailModel(auditRating.getByUser());

		return new AuditRatingModel(auditRating.getId(), auditRating.getUser().getId(), byUserDetailModel,
				auditRating.getRating(), auditRating.getNote());
	}

	@Transactional
	public Order cancelOrder(Order order, UserEntity userEntity) {
		log.info("cancelOrder");

		order.setOrderStatus(OrderStatus.CANCELLED_BY_USER);
		orderRepo.save(order);
		log.info("cancelOrder :: " + order);

		return order;
	}

	@Transactional
	public UserDetailModel createDispatcherUserDetailModel(UserEntity dispatcherUserEntity, UserEntity userEntity) {
		log.info("createDispatcherUserDetailModel");
		boolean isFav = false;
		FavoriteDispatcherMapping favoriteDispatcherMapping = favoriteDispatcherMappingRepo.findByUserIdAndDispatcherId(
				userEntity.getId(), dispatcherUserEntity.getUserDispatcherDetail().getId());

		if (null != favoriteDispatcherMapping) {
			log.info("createDispatcherUserDetailModel :: favoriteDispatcherMapping " + favoriteDispatcherMapping);
			isFav = favoriteDispatcherMapping.isFav();
		}

		UserDispatcherDetailModel userDispatcherDetailModel = createUserDispatcherDetailModel(
				dispatcherUserEntity.getUserDispatcherDetail(), isFav);

		return new UserDetailModel(authBuilder.createUserModel(dispatcherUserEntity),
				dispatcherUserEntity.getUserRoleMappings().stream()
						.map(mapper -> new UserRoleModel(mapper.getMasterRole().getId(),
								String.valueOf(mapper.getMasterRole().getRole()), mapper.getApplicationStatus()))
						.collect(Collectors.toList()),
				userDispatcherDetailModel);
	}

	@Transactional
	public UserCustomerDetailModel createUserCustomerDetailModel(UserCustomerDetail userCustomerDetail) {
		log.info("createUserCustomerDetailModel");
		String imageUrl = null;
		if (null != userCustomerDetail.getImage() && StringUtils.isNotBlank(userCustomerDetail.getImage()))
			imageUrl = AppUtil.AWS_S3_BASE_URL_AND_BUCKET + userCustomerDetail.getImage();
		log.info("createUserCustomerDetailModel :: imageUrl " + imageUrl);

		String imageThumbUrl = null;
		if (null != userCustomerDetail.getImageThumb() && StringUtils.isNotBlank(userCustomerDetail.getImageThumb()))
			imageThumbUrl = AppUtil.AWS_S3_BASE_URL_AND_BUCKET + userCustomerDetail.getImageThumb();
		log.info("createUserCustomerDetailModel :: imageThumbUrl " + imageThumbUrl);

		return new UserCustomerDetailModel(userCustomerDetail.getId(), userCustomerDetail.getFirstName(),
				userCustomerDetail.getLastName(), imageUrl, imageThumbUrl, userCustomerDetail.getApplicationStatus(),
				userCustomerDetail.isActive());
	}

	@Transactional
	public ApplyPromoCodeModel createApplyPromoCodeModel(PromoCode promoCode, List<Product> products) {
		log.info("createApplyPromoCodeModel");
		List<Integer> productIds = new ArrayList<>();

		if (null != products && !products.isEmpty())
			productIds = products.stream().map(product -> product.getId()).collect(Collectors.toList());
		log.info("createApplyPromoCodeModel :: productIds " + productIds);

		PromoCodeModel promoCodeModel = createPromoCodeModel(promoCode);
		log.info("createApplyPromoCodeModel :: promoCodeModel " + promoCodeModel);

		return new ApplyPromoCodeModel(promoCodeModel, productIds);
	}

	@Transactional
	public FavoriteOrderMapping favoriteOrder(Order order, boolean unfav, UserEntity userEntity) {
		log.info("favoriteOrder");
		FavoriteOrderMapping favoriteOrderMapping = favoriteOrderMappingRepo.findByUserIdAndOrderId(userEntity.getId(),
				order.getId());

		if ((null == favoriteOrderMapping && unfav)
				|| (null != favoriteOrderMapping && !favoriteOrderMapping.isFav() && unfav))
			throw new AppException(StringConst.ORDER_UNFAVORITE_REJECT, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
		else if (null != favoriteOrderMapping && favoriteOrderMapping.isFav() && !unfav)
			throw new AppException(StringConst.ORDER_FAVORITE_REJECT, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		if (null == favoriteOrderMapping) {
			favoriteOrderMapping = new FavoriteOrderMapping();
			favoriteOrderMapping.setUser(userEntity);
			favoriteOrderMapping.setOrder(order);
		}

		if (unfav)
			favoriteOrderMapping.setFav(false);
		else
			favoriteOrderMapping.setFav(true);

		favoriteOrderMappingRepo.save(favoriteOrderMapping);
		log.info("favoriteOrder :: favoriteOrderMapping " + favoriteOrderMapping);

		return favoriteOrderMapping;
	}

	@Transactional
	public CustomerAddressDetail setAddressAsDefault(CustomerAddressDetail customerAddressDetail) {
		log.info("setAddressAsDefault");
		customerAddressDetail.setDefault(true);

		customerAddressDetailRepo.save(customerAddressDetail);
		log.info("setAddressAsDefault :: customerAddressDetail " + customerAddressDetail);

		return customerAddressDetail;
	}

	@Transactional
	public boolean setAddressesAsNonDefault(List<CustomerAddressDetail> defaultAddressDetails) {
		log.info("setAddressesAsNonDefault");
		defaultAddressDetails.stream().forEach(addressDetail -> addressDetail.setDefault(false));

		customerAddressDetailRepo.save(defaultAddressDetails);
		log.info("setAddressesAsNonDefault :: defaultAddressDetails " + defaultAddressDetails);

		return true;
	}

	@Transactional
	public boolean saveProductAggregatesForProductLikedOrDisliked(Product product, boolean unFav,
			UserEntity userEntity) {
		log.info("saveProductAggregates");
		if (unFav)
			saveProductAggregatesOnDisliked(product, userEntity);
		else
			dispatcherBuilder.saveProductAggregates(product, ProductAggregateType.LIKED, userEntity);

		return true;
	}

	@Transactional
	public boolean saveProductAggregatesOnDisliked(Product product, UserEntity userEntity) {
		log.info("saveProductAggregates");
		ProductAggregateDetail productAggregateDetail = productAggregateDetailRepo
				.findByProductIdAndUserIdAndProductAggregateType(product.getId(), userEntity.getId(),
						ProductAggregateType.LIKED);
		log.info("saveProductAggregates :: productAggregateDetail " + productAggregateDetail);

		if (null != productAggregateDetail) {
			productAggregateDetail.setDeleted(true);
			productAggregateDetailRepo.save(productAggregateDetail);

			int count = productAggregateDetail.getProductAggregate().getCount() - AppConst.NUMBER.ONE;
			productAggregateDetail.getProductAggregate().setCount(count);
			productAggregateRepo.save(productAggregateDetail.getProductAggregate());

			log.info("saveProductAggregates :: productAggregateDetail.getProductAggregate() "
					+ productAggregateDetail.getProductAggregate());
		}

		return true;
	}

	@Transactional
	public boolean saveAuditOrderStatus(Order order, UserEntity userEntity, OrderStatus orderStatus) {
		log.info("createAuditOrderStatus");
		AuditOrderStatus auditOrderStatus = createAuditOrderStatus(order, userEntity, orderStatus);
		auditOrderStatusRepo.save(auditOrderStatus);

		log.info("createAuditOrderStatus :: auditOrderStatus " + auditOrderStatus);

		return true;
	}

	@Transactional
	public AuditOrderStatus createAuditOrderStatus(Order order, UserEntity userEntity, OrderStatus orderStatus) {
		log.info("createAuditOrderStatus");
		AuditOrderStatus auditOrderStatus = new AuditOrderStatus();
		auditOrderStatus.setOrder(order);
		auditOrderStatus.setUser(userEntity);
		auditOrderStatus.setOrderStatus(orderStatus);

		return auditOrderStatus;
	}

	@Transactional
	public boolean rateDispatcher(RateUserForm rateUserForm, Order order, UserEntity userEntity) {
		log.info("rateDispatcher");
		if (AppConst.FLOAT_ZERO == rateUserForm.getRating())
			throw new AppException(StringConst.RATING_INVALID, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		userBuilder.rateUser(rateUserForm, order, order.getUserDispatcherDetail().getUser(), userEntity);

		order.getOrderRatingDetail().setDispatcherPendingByCustomer(false);
		order.getOrderRatingDetail().setDispatcherRatingByCustomer(rateUserForm.getRating());
		orderRatingDetailRepo.save(order.getOrderRatingDetail());
		log.info("rateDispatcher :: order.getOrderRatingDetail() " + order.getOrderRatingDetail());

		return true;
	}

	@Transactional
	public boolean rateDriver(RateUserForm rateUserForm, Order order, UserEntity userEntity) {
		log.info("rateDriver");
		if (AppConst.FLOAT_ZERO == rateUserForm.getRating())
			throw new AppException(StringConst.RATING_INVALID, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		userBuilder.rateUser(rateUserForm, order, order.getDriverOrderMapping().getUserDriverDetail().getUser(),
				userEntity);

		order.getOrderRatingDetail().setDriverPendingByCustomer(false);
		order.getOrderRatingDetail().setDriverRatingByCustomer(rateUserForm.getRating());
		orderRatingDetailRepo.save(order.getOrderRatingDetail());
		log.info("rateDriver :: order.getOrderRatingDetail() " + order.getOrderRatingDetail());

		return true;
	}

}
