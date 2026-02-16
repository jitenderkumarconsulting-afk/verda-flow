package com.org.verdaflow.rest.api.customer.service.impl;

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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.verdaflow.rest.api.admin.model.UserDetailModel;
import com.org.verdaflow.rest.api.auth.model.UserDispatcherDetailModel;
import com.org.verdaflow.rest.api.customer.CustomerBuilder;
import com.org.verdaflow.rest.api.customer.form.CartDetailForm;
import com.org.verdaflow.rest.api.customer.form.CustomerAddressDetailForm;
import com.org.verdaflow.rest.api.customer.form.PlaceOrderForm;
import com.org.verdaflow.rest.api.customer.form.UpdateUserCustomerDetailForm;
import com.org.verdaflow.rest.api.customer.model.ApplyPromoCodeModel;
import com.org.verdaflow.rest.api.customer.model.CartDetailModel;
import com.org.verdaflow.rest.api.customer.model.CartModel;
import com.org.verdaflow.rest.api.customer.model.CustomerAddressDetailModel;
import com.org.verdaflow.rest.api.customer.service.CustomerService;
import com.org.verdaflow.rest.api.dispatcher.model.ProductModel;
import com.org.verdaflow.rest.api.user.form.RateOrderForm;
import com.org.verdaflow.rest.api.user.model.OrderModel;
import com.org.verdaflow.rest.common.enums.ApplicationStatus;
import com.org.verdaflow.rest.common.enums.DeliveryType;
import com.org.verdaflow.rest.common.enums.OrderStatus;
import com.org.verdaflow.rest.common.model.CountModel;
//xeemu@103.36.77.34/home/xeemu/Java_Backend/verdaflow-java
import com.org.verdaflow.rest.config.common.AppConst;
import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.config.security.jwt.JwtUser;
import com.org.verdaflow.rest.dto.PaginatedResponse;
import com.org.verdaflow.rest.entity.CartDetail;
import com.org.verdaflow.rest.entity.CustomerAddressDetail;
import com.org.verdaflow.rest.entity.FavoriteDispatcherMapping;
import com.org.verdaflow.rest.entity.FavoriteOrderMapping;
import com.org.verdaflow.rest.entity.FavoriteProductMapping;
import com.org.verdaflow.rest.entity.MasterType;
import com.org.verdaflow.rest.entity.Order;
import com.org.verdaflow.rest.entity.Product;
import com.org.verdaflow.rest.entity.ProductPriceDetail;
import com.org.verdaflow.rest.entity.PromoCode;
import com.org.verdaflow.rest.entity.UserCustomerDetail;
import com.org.verdaflow.rest.entity.UserDispatcherDetail;
import com.org.verdaflow.rest.error.AppException;
import com.org.verdaflow.rest.notification.NotificationBuilder;
import com.org.verdaflow.rest.repo.CartDetailRepo;
import com.org.verdaflow.rest.repo.CustomerAddressDetailRepo;
import com.org.verdaflow.rest.repo.MasterTypeRepo;
import com.org.verdaflow.rest.repo.OrderRepo;
import com.org.verdaflow.rest.repo.ProductPriceDetailRepo;
import com.org.verdaflow.rest.repo.ProductRepo;
import com.org.verdaflow.rest.repo.PromoCodeRepo;
import com.org.verdaflow.rest.repo.UserDispatcherDetailRepo;
import com.org.verdaflow.rest.util.AppUtil;
import com.org.verdaflow.rest.util.DateUtil;

@Service
public class CustomerServiceImpl implements CustomerService {
	public static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Autowired
	private CustomerBuilder customerBuilder;

	@Autowired
	private AppUtil appUtil;

	@Autowired
	private UserDispatcherDetailRepo userDispatcherDetailRepo;

	@Autowired
	private ProductRepo productRepo;

	@Autowired
	private MasterTypeRepo masterTypeRepo;

	@Autowired
	private CustomerAddressDetailRepo customerAddressDetailRepo;

	@Autowired
	private Environment env;

	@Autowired
	private ProductPriceDetailRepo productPriceDetailRepo;

	@Autowired
	private CartDetailRepo cartDetailRepo;

	@Autowired
	private PromoCodeRepo promoCodeRepo;

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private NotificationBuilder notificationBuilder;

	@Override
	@Transactional
	public UserDetailModel getProfile(JwtUser jwtUser) {
		log.info("getProfile");
		return customerBuilder.createCustomerUserDetailModel(jwtUser.getUserEntity());
	}

	@Override
	@Transactional
	public UserDetailModel updateProfile(UpdateUserCustomerDetailForm updateUserCustomerDetailForm, JwtUser jwtUser) {
		log.info("updateProfile");
		UserCustomerDetail userCustomerDetail = customerBuilder.updateUserCustomerDetail(updateUserCustomerDetailForm,
				jwtUser.getUserEntity().getUserCustomerDetail());

		log.info("updateProfile :: userCustomerDetail " + userCustomerDetail);

		return customerBuilder.createCustomerUserDetailModel(jwtUser.getUserEntity());
	}

