package com.org.verdaflow.rest.api.master.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.verdaflow.rest.api.master.MasterBuilder;
import com.org.verdaflow.rest.api.master.model.MasterModel;
import com.org.verdaflow.rest.api.master.service.MasterService;
import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.config.security.jwt.JwtUser;
import com.org.verdaflow.rest.entity.MasterCategory;
import com.org.verdaflow.rest.entity.MasterEffect;
import com.org.verdaflow.rest.entity.MasterEta;
import com.org.verdaflow.rest.entity.MasterType;
import com.org.verdaflow.rest.entity.PromoCode;
import com.org.verdaflow.rest.repo.MasterCategoryRepo;
import com.org.verdaflow.rest.repo.MasterEffectRepo;
import com.org.verdaflow.rest.repo.MasterEtaRepo;
import com.org.verdaflow.rest.repo.MasterTypeRepo;
import com.org.verdaflow.rest.repo.PromoCodeRepo;
import com.org.verdaflow.rest.util.AppUtil;

@Service
public class MasterServiceImpl implements MasterService {
	public static final Logger log = LoggerFactory.getLogger(MasterServiceImpl.class);

	@Autowired
	private MasterBuilder masterBuilder;

	@Autowired
	private MasterCategoryRepo masterCategoryRepo;

	@Autowired
	private MasterEffectRepo masterEffectRepo;

	@Autowired
	private MasterTypeRepo masterTypeRepo;

	@Autowired
	private MasterEtaRepo masterEtaRepo;

	@Autowired
	private AppUtil appUtil;

	@Autowired
	private PromoCodeRepo promoCodeRepo;

	@Override
	@Transactional
	public List<MasterModel> listCategories(String query, JwtUser jwtUser) {
		log.info("listCategories");
		List<MasterCategory> masterCategories = null;

		if (StringUtils.isBlank(query) && null == query) {
			masterCategories = masterCategoryRepo.findAllActiveCategories();
		} else {
			String searchQuery = new StringBuilder(StringConst.PERCENTILE).append(appUtil.sanatizeQuery(query))
					.append(StringConst.PERCENTILE).toString();

			masterCategories = masterCategoryRepo.findAllActiveCategoriesSearch(searchQuery);
		}

		log.info("listCategories :: masterCategories " + masterCategories);

		List<MasterModel> masterModels = new ArrayList<>();
		if (!masterCategories.isEmpty()) {
			masterModels = masterCategories.stream().filter(predicate -> !predicate.isDeleted())
					.map(masterCategory -> masterBuilder.createMasterModel(masterCategory))
					.collect(Collectors.toList());
		}
		log.info("listCategories :: masterModels " + masterModels);

		return masterModels;

	}

	@Override
	@Transactional
	public List<MasterModel> listEffects(String query, JwtUser jwtUser) {
		log.info("listEffects");
		List<MasterEffect> masterEffects = null;

		if (StringUtils.isBlank(query) && null == query) {
			masterEffects = masterEffectRepo.findAllActiveEffects();
		} else {
			String searchQuery = new StringBuilder(StringConst.PERCENTILE).append(appUtil.sanatizeQuery(query))
					.append(StringConst.PERCENTILE).toString();

			masterEffects = masterEffectRepo.findAllActiveEffectsSearch(searchQuery);
		}

		log.info("listEffects :: masterEffects " + masterEffects);

		List<MasterModel> masterModels = new ArrayList<>();
		if (!masterEffects.isEmpty()) {
			masterModels = masterEffects.stream().filter(predicate -> !predicate.isDeleted())
					.map(masterEffect -> masterBuilder.createMasterModel(masterEffect)).collect(Collectors.toList());
		}
		log.info("listEffects :: masterModels " + masterModels);

		return masterModels;
	}

	@Override
	@Transactional
	public List<MasterModel> listTypes(String query, JwtUser jwtUser) {
		log.info("listTypes");
		List<MasterType> masterTypes = null;

		if (StringUtils.isBlank(query) && null == query) {
			masterTypes = masterTypeRepo.findAllActiveTypes();
		} else {
			String searchQuery = new StringBuilder(StringConst.PERCENTILE).append(appUtil.sanatizeQuery(query))
					.append(StringConst.PERCENTILE).toString();

			masterTypes = masterTypeRepo.findAllActiveTypesSearch(searchQuery);
		}

		log.info("listTypes :: masterTypes " + masterTypes);

		List<MasterModel> masterModels = new ArrayList<>();
		if (!masterTypes.isEmpty()) {
			masterModels = masterTypes.stream().filter(predicate -> !predicate.isDeleted())
					.map(masterType -> masterBuilder.createMasterModel(masterType)).collect(Collectors.toList());
		}
		log.info("listTypes :: masterModels " + masterModels);

		return masterModels;
	}

	@Override
	@Transactional
	public List<MasterModel> listEtas(String query, JwtUser jwtUser) {
		log.info("listEtas");
		List<MasterEta> masterEtas = null;

		if (StringUtils.isBlank(query) && null == query) {
			masterEtas = masterEtaRepo.findAllActiveEtas();
		} else {
			String searchQuery = new StringBuilder(StringConst.PERCENTILE).append(appUtil.sanatizeQuery(query))
					.append(StringConst.PERCENTILE).toString();

			masterEtas = masterEtaRepo.findAllActiveEtasSearch(searchQuery);
		}

		log.info("listEtas :: masterEtas " + masterEtas);

		List<MasterModel> masterModels = new ArrayList<>();
		if (!masterEtas.isEmpty()) {
			masterModels = masterEtas.stream().filter(predicate -> !predicate.isDeleted())
					.map(masterEta -> masterBuilder.createMasterModel(masterEta)).collect(Collectors.toList());
		}
		log.info("listEtas :: masterModels " + masterModels);

		return masterModels;
	}

	@Override
	@Transactional
	public List<MasterModel> listPromoCodes(int dispatcherId, String query, JwtUser jwtUser) {
		log.info("listPromoCodes");
		List<PromoCode> promoCodes = null;

		if (StringUtils.isBlank(query) && null == query) {
			promoCodes = promoCodeRepo.findAllActivePromoCodesByDispatcherId(dispatcherId);
		} else {
			String searchQuery = new StringBuilder(StringConst.PERCENTILE).append(appUtil.sanatizeQuery(query))
					.append(StringConst.PERCENTILE).toString();

			promoCodes = promoCodeRepo.findAllActivePromoCodesByDispatcherIdSearch(dispatcherId, searchQuery);
		}

		log.info("listPromoCodes :: promoCodes " + promoCodes);

		List<MasterModel> masterModels = new ArrayList<>();
		if (!promoCodes.isEmpty()) {
			masterModels = promoCodes.stream().filter(predicate -> !predicate.isDeleted())
					.map(promoCode -> masterBuilder.createMasterModel(promoCode)).collect(Collectors.toList());
		}
		log.info("listPromoCodes :: masterModels " + masterModels);

		return masterModels;
	}

}
