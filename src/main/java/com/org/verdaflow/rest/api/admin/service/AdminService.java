package com.org.verdaflow.rest.api.admin.service;

import org.springframework.data.domain.Pageable;

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
import com.org.verdaflow.rest.api.dispatcher.model.GraphCountModel;
import com.org.verdaflow.rest.api.user.form.ChangePasswordForm;
import com.org.verdaflow.rest.config.security.jwt.JwtUser;
import com.org.verdaflow.rest.dto.PaginatedResponse;

public interface AdminService {

	/**
	 * Change password.
	 * 
	 * @param changePasswordForm
	 * @param jwtUser
	 * @return
	 */
	boolean changePassword(ChangePasswordForm changePasswordForm, JwtUser jwtUser);

	/**
	 * Register new Dispatcher.
	 * 
	 * @param registerDispatcherForm
	 * @param jwtUser
	 * @return
	 */
	boolean registerDispatcher(RegisterDispatcherForm registerDispatcherForm, JwtUser jwtUser);

	/**
	 * Update dispatcher details.
	 * 
	 * @param registerDispatcherForm
	 * @param jwtUser
	 * @return
	 */
	UserDetailModel updateDispatcherDetails(RegisterDispatcherForm registerDispatcherForm, JwtUser jwtUser);

	/**
	 * List Dispatchers.
	 * 
	 * @param pageable
	 * @param query
	 * @param filter
	 *                 (0 = All dispatcher users, 1 = All approved dispatcher users,
	 *                 2 =
	 *                 All pending dispatcher users, 3 = All rejected dispatcher
	 *                 users)
	 * @param jwtUser
	 * @return
	 */
	PaginatedResponse<UserDetailModel> listDispatcherUsers(Pageable pageable, String query, int filter,
			JwtUser jwtUser);

	/**
	 * User Dispatcher Details complete fetch
	 * 
	 * @param dispatcherUserId
	 * @return
	 */
	UserDetailModel getUserDispatcherDetails(int dispatcherUserId, JwtUser jwtUser);

	/**
	 * List Drivers on the basis of dispatcher Id.
	 * 
	 * @param pageable
	 * @param query
	 * @param dispatcherId
	 * @param filter
	 *                     (0 = All driver users, 1 = All approved driver users, 2 =
	 *                     All
	 *                     pending driver users, 3 = All rejected driver users)
	 * @param jwtUser
	 * @return
	 */
	UserDriverListWithUserDispatcherModel listDriverUsersForDispatcher(Pageable pageable, String query,
			int dispatcherId, int filter, JwtUser jwtUser);

	/**
	 * Create Category.
	 * 
	 * @param categoryForm
	 * @param jwtUser
	 */
	boolean createMasterCategory(MasterCategoryForm categoryForm, JwtUser jwtUser);

	/**
	 * Update Category.
	 * 
	 * @param categoryForm
	 * @param jwtUser
	 * @return
	 */
	boolean updateMasterCategory(MasterCategoryForm categoryForm, JwtUser jwtUser);

	/**
	 * Delete Category.
	 * 
	 * @param categoryId
	 * @param jwtUser
	 */
	boolean deleteMasterCategory(int categoryId, JwtUser jwtUser);

	/**
	 * List Categories.
	 * 
	 * @param pageable
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	PaginatedResponse<MasterCategoryModel> listMasterCategories(Pageable pageable, String query, JwtUser jwtUser);

	/**
	 * Get Category Details.
	 * 
	 * @param categoryId
	 * @param jwtUser
	 */
	MasterCategoryModel getMasterCategory(int categoryId, JwtUser jwtUser);

	/**
	 * Create Effect.
	 * 
	 * @param masterEffectForm
	 * @param jwtUser
	 */

	boolean createMasterEffect(MasterEffectForm masterEffectForm, JwtUser jwtUser);

	/**
	 * Update Effect.
	 * 
	 * @param masterEffectForm
	 * @param jwtUser
	 * @return
	 */
	boolean updateMasterEffect(MasterEffectForm masterEffectForm, JwtUser jwtUser);

	/**
	 * Delete Effect.
	 * 
	 * @param effectId
	 * @param jwtUser
	 */
	boolean deleteMasterEffect(int effectId, JwtUser jwtUser);

