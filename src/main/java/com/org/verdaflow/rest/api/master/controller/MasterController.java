package com.org.verdaflow.rest.api.master.controller;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.org.verdaflow.rest.api.master.model.MasterModel;
import com.org.verdaflow.rest.api.master.service.MasterService;
import com.org.verdaflow.rest.config.common.AppConst;
import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.config.security.jwt.JwtUser;
import com.org.verdaflow.rest.dto.ResponseEnvelope;
import com.org.verdaflow.rest.util.AppUtil;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/master")
public class MasterController {
	public static final Logger log = LoggerFactory.getLogger(MasterController.class);

	@Autowired
	private MasterService masterService;

	@Autowired
	private AppUtil appUtil;

	/**
	 * This method is used to populate the list of categories.
	 *
	 * @param authtoken
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/categories")
	@ApiOperation(value = "This method is used to populate the list of categories.")
	public ResponseEnvelope<List<MasterModel>> listCategories(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestParam(value = "query", required = false, defaultValue = StringConst.BLANK_STRING) String query,
			JwtUser jwtUser) {
		log.info("listCategories");
		if (StringUtils.isNotBlank(query))
			appUtil.validateScriptingTagAlert(query);

		return new ResponseEnvelope<>(masterService.listCategories(appUtil.sanatizeQuery(query), jwtUser), true,
				StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the list of effects.
	 *
	 * @param authtoken
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/effects")
	@ApiOperation(value = "This method is used to populate the list of effects.")
	public ResponseEnvelope<List<MasterModel>> listEffects(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestParam(value = "query", required = false, defaultValue = StringConst.BLANK_STRING) String query,
			JwtUser jwtUser) {
		log.info("listEffects");
		if (StringUtils.isNotBlank(query))
			appUtil.validateScriptingTagAlert(query);

		return new ResponseEnvelope<>(masterService.listEffects(appUtil.sanatizeQuery(query), jwtUser), true,
				StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the list of types.
	 *
	 * @param authtoken
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/types")
	@ApiOperation(value = "This method is used to populate the list of types.")
	public ResponseEnvelope<List<MasterModel>> listTypes(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestParam(value = "query", required = false, defaultValue = StringConst.BLANK_STRING) String query,
			JwtUser jwtUser) {
		log.info("listTypes");
		if (StringUtils.isNotBlank(query))
			appUtil.validateScriptingTagAlert(query);

		return new ResponseEnvelope<>(masterService.listTypes(appUtil.sanatizeQuery(query), jwtUser), true,
				StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the list of ETAs.
	 *
	 * @param authtoken
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/etas")
	@ApiOperation(value = "This method is used to populate the list of ETAs.")
	public ResponseEnvelope<List<MasterModel>> listEtas(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestParam(value = "query", required = false, defaultValue = StringConst.BLANK_STRING) String query,
			JwtUser jwtUser) {
		log.info("listEtas");
		if (StringUtils.isNotBlank(query))
			appUtil.validateScriptingTagAlert(query);

		return new ResponseEnvelope<>(masterService.listEtas(appUtil.sanatizeQuery(query), jwtUser), true,
				StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the list of promo codes of a particular
	 * Dispatcher.
	 *
	 * @param authtoken
	 * @param dispatcherId
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/promoCodes")
	@ApiOperation(value = "This method is used to populate the list of promo codes of a particular Dispatcher.")
	public ResponseEnvelope<List<MasterModel>> listPromoCodes(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestParam("dispatcherId") int dispatcherId,
			@RequestParam(value = "query", required = false, defaultValue = StringConst.BLANK_STRING) String query,
			JwtUser jwtUser) {
		log.info("listPromoCodes");
		if (StringUtils.isNotBlank(query))
			appUtil.validateScriptingTagAlert(query);

		return new ResponseEnvelope<>(masterService.listPromoCodes(dispatcherId, appUtil.sanatizeQuery(query), jwtUser),
				true, StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

}
