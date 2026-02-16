package com.org.verdaflow.rest.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.org.verdaflow.rest.entity.base.BaseEntity;

/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@Table(name = "products")
@NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p")
public class Product extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "name")
	private String name;

	@Column(name = "groupType")
	private int groupType;

	@Column(name = "image")
	private String image;

	@Column(name = "image_thumb")
	private String imageThumb;

	@Column(name = "description")
	private String description;

	@Column(name = "thc")
	private float thc;

	@Column(name = "cbd")
	private float cbd;

	@Column(name = "active")
	private boolean active = Boolean.TRUE;

	// bi-directional many-to-one association to UserDispatcherDetail
	@ManyToOne
	@JoinColumn(name = "dispatcher_id")
	private UserDispatcherDetail userDispatcherDetail;

	// bi-directional one-to-one association to MasterCategory
	@OneToOne
	@JoinColumn(name = "category_id")
	private MasterCategory masterCategory;

	// bi-directional one-to-one association to MasterType
	@OneToOne
	@JoinColumn(name = "type_id")
	private MasterType type;

	// bi-directional one-to-one association to PromoCode
	@OneToOne
	@JoinColumn(name = "promo_code_id")
	private PromoCode promoCode;

	// bi-directional one-to-many association to ProductEffectMapping
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
	@Where(clause = "is_deleted = 0")
	private List<ProductEffectMapping> productEffectMappings;

	// bi-directional one-to-many association to ProductPriceDetail
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
	@Where(clause = "is_deleted = 0")
	private List<ProductPriceDetail> productPriceDetails;

	public Product() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getGroupType() {
		return groupType;
	}

	public void setGroupType(int groupType) {
		this.groupType = groupType;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImageThumb() {
		return imageThumb;
	}

	public void setImageThumb(String imageThumb) {
		this.imageThumb = imageThumb;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getThc() {
		return thc;
	}

	public void setThc(float thc) {
		this.thc = thc;
	}

	public float getCbd() {
		return cbd;
	}

	public void setCbd(float cbd) {
		this.cbd = cbd;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public UserDispatcherDetail getUserDispatcherDetail() {
		return userDispatcherDetail;
	}

	public void setUserDispatcherDetail(UserDispatcherDetail userDispatcherDetail) {
		this.userDispatcherDetail = userDispatcherDetail;
	}

	public MasterCategory getMasterCategory() {
		return masterCategory;
	}

	public void setMasterCategory(MasterCategory masterCategory) {
		this.masterCategory = masterCategory;
	}

	public MasterType getType() {
		return type;
	}

	public void setType(MasterType type) {
		this.type = type;
	}

	public PromoCode getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(PromoCode promoCode) {
		this.promoCode = promoCode;
	}

	public List<ProductEffectMapping> getProductEffectMappings() {
		return productEffectMappings;
	}

	public void setProductEffectMappings(List<ProductEffectMapping> productEffectMappings) {
		this.productEffectMappings = productEffectMappings;
	}

	public List<ProductPriceDetail> getProductPriceDetails() {
		return productPriceDetails;
	}

	public void setProductPriceDetails(List<ProductPriceDetail> productPriceDetails) {
		this.productPriceDetails = productPriceDetails;
	}

}
