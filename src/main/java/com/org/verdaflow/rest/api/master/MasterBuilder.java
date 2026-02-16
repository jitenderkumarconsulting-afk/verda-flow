package com.org.verdaflow.rest.api.master;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.org.verdaflow.rest.api.master.model.MasterModel;
import com.org.verdaflow.rest.entity.MasterCategory;
import com.org.verdaflow.rest.entity.MasterEffect;
import com.org.verdaflow.rest.entity.MasterEta;
import com.org.verdaflow.rest.entity.MasterType;
import com.org.verdaflow.rest.entity.PromoCode;

@Component
public class MasterBuilder {
	public static final Logger log = LoggerFactory.getLogger(MasterBuilder.class);

	@Transactional
	public MasterModel createMasterModel(MasterEffect masterEffect) {
		log.info("createMasterModel");

		return new MasterModel(masterEffect.getId(), masterEffect.getName());
	}

	@Transactional
	public MasterModel createMasterModel(MasterCategory masterCategory) {
		log.info("createMasterModel");

		return new MasterModel(masterCategory.getId(), masterCategory.getName());
	}

	@Transactional
	public MasterModel createMasterModel(MasterType masterType) {
		log.info("createMasterModel");

		return new MasterModel(masterType.getId(), masterType.getName());
	}

	@Transactional
	public MasterModel createMasterModel(MasterEta masterEta) {
		log.info("createMasterModel");

		return new MasterModel(masterEta.getId(), masterEta.getName());
	}

	@Transactional
	public MasterModel createMasterModel(PromoCode promoCode) {
		log.info("createMasterModel");

		return new MasterModel(promoCode.getId(), promoCode.getName());
	}

}
