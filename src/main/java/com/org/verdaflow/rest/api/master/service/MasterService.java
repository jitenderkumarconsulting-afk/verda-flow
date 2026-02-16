package com.org.verdaflow.rest.api.master.service;

import java.util.List;

import com.org.verdaflow.rest.api.master.model.MasterModel;
import com.org.verdaflow.rest.config.security.jwt.JwtUser;

public interface MasterService {

	/**
	 * List Categories.
	 * 
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	List<MasterModel> listCategories(String query, JwtUser jwtUser);

	/**
	 * List Effects.
	 * 
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	List<MasterModel> listEffects(String query, JwtUser jwtUser);

	/**
	 * List Types.
	 * 
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	List<MasterModel> listTypes(String query, JwtUser jwtUser);

	/**
	 * List ETAs.
	 * 
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	List<MasterModel> listEtas(String query, JwtUser jwtUser);

	/**
	 * List Promo Codes of a particular Dispatcher.
	 * 
	 * @param dispatcherId
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	List<MasterModel> listPromoCodes(int dispatcherId, String query, JwtUser jwtUser);

}