	/**
	 * List Effects.
	 * 
	 * @param pageable
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	PaginatedResponse<MasterEffectModel> listMasterEffects(Pageable pageable, String query, JwtUser jwtUser);

	/**
	 * Get Effect Details.
	 * 
	 * @param effectId
	 * @param jwtUser
	 */
	MasterEffectModel getMasterEffect(int effectId, JwtUser jwtUser);

	/**
	 * Create Type.
	 * 
	 * @param masterTypeForm
	 * @param jwtUser
	 */

	boolean createMasterType(MasterTypeForm masterTypeForm, JwtUser jwtUser);

	/**
	 * Update Type.
	 * 
	 * @param masterTypeForm
	 * @param jwtUser
	 * @return
	 */
	boolean updateMasterType(MasterTypeForm masterTypeForm, JwtUser jwtUser);

	/**
	 * Delete Type.
	 * 
	 * @param typeId
	 * @param jwtUser
	 */
	boolean deleteMasterType(int typeId, JwtUser jwtUser);

	/**
	 * List Types.
	 * 
	 * @param pageable
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	PaginatedResponse<MasterTypeModel> listMasterTypes(Pageable pageable, String query, JwtUser jwtUser);

	/**
	 * Get Type details.
	 * 
	 * @param typeId
	 * @param jwtUser
	 */
	MasterTypeModel getMasterType(int typeId, JwtUser jwtUser);

	/**
	 * Create ETA.
	 * 
	 * @param masterEtaForm
	 * @param jwtUser
	 */

	boolean createMasterEta(MasterEtaForm masterEtaForm, JwtUser jwtUser);

	/**
	 * Update ETA.
	 * 
	 * @param masterEtaForm
	 * @param jwtUser
	 * @return
	 */
	boolean updateMasterEta(MasterEtaForm masterEtaForm, JwtUser jwtUser);

	/**
	 * Delete ETA.
	 * 
	 * @param etaId
	 * @param jwtUser
	 */
	boolean deleteMasterEta(int etaId, JwtUser jwtUser);

	/**
	 * List ETAs.
	 * 
	 * @param pageable
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	PaginatedResponse<MasterEtaModel> listMasterEtas(Pageable pageable, String query, JwtUser jwtUser);

	/**
	 * Get ETA details.
	 * 
	 * @param etaId
	 * @param jwtUser
	 */
	MasterEtaModel getMasterEta(int etaId, JwtUser jwtUser);

	/**
	 * Logout
	 * 
	 * @param jwtUser
	 * @return
	 */
	boolean logout(JwtUser jwtUser);

	/**
	 * Activate - Deactivate a Dispatcher
	 * 
	 * @param dispatcherUserId
	 * @param deact
	 * @param jwtUser
	 * @return
	 */
	UserDetailModel activateDispatcher(int dispatcherUserId, boolean deact, JwtUser jwtUser);

	/**
	 * List Customers.
	 * 
	 * @param pageable
	 * @param query
	 * @param filter
	 *                 (0 = All customer users, 1 = All approved customer users, 2 =
	 *                 All
	 *                 pending customer users, 3 = All rejected customer users)
	 * @param jwtUser
	 * @return
	 */
	PaginatedResponse<UserDetailModel> listCustomerUsers(Pageable pageable, String query, int filter, JwtUser jwtUser);

	/**
	 * Activate - Deactivate a Customer
	 * 
	 * @param customerUserId
	 * @param deact
	 * @param jwtUser
	 * @return
	 */
	UserDetailModel activateCustomer(int customerUserId, boolean deact, JwtUser jwtUser);

	/**
	 * List Orders of Dispatcher.
	 *
	 * @param pageable
	 * @param dispatcherUserId
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	OrderListWithUserDispatcherModel listOrdersForDispatcher(Pageable pageable, int dispatcherUserId, String query,
			int filter, JwtUser jwtUser);

	/**
	 * List Orders of Customer.
	 *
	 * @param pageable
	 * @param customerUserId
	 * @param filter
	 * @param jwtUser
	 * @return
	 */
	OrderListWithUserCustomerModel listOrdersOfCustomer(Pageable pageable, int customerUserId, int filter,
			JwtUser jwtUser);

	/**
	 * Product Sold Graph.
	 * 
	 * @param dispatcherId
	 * @param startDate
	 * @param endDate
	 * @param filter
	 * @param jwtUser
	 * @return
	 */
	GraphCountModel graphProductSold(int dispatcherId, long startDate, long endDate, int filter, JwtUser jwtUser);

}