	@Override
	@Transactional
	public PaginatedResponse<UserDispatcherDetailModel> listDispatchers(Pageable pageable, String query, BigDecimal lat,
			BigDecimal lng, List<Integer> categoryIds, JwtUser jwtUser) {
		log.info("listDispatchers");
		Page<UserDispatcherDetail> userDispatcherDetails;

		if (null == query || StringUtils.isBlank(query)) {
			if (null == categoryIds) {
				userDispatcherDetails = userDispatcherDetailRepo.findAllActiveByApplicationStatusOrderByNearestLocation(
						pageable, ApplicationStatus.APPROVED, lat, lng);
			} else {
				userDispatcherDetails = userDispatcherDetailRepo
						.findAllActiveByApplicationStatusWithCategoryFilterOrderByNearestLocation(pageable,
								ApplicationStatus.APPROVED, lat, lng, categoryIds);
			}
		} else {
			String searchQuery = new StringBuilder(StringConst.PERCENTILE).append(appUtil.sanatizeQuery(query))
					.append(StringConst.PERCENTILE).toString();

			if (null == categoryIds) {
				userDispatcherDetails = userDispatcherDetailRepo
						.findAllActiveByApplicationStatusOrderByNearestLocationSearch(pageable,
								ApplicationStatus.APPROVED, lat, lng, searchQuery);
			} else {
				userDispatcherDetails = userDispatcherDetailRepo
						.findAllActiveByApplicationStatusWithCategoryFilterOrderByNearestLocationSearch(pageable,
								ApplicationStatus.APPROVED, lat, lng, categoryIds, searchQuery);
			}
		}
		log.info("listDispatchers :: userDispatcherDetails " + userDispatcherDetails);

		List<UserDispatcherDetailModel> userDispatcherDetailModels = customerBuilder
				.createUserDispatcherDetailModelsList(userDispatcherDetails, jwtUser.getUserEntity());
		log.info("listDispatchers :: userDispatcherDetailModels " + userDispatcherDetailModels);

		int nextPage;
		if (userDispatcherDetails.getContent().isEmpty()
				|| userDispatcherDetails.getTotalPages() == pageable.getPageNumber() + AppConst.NUMBER.ONE
				|| userDispatcherDetails.getTotalPages() == AppConst.NUMBER.ONE) {
			nextPage = -AppConst.NUMBER.ONE;
		} else {
			nextPage = (pageable.getPageNumber() + AppConst.NUMBER.ONE) + AppConst.NUMBER.ONE;
		}
		log.info("listDispatchers :: nextPage " + nextPage);

		return new PaginatedResponse<>(userDispatcherDetailModels, (int) userDispatcherDetails.getTotalPages(),
				userDispatcherDetails.getTotalElements(), nextPage);
	}

