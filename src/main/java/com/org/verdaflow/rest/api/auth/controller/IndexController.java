package com.org.verdaflow.rest.api.auth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.annotations.ApiOperation;

@Controller
public class IndexController {
	public static final Logger log = LoggerFactory.getLogger(IndexController.class);

	/**
	 * This method is used to populate terms & conditions.
	 * 
	 * @return
	 */
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/terms")
	@ApiOperation(value = "This method is used to populate terms & conditions of the application.")
	public String terms() {
		return "terms";
	}

	/**
	 * This method is used to populate privacy policies.
	 * 
	 * @return
	 */
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/privacy")
	@ApiOperation(value = "This method is used to populate privacy policies of the application.")
	public String privacy() {
		return "privacy";
	}

	/**
	 * This method is used to populate socket test of the application.
	 * 
	 * @return
	 */
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/socket/test")
	@ApiOperation(value = "This method is used to populate socket test of the application.")
	public String socketTest() {
		return "socket-test";
	}

	/**
	 * This method is used to populate socket test of the application.
	 * 
	 * @return
	 */
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/location/test")
	@ApiOperation(value = "This method is used to populate socket test of the application.")
	public String locationTest() {
		return "location-test";
	}

}
