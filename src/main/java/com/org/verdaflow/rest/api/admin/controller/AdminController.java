package com.org.verdaflow.rest.api.admin.controller;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
import com.org.verdaflow.rest.api.dispatcher.model.GraphCountModel;
import com.org.verdaflow.rest.api.user.form.ChangePasswordForm;
import com.org.verdaflow.rest.common.model.GenericModel;
import com.org.verdaflow.rest.config.common.AppConst;
import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.config.security.jwt.JwtUser;
import com.org.verdaflow.rest.dto.PaginatedResponse;
import com.org.verdaflow.rest.dto.ResponseEnvelope;
import com.org.verdaflow.rest.util.AppUtil;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
	public static final Logger log = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	private AdminService adminService;

	@Autowired
	private Environment env;

	@Autowired
	private AppUtil appUtil;

	/**
	 * This method is used to change password.
	 * 
	 * @param authtoken
	 * @param changePasswordForm
	 * @param jwtUser
	 * @return
	 */
	@PatchMapping(value = "/changePassword")
	@ApiOperation(value = "This method is used to change password.")
	public ResponseEnvelope<GenericModel<Boolean>> changePassword(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestBody @Valid ChangePasswordForm changePasswordForm, JwtUser jwtUser) {
		log.info("changePassword");
		return new ResponseEnvelope<>(new GenericModel<>(adminService.changePassword(changePasswordForm, jwtUser)),
				true, env.getProperty(StringConst.PASWRD_CHANGE_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for registering the dispatcher into the application.
	 * 
	 * @param authtoken
	 * @param registerDispatcherForm
	 * @param jwtUser
	 * @return
	 */
	@PostMapping(value = "/dispatcher")
	@ApiOperation(value = "This method is used for registering the dispatcher into the application.")
	public ResponseEnvelope<GenericModel<Boolean>> registerDispatcher(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestBody @Valid RegisterDispatcherForm registerDispatcherForm, JwtUser jwtUser) {
		log.info("registerDispatcher");
		return new ResponseEnvelope<>(
				new GenericModel<>(adminService.registerDispatcher(registerDispatcherForm, jwtUser)), true,
				env.getProperty(StringConst.REGISTER_DISPATCHER), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for updating the dispatcher details into the application.
	 * 
	 * @param authtoken
	 * @param registerDispatcherForm
	 * @param jwtUser
	 * @return
	 */
	@PatchMapping(value = "/dispatcher")
	@ApiOperation(value = "This method is used for updating the dispatcher details into the application.")
	public ResponseEnvelope<UserDetailModel> updateDispatcherDetails(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestBody @Valid RegisterDispatcherForm registerDispatcherForm, JwtUser jwtUser) {
		log.info("updateDispatcherDetails");
		return new ResponseEnvelope<>(adminService.updateDispatcherDetails(registerDispatcherForm, jwtUser), true,
				env.getProperty(StringConst.UPDATE_DISPATCHER), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the list of dispatcher users application
	 * received on the platform.
	 *
	 * @param authtoken
	 * @param page
	 * @param size
	 * @param query
	 * @param filter
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/users/dispatcher")
	@ApiOperation(value = "This method is used to populate the list of dispatcher users application received on the platform.", notes = "Filter Values "
			+ "(Inside 'int' param 'filter') :  "
			+ " 0 = All dispatcher users, 1 = All approved dispatcher users, 2 = All pending dispatcher users, 3 = All rejected dispatcher users")
	public ResponseEnvelope<PaginatedResponse<UserDetailModel>> listDispatcherUsers(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @RequestParam("page") int page,
			@RequestParam("size") int size,
			@RequestParam(value = "query", required = false, defaultValue = StringConst.BLANK_STRING) String query,
			@RequestParam(value = "filter", required = false, defaultValue = "0") int filter, JwtUser jwtUser) {
		log.info("listDispatcherUsers");
		if (StringUtils.isNotBlank(query))
			appUtil.validateScriptingTagAlert(query);

		Pageable pageable = new PageRequest(page - 1, size);
		return new ResponseEnvelope<>(
				adminService.listDispatcherUsers(pageable, appUtil.sanatizeQuery(query), filter, jwtUser), true,
				StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to fetch the dispatcher user details application received
	 * on the platform.
	 *
	 * @param authtoken
	 * @param dispatcherUserId
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/users/dispatcher/{dispatcherUserId}")
	@ApiOperation(value = "This method is used to fetch the dispatcher user details application received on the platform.")
	public ResponseEnvelope<UserDetailModel> userDispatcherDetails(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@PathVariable(value = "dispatcherUserId", required = true) int dispatcherUserId, JwtUser jwtUser) {
		log.info("userDispatcherDetails");

		return new ResponseEnvelope<>(adminService.getUserDispatcherDetails(dispatcherUserId, jwtUser), true,
				StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the list of driver users application received
	 * on the platform on the basis of dispatcher Id.
	 *
	 * @param authtoken
	 * @param page
	 * @param size
	 * @param query
	 * @param dispatcherId
	 * @param filter
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/users/{dispatcherUserId}/driver")
	@ApiOperation(value = "This method is used to populate the list of driver users application received on the platform on the basis of dispatcher Id.", notes = "Filter Values "
			+ "(Inside 'int' param 'filter') :  "
			+ " 0 = All driver users, 1 = All approved driver users, 2 = All pending driver users, 3 = All rejected driver users")
	public ResponseEnvelope<UserDriverListWithUserDispatcherModel> listDriverUsersForDispatcher(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @RequestParam("page") int page,
			@RequestParam("size") int size,
			@RequestParam(value = "query", required = false, defaultValue = StringConst.BLANK_STRING) String query,
			@PathVariable(value = "dispatcherUserId", required = true) int dispatcherUserId,
			@RequestParam(value = "filter", required = false, defaultValue = "0") int filter, JwtUser jwtUser) {
		log.info("listDriverUsersForDispatcher");
		if (StringUtils.isNotBlank(query))
			appUtil.validateScriptingTagAlert(query);

		Pageable pageable = new PageRequest(page - 1, size);
		return new ResponseEnvelope<>(adminService.listDriverUsersForDispatcher(pageable, appUtil.sanatizeQuery(query),
				dispatcherUserId, filter, jwtUser), true, StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for creating new master category.
	 *
	 * @param authtoken
	 * @param categoryForm
	 * @param jwtUser
	 * @return
	 */
	@PostMapping(value = "/masterCategory")
	@ApiOperation(value = "This method is used for creating new category.")
	public ResponseEnvelope<GenericModel<Boolean>> createMasterCategory(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestBody @Valid MasterCategoryForm categoryForm, JwtUser jwtUser) {
		log.info("createMasterCategory");
		return new ResponseEnvelope<>(new GenericModel<>(adminService.createMasterCategory(categoryForm, jwtUser)),
				true, env.getProperty(StringConst.CREATE_CATEGORY_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for updating the master category details.
	 *
	 * @param authtoken
	 * @param categoryForm
	 * @param jwtUser
	 * @return
	 */

	@PatchMapping(value = "/masterCategory")
	@ApiOperation(value = "This method is used for updating the category details.")
	public ResponseEnvelope<GenericModel<Boolean>> updateMasterCategory(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestBody @Valid MasterCategoryForm categoryForm, JwtUser jwtUser) {
		log.info("updateMasterCategory");

		return new ResponseEnvelope<>(new GenericModel<>(adminService.updateMasterCategory(categoryForm, jwtUser)),
				true, env.getProperty(StringConst.UPDATE_CATEGORY_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for deleting the master category.
	 *
	 * @param authtoken
	 * @param categoryId
	 * @param jwtUser
	 * @return
	 */
	@DeleteMapping(value = "/masterCategory/{categoryId}")
	@ApiOperation(value = "This method is used for deleting the category.")
	public ResponseEnvelope<GenericModel<Boolean>> deleteMasterCategory(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@PathVariable(value = "categoryId", required = true) int categoryId, JwtUser jwtUser) {
		log.info("deleteMasterCategory");
		return new ResponseEnvelope<>(new GenericModel<>(adminService.deleteMasterCategory(categoryId, jwtUser)), true,
				env.getProperty(StringConst.DELETE_CATEGORY_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the list of master categories.
	 * 
	 * @param authtoken
	 * @param page
	 * @param size
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/masterCategories")
	@ApiOperation(value = "This method is used to populate the list of categories.")
	public ResponseEnvelope<PaginatedResponse<MasterCategoryModel>> listMasterCategories(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @RequestParam("page") int page,
			@RequestParam("size") int size,
			@RequestParam(value = "query", required = false, defaultValue = StringConst.BLANK_STRING) String query,
			JwtUser jwtUser) {
		log.info("listMasterCategories");
		if (StringUtils.isNotBlank(query))
			appUtil.validateScriptingTagAlert(query);

		Pageable pageable = new PageRequest(page - 1, size);
		return new ResponseEnvelope<>(
				adminService.listMasterCategories(pageable, appUtil.sanatizeQuery(query), jwtUser), true,
				StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to fetch the master category details.
	 *
	 * @param authtoken
	 * @param categoryId
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/masterCategory/{categoryId}")
	@ApiOperation(value = "This method is used to fetch the category details.")
	public ResponseEnvelope<MasterCategoryModel> getMasterCategory(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@PathVariable(value = "categoryId", required = true) int categoryId, JwtUser jwtUser) {
		log.info("getMasterCategory");
		return new ResponseEnvelope<>(adminService.getMasterCategory(categoryId, jwtUser), true,
				StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for creating new master effect.
	 *
	 * @param authtoken
	 * @param masterEffectForm
	 * @param jwtUser
	 * @return
	 */
	@PostMapping(value = "/masterEffect")
	@ApiOperation(value = "This method is used for creating new effect.")
	public ResponseEnvelope<GenericModel<Boolean>> createMasterEffect(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestBody @Valid MasterEffectForm masterEffectForm, JwtUser jwtUser) {
		log.info("createMasterEffect");
		return new ResponseEnvelope<>(new GenericModel<>(adminService.createMasterEffect(masterEffectForm, jwtUser)),
				true, env.getProperty(StringConst.CREATE_EFFECT_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for updating the master effect details.
	 *
	 * @param authtoken
	 * @param masterEffectForm
	 * @param jwtUser
	 * @return
	 */

	@PatchMapping(value = "/masterEffect")
	@ApiOperation(value = "This method is used for updating the effect details.")
	public ResponseEnvelope<GenericModel<Boolean>> updateMasterEffect(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestBody @Valid MasterEffectForm masterEffectForm, JwtUser jwtUser) {
		log.info("updateMasterEffect");

		return new ResponseEnvelope<>(new GenericModel<>(adminService.updateMasterEffect(masterEffectForm, jwtUser)),
				true, env.getProperty(StringConst.UPDATE_EFFECT_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for deleting effect the master effect.
	 *
	 * @param authtoken
	 * @param effectId
	 * @param jwtUser
	 * @return
	 */
	@DeleteMapping(value = "/masterEffect/{effectId}")
	@ApiOperation(value = "This method is used for deleting the effect.")
	public ResponseEnvelope<GenericModel<Boolean>> deleteMasterEffect(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@PathVariable(value = "effectId", required = true) int effectId, JwtUser jwtUser) {
		log.info("deleteMasterEffect");
		return new ResponseEnvelope<>(new GenericModel<>(adminService.deleteMasterEffect(effectId, jwtUser)), true,
				env.getProperty(StringConst.DELETE_EFFECT_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the list of master effects.
	 * 
	 * @param authtoken
	 * @param page
	 * @param size
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/masterEffects")
	@ApiOperation(value = "This method is used to populate the list of effects.")
	public ResponseEnvelope<PaginatedResponse<MasterEffectModel>> listMasterEffect(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @RequestParam("page") int page,
			@RequestParam("size") int size,
			@RequestParam(value = "query", required = false, defaultValue = StringConst.BLANK_STRING) String query,
			JwtUser jwtUser) {
		log.info("listMasterEffect");
		if (StringUtils.isNotBlank(query))
			appUtil.validateScriptingTagAlert(query);

		Pageable pageable = new PageRequest(page - 1, size);
		return new ResponseEnvelope<>(adminService.listMasterEffects(pageable, appUtil.sanatizeQuery(query), jwtUser),
				true, StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for get the master effect details.
	 *
	 * @param authtoken
	 * @param effectId
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/masterEffect/{effectId}")
	@ApiOperation(value = "This method is used for get the effect details.")
	public ResponseEnvelope<MasterEffectModel> getMasterEffect(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@PathVariable(value = "effectId", required = true) int effectId, JwtUser jwtUser) {
		log.info("getMasterEffect");
		return new ResponseEnvelope<>(adminService.getMasterEffect(effectId, jwtUser), true, StringConst.EMPTY_STRING,
				AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for creating new master type.
	 *
	 * @param authtoken
	 * @param masterTypeForm
	 * @param jwtUser
	 * @return
	 */
	@PostMapping(value = "/masterType")
	@ApiOperation(value = "This method is used for creating new type.")
	public ResponseEnvelope<GenericModel<Boolean>> createMasterType(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestBody @Valid MasterTypeForm masterTypeForm, JwtUser jwtUser) {
		log.info("createMasterType");
		return new ResponseEnvelope<>(new GenericModel<>(adminService.createMasterType(masterTypeForm, jwtUser)), true,
				env.getProperty(StringConst.CREATE_TYPE_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for updating the master type details.
	 *
	 * @param authtoken
	 * @param masterTypeForm
	 * @param jwtUser
	 * @return
	 */

	@PatchMapping(value = "/masterType")
	@ApiOperation(value = "This method is used for updating the type details.")
	public ResponseEnvelope<GenericModel<Boolean>> updateMasterType(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestBody @Valid MasterTypeForm masterTypeForm, JwtUser jwtUser) {
		log.info("updateMasterType");

		return new ResponseEnvelope<>(new GenericModel<>(adminService.updateMasterType(masterTypeForm, jwtUser)), true,
				env.getProperty(StringConst.UPDATE_TYPE_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for deleting type the master type.
	 *
	 * @param authtoken
	 * @param typeId
	 * @param jwtUser
	 * @return
	 */
	@DeleteMapping(value = "/masterType/{typeId}")
	@ApiOperation(value = "This method is used for deleting the type.")
	public ResponseEnvelope<GenericModel<Boolean>> deleteMasterType(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@PathVariable(value = "typeId", required = true) int typeId, JwtUser jwtUser) {
		log.info("deleteMasterType");
		return new ResponseEnvelope<>(new GenericModel<>(adminService.deleteMasterType(typeId, jwtUser)), true,
				env.getProperty(StringConst.DELETE_TYPE_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the list of master types.
	 * 
	 * @param authtoken
	 * @param page
	 * @param size
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/masterTypes")
	@ApiOperation(value = "This method is used to populate the list of types.")
	public ResponseEnvelope<PaginatedResponse<MasterTypeModel>> listMasterTypes(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @RequestParam("page") int page,
			@RequestParam("size") int size,
			@RequestParam(value = "query", required = false, defaultValue = StringConst.BLANK_STRING) String query,
			JwtUser jwtUser) {
		log.info("listMasterTypes");
		if (StringUtils.isNotBlank(query))
			appUtil.validateScriptingTagAlert(query);

		Pageable pageable = new PageRequest(page - 1, size);
		return new ResponseEnvelope<>(adminService.listMasterTypes(pageable, appUtil.sanatizeQuery(query), jwtUser),
				true, StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for get the master type details.
	 *
	 * @param authtoken
	 * @param typeId
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/masterType/{typeId}")
	@ApiOperation(value = "This method is used for get the type details.")
	public ResponseEnvelope<MasterTypeModel> getMasterType(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@PathVariable(value = "typeId", required = true) int typeId, JwtUser jwtUser) {
		log.info("getMasterType");
		return new ResponseEnvelope<>(adminService.getMasterType(typeId, jwtUser), true, StringConst.EMPTY_STRING,
				AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for creating new master ETA.
	 *
	 * @param authtoken
	 * @param masterEtaForm
	 * @param jwtUser
	 * @return
	 */
	@PostMapping(value = "/masterEta")
	@ApiOperation(value = "This method is used for creating new ETA.")
	public ResponseEnvelope<GenericModel<Boolean>> createMasterEta(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestBody @Valid MasterEtaForm masterEtaForm, JwtUser jwtUser) {
		log.info("createMasterEta");
		return new ResponseEnvelope<>(new GenericModel<>(adminService.createMasterEta(masterEtaForm, jwtUser)), true,
				env.getProperty(StringConst.CREATE_ETA_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for updating the master ETA details.
	 *
	 * @param authtoken
	 * @param masterEtaForm
	 * @param jwtUser
	 * @return
	 */

	@PatchMapping(value = "/masterEta")
	@ApiOperation(value = "This method is used for updating the ETA details.")
	public ResponseEnvelope<GenericModel<Boolean>> updateMasterEta(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestBody @Valid MasterEtaForm masterEtaForm, JwtUser jwtUser) {
		log.info("updateMasterEta");

		return new ResponseEnvelope<>(new GenericModel<>(adminService.updateMasterEta(masterEtaForm, jwtUser)), true,
				env.getProperty(StringConst.UPDATE_ETA_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for deleting the master eta.
	 *
	 * @param authtoken
	 * @param etaId
	 * @param jwtUser
	 * @return
	 */
	@DeleteMapping(value = "/masterEta/{etaId}")
	@ApiOperation(value = "This method is used for deleting the ETA.")
	public ResponseEnvelope<GenericModel<Boolean>> deleteMasterEta(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@PathVariable(value = "etaId", required = true) int etaId, JwtUser jwtUser) {
		log.info("deleteMasterEta");
		return new ResponseEnvelope<>(new GenericModel<>(adminService.deleteMasterEta(etaId, jwtUser)), true,
				env.getProperty(StringConst.DELETE_ETA_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the list of master eta.
	 * 
	 * @param authtoken
	 * @param page
	 * @param size
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/masterEtas")
	@ApiOperation(value = "This method is used to populate the list of master eta.")
	public ResponseEnvelope<PaginatedResponse<MasterEtaModel>> listMasterEta(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @RequestParam("page") int page,
			@RequestParam("size") int size,
			@RequestParam(value = "query", required = false, defaultValue = StringConst.BLANK_STRING) String query,
			JwtUser jwtUser) {
		log.info("listMasterEta");
		if (StringUtils.isNotBlank(query))
			appUtil.validateScriptingTagAlert(query);

		Pageable pageable = new PageRequest(page - 1, size);
		return new ResponseEnvelope<>(adminService.listMasterEtas(pageable, appUtil.sanatizeQuery(query), jwtUser),
				true, StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for get the master eta details.
	 *
	 * @param authtoken
	 * @param etaId
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/masterEta/{etaId}")
	@ApiOperation(value = "This method is used for get the master eta details.")
	public ResponseEnvelope<MasterEtaModel> getMasterEta(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@PathVariable(value = "etaId", required = true) int etaId, JwtUser jwtUser) {
		log.info("getMasterEta");
		return new ResponseEnvelope<>(adminService.getMasterEta(etaId, jwtUser), true, StringConst.EMPTY_STRING,
				AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to logout the Admin.
	 * 
	 * @param authtoken
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/logout")
	@ApiOperation(value = "This method is used to logout the Admin.")
	public ResponseEnvelope<GenericModel<Boolean>> logout(
			@RequestHeader(value = "authtoken", required = true) String authtoken, JwtUser jwtUser) {
		log.info("AdminController.logout");

		return new ResponseEnvelope<>(new GenericModel<>(adminService.logout(jwtUser)), true,
				env.getProperty(StringConst.LOGOUT_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to activate or deactivate a Dispatcher.
	 * 
	 * @param authtoken
	 * @param dispatcherUserId
	 * @param deact
	 * @param jwtUser
	 * 
	 * @return
	 */
	@GetMapping(value = "/activateDispatcher/{dispatcherUserId}")
	@ApiOperation(value = "This method is used to activate or deactivate a Dispatcher", notes = " PARAM DESCRIPTION  :  "
			+ " deact(boolean) - true (Will deactivate the Dispatcher) ")
	public ResponseEnvelope<UserDetailModel> activateDispatcher(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@PathVariable("dispatcherUserId") int dispatcherUserId,
			@RequestParam(value = "deact", required = false) boolean deact, JwtUser jwtUser) {
		log.info("activateDispatcher");

		return new ResponseEnvelope<>(adminService.activateDispatcher(dispatcherUserId, deact, jwtUser), true,
				deact ? env.getProperty(StringConst.DISPATCHER_DEACTIVATED_SUCCESS)
						: env.getProperty(StringConst.DISPATCHER_ACTIVATED_SUCCESS),
				AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the list of customer users application
	 * received on the platform.
	 *
	 * @param authtoken
	 * @param page
	 * @param size
	 * @param query
	 * @param filter
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/users/customer")
	@ApiOperation(value = "This method is used to populate the list of customer users application received on the platform.", notes = "Filter Values "
			+ "(Inside 'int' param 'filter') :  "
			+ " 0 = All customer users, 1 = All approved customer users, 2 = All pending customer users, 3 = All rejected customer users")
	public ResponseEnvelope<PaginatedResponse<UserDetailModel>> listCustomerUsers(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @RequestParam("page") int page,
			@RequestParam("size") int size,
			@RequestParam(value = "query", required = false, defaultValue = StringConst.BLANK_STRING) String query,
			@RequestParam(value = "filter", required = false, defaultValue = "0") int filter, JwtUser jwtUser) {
		log.info("listCustomerUsers");
		if (StringUtils.isNotBlank(query))
			appUtil.validateScriptingTagAlert(query);

		Pageable pageable = new PageRequest(page - 1, size);
		return new ResponseEnvelope<>(
				adminService.listCustomerUsers(pageable, appUtil.sanatizeQuery(query), filter, jwtUser), true,
				StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to activate or deactivate a Customer.
	 * 
	 * @param authtoken
	 * @param customerUserId
	 * @param deact
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/activateCustomer/{customerUserId}")
	@ApiOperation(value = "This method is used to activate or deactivate a Customer", notes = " PARAM DESCRIPTION  :  "
			+ " deact(boolean) - true (Will deactivate the Customer) ")
	public ResponseEnvelope<UserDetailModel> activateCustomer(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@PathVariable("customerUserId") int customerUserId,
			@RequestParam(value = "deact", required = false) boolean deact, JwtUser jwtUser) {
		log.info("activateCustomer");

		return new ResponseEnvelope<>(adminService.activateCustomer(customerUserId, deact, jwtUser), true,
				deact ? env.getProperty(StringConst.CUSTOMER_DEACTIVATED_SUCCESS)
						: env.getProperty(StringConst.CUSTOMER_ACTIVATED_SUCCESS),
				AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the list of orders of the Dispatcher.
	 * 
	 * @param authtoken
	 * @param page
	 * @param size
	 * @param dispatcherUserId
	 * @param query
	 * @param filter
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/orders/dispatcher/{dispatcherUserId}")
	@ApiOperation(value = "This method is used to populate the list of orders for Dispatcher.", notes = "Filter Values "
			+ "(Inside 'int' param 'filter') :  "
			+ " 0 = All orders, 1 = All new and pending orders, 2 = All completed orders")
	public ResponseEnvelope<OrderListWithUserDispatcherModel> listOrdersForDispatcher(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @RequestParam("page") int page,
			@RequestParam(value = "query", required = false, defaultValue = StringConst.BLANK_STRING) String query,
			@RequestParam(value = "filter", required = false, defaultValue = "0") int filter,
			@RequestParam("size") int size, @PathVariable(value = "dispatcherUserId") int dispatcherUserId,
			JwtUser jwtUser) {
		log.info("listOrdersForDispatcher");
		if (StringUtils.isNotBlank(query))
			appUtil.validateScriptingTagAlert(query);

		Pageable pageable = new PageRequest(page - 1, size);
		return new ResponseEnvelope<>(
				adminService.listOrdersForDispatcher(pageable, dispatcherUserId, query, filter, jwtUser), true,
				StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the list of orders of the Customer.
	 * 
	 * @param authtoken
	 * @param page
	 * @param size
	 * @param customerUserId
	 * @param filter
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/orders/customer/{customerUserId}")
	@ApiOperation(value = "This method is used to populate the list of orders of the Customer.", notes = "Filter Values "
			+ "(Inside 'int' param 'filter') :  "
			+ " 0 = All orders, 1 = All new orders, 2 = All pending orders , 3 = All completed orders")
	public ResponseEnvelope<OrderListWithUserCustomerModel> listOrdersofCustomer(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @RequestParam("page") int page,
			@RequestParam("size") int size, @PathVariable(value = "customerUserId") int customerUserId,
			@RequestParam(value = "filter", required = false, defaultValue = "0") int filter, JwtUser jwtUser) {
		log.info("listOrdersofCustomer");

		Pageable pageable = new PageRequest(page - 1, size);
		return new ResponseEnvelope<>(adminService.listOrdersOfCustomer(pageable, customerUserId, filter, jwtUser),
				true, StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the graph for products sold by dispatcherId.
	 * 
	 * @param authtoken
	 * @param dispatcherId
	 * @param startDate
	 * @param endDate
	 * @param filter
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "graph/productSold/{dispatcherId}")
	@ApiOperation(value = "This method is used to populate the graph for products sold.", notes = "Filter Values "
			+ "(Inside 'int' param 'filter') :  " + " 0 = Date,1=Weekly, 2 = Month, 3 = Year")
	public ResponseEnvelope<GraphCountModel> graphProductSold(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@PathVariable(value = "dispatcherId") int dispatcherId, @RequestParam("startDate") long startDate,
			@RequestParam("endDate") long endDate,
			@RequestParam(value = "filter", required = false, defaultValue = "0") int filter, JwtUser jwtUser) {
		log.info("graphProductSold");
		return new ResponseEnvelope<>(adminService.graphProductSold(dispatcherId, startDate, endDate, filter, jwtUser),
				true, StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

}