	@Override
	@Transactional
	public PaginatedResponse<ProductModel> listProducts(Pageable pageable, String query, int dispatcherId, int typeId,
			JwtUser jwtUser) {
		log.info("listProducts");
		UserDispatcherDetail userDispatcherDetail = userDispatcherDetailRepo.findByDispatcherId(dispatcherId);
		if (null == userDispatcherDetail)
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("listProducts :: userDispatcherDetail " + userDispatcherDetail);

		MasterType masterType = masterTypeRepo.findByTypeId(typeId);
		if (null == masterType)
			throw new AppException(StringConst.TYPE_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("listProducts :: masterType " + masterType);

		Page<Product> products;
		if (null == query || StringUtils.isBlank(query)) {
			products = productRepo.findAllActiveProductsByDispatcherIdAndTypeId(pageable, dispatcherId, typeId);
		} else {
			String searchQuery = new StringBuilder(StringConst.PERCENTILE).append(appUtil.sanatizeQuery(query))
					.append(StringConst.PERCENTILE).toString();
			products = productRepo.findAllActiveProductsByDispatcherIdAndTypeIdSearch(pageable, dispatcherId, typeId,
					searchQuery);
		}
		log.info("listProducts :: products " + products);

		List<ProductModel> productModels = customerBuilder.createProductModelsList(products, jwtUser.getUserEntity());

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
	public CustomerAddressDetailModel createAddress(CustomerAddressDetailForm customerAddressDetailForm,
			JwtUser jwtUser) {
		log.info("createAddress");
		if (null == jwtUser.getUserEntity().getUserCustomerDetail())
			throw new AppException(StringConst.CUSTOMER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		boolean isDefault = customerAddressDetailRepo
				.findAddressesCountByCustomerId(jwtUser.getUserEntity().getUserCustomerDetail().getId()) <= 0;
		log.info("createAddress :: isDefault " + isDefault);

		CustomerAddressDetail customerAddressDetail = customerBuilder.createAddress(customerAddressDetailForm,
				jwtUser.getUserEntity().getUserCustomerDetail(), isDefault);
		log.info("createAddress :: customerAddressDetail " + customerAddressDetail);

		return customerBuilder.createCustomerAddressModel(customerAddressDetail);
	}

	@Override
	@Transactional
	public CustomerAddressDetailModel updateAddress(CustomerAddressDetailForm customerAddressDetailForm,
			JwtUser jwtUser) {
		log.info("updateAddress");
		if (null == jwtUser.getUserEntity().getUserCustomerDetail())
			throw new AppException(StringConst.CUSTOMER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		if (AppConst.NUMBER.ZERO == customerAddressDetailForm.getAddressId())
			throw new AppException(StringConst.UPDATE_ADDRESS_ID_INVALID, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		CustomerAddressDetail customerAddressDetail = customerAddressDetailRepo.findByAddressIdAndCustomerId(
				customerAddressDetailForm.getAddressId(), jwtUser.getUserEntity().getUserCustomerDetail().getId());
		if (null == customerAddressDetail)
			throw new AppException(StringConst.ADDRESS_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("updateAddress :: customerAddressDetail " + customerAddressDetail);

		customerAddressDetail = customerBuilder.updateAddress(customerAddressDetailForm, customerAddressDetail,
				jwtUser);

		return customerBuilder.createCustomerAddressModel(customerAddressDetail);
	}

	@Override
	@Transactional
	public boolean deleteAddress(int addressId, JwtUser jwtUser) {
		log.info("deleteAddress");

		if (null == jwtUser.getUserEntity().getUserCustomerDetail())
			throw new AppException(StringConst.CUSTOMER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		CustomerAddressDetail customerAddressDetail = customerAddressDetailRepo.findByAddressIdAndCustomerId(addressId,
				jwtUser.getUserEntity().getUserCustomerDetail().getId());
		if (null == customerAddressDetail)
			throw new AppException(StringConst.ADDRESS_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("deleteAddress :: customerAddressDetail " + customerAddressDetail);

		customerBuilder.deleteAddress(customerAddressDetail);

		return true;
	}

	@Override
	@Transactional
	public PaginatedResponse<CustomerAddressDetailModel> listAddresses(Pageable pageable, String query,
			JwtUser jwtUser) {
		log.info("listAddresses");
		if (null == jwtUser.getUserEntity().getUserCustomerDetail())
			throw new AppException(StringConst.CUSTOMER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		Page<CustomerAddressDetail> customerAddressDetails;
		if (null == query || StringUtils.isBlank(query)) {
			customerAddressDetails = customerAddressDetailRepo.findAllByCustomerId(pageable,
					jwtUser.getUserEntity().getUserCustomerDetail().getId());
		} else {
			String searchQuery = new StringBuilder(StringConst.PERCENTILE).append(appUtil.sanatizeQuery(query))
					.append(StringConst.PERCENTILE).toString();
			customerAddressDetails = customerAddressDetailRepo.findAllByCustomerIdSearch(pageable,
					jwtUser.getUserEntity().getUserCustomerDetail().getId(), searchQuery);
		}

		log.info("listAddresses :: customerAddressDetails " + customerAddressDetails);

		List<CustomerAddressDetailModel> customerAddressDetailModels = new ArrayList<>();
		if (!customerAddressDetails.getContent().isEmpty()) {
			customerAddressDetailModels = customerAddressDetails.getContent().stream()
					.filter(predicate -> !predicate.isDeleted())
					.map(customerAddressDetail -> customerBuilder.createCustomerAddressModel(customerAddressDetail))
					.collect(Collectors.toList());
		}

		log.info("listAddresses :: customerAddressDetailModels " + customerAddressDetailModels);

		int nextPage;
		if (customerAddressDetails.getContent().isEmpty()
				|| customerAddressDetails.getTotalPages() == pageable.getPageNumber() + AppConst.NUMBER.ONE
				|| customerAddressDetails.getTotalPages() == AppConst.NUMBER.ONE) {
			nextPage = -AppConst.NUMBER.ONE;
		} else {
			nextPage = (pageable.getPageNumber() + AppConst.NUMBER.ONE) + AppConst.NUMBER.ONE;
		}
		log.info("listAddresses :: nextPage " + nextPage);
		return new PaginatedResponse<>(customerAddressDetailModels, (int) customerAddressDetails.getTotalPages(),
				customerAddressDetails.getTotalElements(), nextPage);
	}

	@Override
	@Transactional
	public CustomerAddressDetailModel addressDetails(int addressId, JwtUser jwtUser) {
		log.info("addressDetails");
		if (null == jwtUser.getUserEntity().getUserCustomerDetail())
			throw new AppException(StringConst.CUSTOMER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		CustomerAddressDetail customerAddressDetail = customerAddressDetailRepo.findByAddressIdAndCustomerId(addressId,
				jwtUser.getUserEntity().getUserCustomerDetail().getId());
		if (null == customerAddressDetail)
			throw new AppException(StringConst.ADDRESS_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("driverDetails :: customerAddressDetail " + customerAddressDetail);

		return customerBuilder.createCustomerAddressModel(customerAddressDetail);

	}

	@Override
	@Transactional
	public CustomerAddressDetailModel setDefaultAddress(int addressId, JwtUser jwtUser) {
		log.info("setDefaultAddress");
		if (null == jwtUser.getUserEntity().getUserCustomerDetail())
			throw new AppException(StringConst.CUSTOMER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		CustomerAddressDetail customerAddressDetail = customerAddressDetailRepo.findByAddressIdAndCustomerId(addressId,
				jwtUser.getUserEntity().getUserCustomerDetail().getId());
		if (null == customerAddressDetail)
			throw new AppException(env.getProperty(StringConst.ADDRESS_NOT_FOUND),
					AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		List<CustomerAddressDetail> defaultAddressDetails = customerAddressDetailRepo
				.findAllDefaultAddressesByCustomerId(jwtUser.getUserEntity().getUserCustomerDetail().getId());

		if (null != defaultAddressDetails && !defaultAddressDetails.isEmpty())
			customerBuilder.setAddressesAsNonDefault(defaultAddressDetails);

		customerAddressDetail = customerBuilder.setAddressAsDefault(customerAddressDetail);

		return customerBuilder.createCustomerAddressModel(customerAddressDetail);
	}

	@Override
	@Transactional
	public UserDispatcherDetailModel favoriteDispatcher(int dispatcherId, boolean unfav, JwtUser jwtUser) {
		log.info("favoriteDispatcher");
		UserDispatcherDetail userDispatcherDetail = userDispatcherDetailRepo.findByDispatcherId(dispatcherId);
		if (null == userDispatcherDetail)
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("favoriteDispatcher:: userDispatcherDetail " + userDispatcherDetail);

		FavoriteDispatcherMapping favoriteDispatcherMapping = customerBuilder.favoriteDispatcher(userDispatcherDetail,
				unfav, jwtUser.getUserEntity());
		log.info("favoriteDispatcher :: favoriteBusinessMapping " + favoriteDispatcherMapping);

		return customerBuilder.createUserDispatcherDetailModel(userDispatcherDetail, favoriteDispatcherMapping.isFav());
	}

	@Override
	@Transactional
	public PaginatedResponse<UserDispatcherDetailModel> listFavoriteDispatchers(Pageable pageable, BigDecimal lat,
			BigDecimal lng, JwtUser jwtUser) {
		log.info("listFavoriteDispatchers");
		if (null == jwtUser.getUserEntity().getUserCustomerDetail())
			throw new AppException(StringConst.CUSTOMER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		Page<UserDispatcherDetail> userDispatcherDetails = userDispatcherDetailRepo
				.findAllFavoriteDispatchersByUserIdOrderByNearestLocation(pageable, jwtUser.getUserEntity().getId(),
						lat, lng);

		List<UserDispatcherDetailModel> userDispatcherDetailModels = new ArrayList<>();

		if (!userDispatcherDetails.getContent().isEmpty()) {
			log.info("listFavoriteDispatchers :: userDispatcherDetails " + userDispatcherDetails);
			userDispatcherDetailModels = userDispatcherDetails.getContent().stream()
					.filter(predicate -> !predicate.isDeleted()).map(userBusinessDetail -> customerBuilder
							.createUserDispatcherDetailModel(userBusinessDetail, true))
					.collect(Collectors.toList());
		}
		log.info("listFavoriteDispatchers :: userDispatcherDetailModels " + userDispatcherDetailModels);

		int nextPage;
		if (userDispatcherDetails.getContent().isEmpty()
				|| userDispatcherDetails.getTotalPages() == pageable.getPageNumber() + AppConst.NUMBER.ONE
				|| userDispatcherDetails.getTotalPages() == AppConst.NUMBER.ONE) {
			nextPage = -AppConst.NUMBER.ONE;
		} else {
			nextPage = (pageable.getPageNumber() + AppConst.NUMBER.ONE) + AppConst.NUMBER.ONE;
		}
		return new PaginatedResponse<>(userDispatcherDetailModels, (int) userDispatcherDetails.getTotalPages(),
				userDispatcherDetails.getTotalElements(), nextPage);
	}

	@Override
	@Transactional
	public ProductModel favoriteProduct(int productId, boolean unfav, JwtUser jwtUser) {
		log.info("favoriteProduct");
		Product product = productRepo.findByProductId(productId);
		if (null == product)
			throw new AppException(StringConst.PRODUCT_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("favoriteProduct:: product " + product);

		FavoriteProductMapping favoriteProductMapping = customerBuilder.favoriteProduct(product, unfav,
				jwtUser.getUserEntity());
		log.info("favoriteProduct :: favoriteProductMapping " + favoriteProductMapping);

		customerBuilder.saveProductAggregatesForProductLikedOrDisliked(product, unfav, jwtUser.getUserEntity());

		return customerBuilder.createProductModel(product, favoriteProductMapping.isFav());
	}

	@Override
	@Transactional
	public PaginatedResponse<ProductModel> listFavoriteProducts(Pageable pageable, JwtUser jwtUser) {
		log.info("listFavoriteProducts");
		if (null == jwtUser.getUserEntity().getUserCustomerDetail())
			throw new AppException(StringConst.CUSTOMER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		Page<Product> products = productRepo.findAllFavoriteProductsByUserIdOrderByName(pageable,
				jwtUser.getUserEntity().getId());
		List<ProductModel> productModels = new ArrayList<>();

		if (!products.getContent().isEmpty()) {
			log.info("listFavoriteProducts :: products " + products);
			productModels = products.getContent().stream().filter(predicate -> !predicate.isDeleted())
					.map(product -> customerBuilder.createProductModel(product, true)).collect(Collectors.toList());
		}
		log.info("listFavoriteProducts :: productModels " + productModels);

		int nextPage;
		if (products.getContent().isEmpty()
				|| products.getTotalPages() == pageable.getPageNumber() + AppConst.NUMBER.ONE
				|| products.getTotalPages() == AppConst.NUMBER.ONE) {
			nextPage = -AppConst.NUMBER.ONE;
		} else {
			nextPage = (pageable.getPageNumber() + AppConst.NUMBER.ONE) + AppConst.NUMBER.ONE;
		}
		return new PaginatedResponse<>(productModels, (int) products.getTotalPages(), products.getTotalElements(),
				nextPage);
	}

	@Override
	@Transactional
	public CountModel addToCart(CartDetailForm cartDetailForm, JwtUser jwtUser) {
		log.info("addToCart");
		if (null == jwtUser.getUserEntity().getUserCustomerDetail())
			throw new AppException(StringConst.CUSTOMER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		if (AppConst.NUMBER.ZERO == cartDetailForm.getProductId())
			throw new AppException(StringConst.PRODUCT_ID_INVALID, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		Product product = productRepo.findByProductId(cartDetailForm.getProductId());
		if (null == product)
			throw new AppException(StringConst.PRODUCT_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("addToCart :: product " + product);

		ProductPriceDetail productPriceDetail = productPriceDetailRepo.findByProductPriceDetailIdAndProductId(
				cartDetailForm.getProductPriceDetailId(), cartDetailForm.getProductId());
		if (null == productPriceDetail)
			throw new AppException(StringConst.PRODUCT_PRICE_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("addToCart :: productPriceDetail " + productPriceDetail);

		List<CartDetail> cartDetails = cartDetailRepo.findCartDetailByUserIdExceptCurrentDispatcherId(
				jwtUser.getUserEntity().getId(), product.getUserDispatcherDetail().getId());

		log.info("addToCart :: cartDetails " + cartDetails);

		if (null != cartDetails && !cartDetails.isEmpty())
			if (cartDetailForm.isShouldEmptyCart()) {
				customerBuilder.deleteCartsDetails(cartDetails);
			} else {
				throw new AppException(StringConst.ADD_TO_CART_REJECT, AppConst.EXCEPTION_CAT.METHOD_NOT_ALLOWED_405);
			}

		if (cartDetailRepo.findCartsCountByUserId(jwtUser.getUserEntity().getId()) == Integer
				.parseInt(env.getProperty(StringConst.CART_COUNT_MAX).trim()))
			throw new AppException(StringConst.ADD_TO_CART_LIMIT_EXCEED, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		customerBuilder.saveCartDetails(product, productPriceDetail, jwtUser.getUserEntity());

		int count = cartDetailRepo.findCartsCountByUserId(jwtUser.getUserEntity().getId());
		log.info("addToCart :: " + count);

		return customerBuilder.createCountModel(count);
	}

	@Override
	@Transactional
	public CartModel listCarts(JwtUser jwtUser) {
		log.info("listCarts");
		if (null == jwtUser.getUserEntity().getUserCustomerDetail())
			throw new AppException(StringConst.CUSTOMER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		List<CartDetail> cartDetails = cartDetailRepo.findAllByUserIdAndProduct(jwtUser.getUserEntity().getId());
		List<CartDetailModel> cartDetailModels = new ArrayList<>();
		UserDispatcherDetail userDispatcherDetail = null;

		if (!cartDetails.isEmpty()) {
			log.info("listCarts :: cartDetails " + cartDetails);
			cartDetailModels = cartDetails.stream().filter(predicate -> !predicate.isDeleted())
					.map(cart -> customerBuilder.createCartDetailModel(cart)).collect(Collectors.toList());

			userDispatcherDetail = cartDetails.get(0).getProduct().getUserDispatcherDetail();
			log.info("listCarts :: userDispatcherDetail " + userDispatcherDetail);
		}
		log.info("listCarts :: cartDetailModels " + cartDetailModels);

		CustomerAddressDetail customerAddressDetail = customerAddressDetailRepo
				.findDefaultByCustomerId(jwtUser.getUserEntity().getUserCustomerDetail().getId());
		log.info("listCarts :: customerAddressDetail " + customerAddressDetail);

		return customerBuilder.createCartModel(cartDetailModels, customerAddressDetail, userDispatcherDetail);
	}

	@Override
	@Transactional
	public CartDetailModel updateCartDetails(CartDetailForm cartDetailForm, JwtUser jwtUser) {
		log.info("updateCartDetails");
		if (null == jwtUser.getUserEntity().getUserCustomerDetail())
			throw new AppException(StringConst.CUSTOMER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		if (AppConst.NUMBER.ZERO == cartDetailForm.getCartId())
			throw new AppException(StringConst.UPDATE_CART_ID_INVALID, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		CartDetail cartDetail = cartDetailRepo.findByCartIdAndUserId(cartDetailForm.getCartId(),
				jwtUser.getUserEntity().getId());
		if (null == cartDetail)
			throw new AppException(StringConst.CART_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("updateCartDetails :: cartDetail " + cartDetail);

		ProductPriceDetail productPriceDetail = productPriceDetailRepo.findByProductPriceDetailIdAndProductId(
				cartDetailForm.getProductPriceDetailId(), cartDetail.getProduct().getId());
		if (null == productPriceDetail || !productPriceDetail.isAvailable())
			throw new AppException(StringConst.PRODUCT_PRICE_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("updateCartDetails :: productPriceDetail " + productPriceDetail);

		cartDetail = customerBuilder.updateCartDetails(cartDetail, productPriceDetail);

		return customerBuilder.createCartDetailModel(cartDetail);
	}

	@Override
	@Transactional
	public CountModel deleteFromCart(int cartId, JwtUser jwtUser) {
		log.info("deleteFromCart");
		if (null == jwtUser.getUserEntity().getUserCustomerDetail())
			throw new AppException(StringConst.CUSTOMER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		CartDetail cartDetail = cartDetailRepo.findByCartIdAndUserId(cartId, jwtUser.getUserEntity().getId());
		if (null == cartDetail)
			throw new AppException(StringConst.CART_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("deleteFromCart :: cartDetail " + cartDetail);

		customerBuilder.deleteCartDetails(cartDetail);

		int count = cartDetailRepo.findCartsCountByUserId(jwtUser.getUserEntity().getId());
		log.info("deleteFromCart :: " + count);

		return customerBuilder.createCountModel(count);
	}

	@Override
	@Transactional
	public CartDetailModel getCartDetails(int cartId, JwtUser jwtUser) {
		log.info("getCartDetails");
		if (null == jwtUser.getUserEntity().getUserCustomerDetail())
			throw new AppException(StringConst.CUSTOMER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		CartDetail cartDetail = cartDetailRepo.findByCartIdAndUserId(cartId, jwtUser.getUserEntity().getId());
		if (null == cartDetail)
			throw new AppException(StringConst.CART_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("getCartDetails :: cartDetail " + cartDetail);

		return customerBuilder.createCartDetailModel(cartDetail);
	}

	@Override
	@Transactional
	public CountModel emptyCart(JwtUser jwtUser) {
		log.info("emptyCart");
		if (null == jwtUser.getUserEntity().getUserCustomerDetail())
			throw new AppException(StringConst.CUSTOMER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		List<CartDetail> cartDetails = cartDetailRepo.findAllByUserId(jwtUser.getUserEntity().getId());
		if (null == cartDetails || cartDetails.isEmpty())
			throw new AppException(StringConst.CART_ALREADY_EMPTY, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
		log.info("emptyCart :: cartDetails " + cartDetails);

		customerBuilder.emptyCart(cartDetails);

		int count = cartDetailRepo.findCartsCountByUserId(jwtUser.getUserEntity().getId());
		log.info("emptyCart :: " + count);

		return customerBuilder.createCountModel(count);
	}

	@Override
	@Transactional
	public CountModel cartCount(JwtUser jwtUser) {
		log.info("cartCount");
		if (null == jwtUser.getUserEntity().getUserCustomerDetail())
			throw new AppException(StringConst.CUSTOMER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		int count = cartDetailRepo.findCartsCountByUserIdAndProduct(jwtUser.getUserEntity().getId());
		log.info("cartCount :: " + count);

		return customerBuilder.createCountModel(count);
	}

	@Override
	@Transactional
	public CartModel listCartsTemporary(int productId, int productPriceDetailId, JwtUser jwtUser) {
		log.info("listCartsTemporary");
		if (null == jwtUser.getUserEntity().getUserCustomerDetail())
			throw new AppException(StringConst.CUSTOMER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		if (AppConst.NUMBER.ZERO == productId)
			throw new AppException(StringConst.PRODUCT_ID_INVALID, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		Product product = productRepo.findByProductId(productId);
		if (null == product)
			throw new AppException(StringConst.PRODUCT_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("listCartsTemporary :: product " + product);

		ProductPriceDetail productPriceDetail = productPriceDetailRepo
				.findByProductPriceDetailIdAndProductId(productPriceDetailId, productId);
		if (null == productPriceDetail)
			throw new AppException(StringConst.PRODUCT_PRICE_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("listCartsTemporary :: productPriceDetail " + productPriceDetail);

		List<CartDetailModel> cartDetailModels = new ArrayList<>();
		cartDetailModels.add(customerBuilder.createTemporaryCartDetailModel(product, productPriceDetail));
		log.info("listCartsTemporary :: cartDetailModels " + cartDetailModels);

		UserDispatcherDetail userDispatcherDetail = product.getUserDispatcherDetail();
		log.info("listCartsTemporary :: userDispatcherDetail " + userDispatcherDetail);

		CustomerAddressDetail customerAddressDetail = customerAddressDetailRepo
				.findDefaultByCustomerId(jwtUser.getUserEntity().getUserCustomerDetail().getId());
		log.info("listCartsTemporary :: customerAddressDetail " + customerAddressDetail);

		return customerBuilder.createCartModel(cartDetailModels, customerAddressDetail, userDispatcherDetail);
	}

	@Override
	@Transactional
	public ApplyPromoCodeModel applyPromoCode(int dispatcherId, String promoCodeName, List<Integer> productsIds,
			JwtUser jwtUser) {
		log.info("applyPromoCode");
		promoCodeName = promoCodeName.trim().toUpperCase();

		PromoCode promoCode = promoCodeRepo.findPromoCodeByNameAndDispatcherId(promoCodeName, dispatcherId);
		if (null == promoCode)
			throw new AppException(StringConst.PROMOCODE_INVALID, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		log.info("applyPromoCode :: currentDate" + DateUtil.getCurrentDate());
		log.info("applyPromoCode :: startDate" + promoCode.getStartDate());
		log.info("applyPromoCode :: endDate" + promoCode.getEndDate());

		if (DateUtil.isFutureDateTime(promoCode.getStartDate()))
			throw new AppException(StringConst.PROMOCODE_INVALID, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		if (DateUtil.isPastDateTime(promoCode.getEndDate()))
			throw new AppException(StringConst.PROMOCODE_EXPIRED, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		List<Product> products = productRepo.getProductsByIdsAndDispatcherIdAndPromoCodeId(productsIds, dispatcherId,
				promoCode.getId());
		log.info("applyPromoCode :: products " + products);

		if (products.isEmpty())
			throw new AppException(StringConst.NO_PRODUCT_FOUND_FOR_PROMOCODE,
					AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		return customerBuilder.createApplyPromoCodeModel(promoCode, products);
	}

	@Override
	@Transactional
	public OrderModel placeOrder(PlaceOrderForm placeOrderForm, JwtUser jwtUser) {
		log.info("placeOrder");
		UserDispatcherDetail userDispatcherDetail = userDispatcherDetailRepo
				.findByDispatcherId(placeOrderForm.getDispatcherId());
		if (null == userDispatcherDetail)
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("placeOrder :: userDispatcherDetail " + userDispatcherDetail);

		if (!userDispatcherDetail.isActive())
			throw new AppException(StringConst.PLACE_ORDER_DISPATCHER_DEACTIVATED,
					AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		if (null == placeOrderForm.getOrderItemDetails() || placeOrderForm.getOrderItemDetails().isEmpty())
			throw new AppException(StringConst.NO_PRODUCT_FOUND_IN_ORDER, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		if (null == placeOrderForm.getOrderPriceDetail())
			throw new AppException(StringConst.NO_ORDER_PRICE_DETAIL_FOUND_IN_ORDER,
					AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		DeliveryType deliveryType = DeliveryType.valueOf(placeOrderForm.getDeliveryType());
		if (null == deliveryType)
			throw new AppException(StringConst.DELIVERY_TYPE_INVALID, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		if (DeliveryType.PICKUP == deliveryType && BigDecimal.ZERO
				.compareTo(placeOrderForm.getOrderPriceDetail().getDeliveryCharges()) != AppConst.NUMBER.ZERO)
			throw new AppException(StringConst.DELIVERY_CHARGES_NOT_APPLICABLE_ON_PICKUP,
					AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		CustomerAddressDetail customerAddressDetail = null;
		if (DeliveryType.DELIVERY == deliveryType) {
			if (AppConst.NUMBER.ZERO == placeOrderForm.getCustomerAddressDetailId())
				throw new AppException(StringConst.ADDRESS_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

			customerAddressDetail = customerAddressDetailRepo.findByAddressIdAndCustomerId(
					placeOrderForm.getCustomerAddressDetailId(),
					jwtUser.getUserEntity().getUserCustomerDetail().getId());
			if (null == customerAddressDetail)
				throw new AppException(StringConst.ADDRESS_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
			log.info("placeOrder :: customerAddressDetail " + customerAddressDetail);
		}

		PromoCode promoCode = null;
		if (AppConst.NUMBER.ZERO != placeOrderForm.getPromoCodeId()) {
			promoCode = promoCodeRepo.findPromoCodeByIdAndDispatcherId(placeOrderForm.getPromoCodeId(),
					userDispatcherDetail.getId());
			if (null == promoCode)
				throw new AppException(StringConst.PROMOCODE_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
			log.info("placeOrder :: promoCode " + promoCode);
		}

		Order order = customerBuilder.saveOrder(placeOrderForm, userDispatcherDetail, promoCode, customerAddressDetail,
				deliveryType, jwtUser.getUserEntity());
		log.info("placeOrder :: order " + order);

		List<CartDetail> cartDetails = cartDetailRepo.findAllByUserId(jwtUser.getUserEntity().getId());
		log.info("placeOrder :: cartDetails " + cartDetails);

		if (placeOrderForm.isShouldEmptyCart() && null != cartDetails && !cartDetails.isEmpty())
			customerBuilder.emptyCart(cartDetails);

		customerBuilder.saveAuditOrderStatus(order, jwtUser.getUserEntity(), OrderStatus.PROCESSING);

		return customerBuilder.checkFavAndCreateOrderModel(order, jwtUser.getUserEntity());
	}

	@Override
	@Transactional
	public PaginatedResponse<OrderModel> listOrders(Pageable pageable, int filter, JwtUser jwtUser) {
		log.info("listOrders");
		Page<Order> orders = null;
		if (AppConst.NUMBER.ZERO == filter) {
			// All orders
			orders = orderRepo.findAllByUserId(pageable, jwtUser.getUserEntity().getId());
		} else if (AppConst.NUMBER.ONE == filter) {
			// All new orders
			orders = orderRepo.findAllNewOrdersForCustomer(pageable, jwtUser.getUserEntity().getId());
		} else if (AppConst.NUMBER.TWO == filter) {
			// All pending orders
			orders = orderRepo.findAllPendingOrdersForCustomer(pageable, jwtUser.getUserEntity().getId());
		} else if (AppConst.NUMBER.THREE == filter) {
			// All completed orders
			orders = orderRepo.findAllCompletedOrdersForCustomer(pageable, jwtUser.getUserEntity().getId());
		} else {
			throw new AppException(StringConst.INVALID_FILTER, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
		}
		log.info("listOrders :: orders " + orders);
		List<OrderModel> orderModels = new ArrayList<>();

		if (!orders.getContent().isEmpty()) {
			log.info("listOrders :: orders " + orders);
			orderModels = orders.getContent().stream().filter(predicate -> !predicate.isDeleted())
					.map(order -> customerBuilder.checkFavAndCreateOrderModel(order, jwtUser.getUserEntity()))
					.collect(Collectors.toList());
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
	public OrderModel favoriteOrder(int orderId, boolean unfav, JwtUser jwtUser) {
		log.info("favoriteOrder");
		Order order = orderRepo.findByOrderId(orderId);
		if (null == order)
			throw new AppException(StringConst.ORDER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("favoriteOrder:: order " + order);

		FavoriteOrderMapping favoriteOrderMapping = customerBuilder.favoriteOrder(order, unfav,
				jwtUser.getUserEntity());
		log.info("favoriteOrder :: favoriteOrderMapping " + favoriteOrderMapping);

		return customerBuilder.createOrderModel(order, favoriteOrderMapping.isFav(), jwtUser.getUserEntity());
	}

	@Override
	@Transactional
	public PaginatedResponse<OrderModel> listFavoriteOrders(Pageable pageable, JwtUser jwtUser) {
		log.info("listFavoriteOrders");
		if (null == jwtUser.getUserEntity().getUserCustomerDetail())
			throw new AppException(StringConst.CUSTOMER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		Page<Order> orders = orderRepo.findAllFavoriteOrdersByUserId(pageable, jwtUser.getUserEntity().getId());
		List<OrderModel> orderModels = new ArrayList<>();

		if (!orders.getContent().isEmpty()) {
			log.info("listFavoriteOrders :: orders " + orders);
			orderModels = orders.getContent().stream().filter(predicate -> !predicate.isDeleted())
					.map(order -> customerBuilder.createOrderModel(order, true, jwtUser.getUserEntity()))
					.collect(Collectors.toList());
		}
		log.info("listFavoriteOrders :: orderModels " + orderModels);

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
	public OrderModel cancelOrder(int orderId, JwtUser jwtUser) {
		log.info("cancelOrder");
		Order order = orderRepo.findByOrderIdAndUserId(orderId, jwtUser.getUserEntity().getId());
		if (order == null)
			throw new AppException(StringConst.ORDER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("cancelOrder :: order " + order);

		log.info("cancelOrder :: order.getOrderStatus() " + order.getOrderStatus());

		if (OrderStatus.PROCESSING != order.getOrderStatus() || DateUtil.isPastDateTime(order.getCancelExpiryTime()))
			throw new AppException(StringConst.ORDER_CANCELLED_REJECT, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		order = customerBuilder.cancelOrder(order, jwtUser.getUserEntity());
		log.info("cancelOrder :: order " + order);

		customerBuilder.saveAuditOrderStatus(order, jwtUser.getUserEntity(), OrderStatus.COMPLETED);

		notificationBuilder.createOrderCancelledByUserNotification(order, jwtUser.getUserEntity());

		return customerBuilder.checkFavAndCreateOrderModel(order, jwtUser.getUserEntity());
	}

	@Override
	@Transactional
	public OrderModel lastCompletedOrder(JwtUser jwtUser) {
		log.info("lastCompletedOrder");
		Order order = orderRepo.findLastCompletedOrderByUserId(jwtUser.getUserEntity().getId());
		if (order == null)
			throw new AppException(StringConst.ORDER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("lastCompletedOrder :: order " + order);

		return customerBuilder.checkFavAndCreateOrderModel(order, jwtUser.getUserEntity());
	}

	@Override
	@Transactional
	public boolean rateOrder(RateOrderForm rateOrderForm, JwtUser jwtUser) {
		log.info("rateOrder");
		Order order = orderRepo.findByOrderIdAndUserId(rateOrderForm.getOrderId(), jwtUser.getUserEntity().getId());
		if (order == null)
			throw new AppException(StringConst.ORDER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("rateOrder :: order " + order);

		if (OrderStatus.COMPLETED != order.getOrderStatus()
				|| (DeliveryType.DELIVERY == order.getDeliveryType() && null == order.getDriverOrderMapping())
				|| null == order.getOrderRatingDetail())
			throw new AppException(StringConst.RATING_SUBMIT_REJECT, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		if (!order.getOrderRatingDetail().isDispatcherPendingByCustomer()
				&& !order.getOrderRatingDetail().isDriverPendingByCustomer())
			throw new AppException(StringConst.RATING_SUBMIT_REJECT, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		if ((order.getOrderRatingDetail().isDispatcherPendingByCustomer()
				&& null == rateOrderForm.getRateDispatcherForm())
				|| (order.getOrderRatingDetail().isDriverPendingByCustomer()
						&& null == rateOrderForm.getRateDriverForm()))
			throw new AppException(StringConst.RATING_INVALID, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		if (order.getOrderRatingDetail().isDispatcherPendingByCustomer())
			customerBuilder.rateDispatcher(rateOrderForm.getRateDispatcherForm(), order, jwtUser.getUserEntity());

		if (order.getOrderRatingDetail().isDriverPendingByCustomer())
			customerBuilder.rateDriver(rateOrderForm.getRateDriverForm(), order, jwtUser.getUserEntity());

		return true;
	}

}