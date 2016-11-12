package com.lucene.models;

import java.io.Serializable;

public class Product implements Serializable, IProduct {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String category;
	private String subCategory;
	private String brand;
	private String supplier;
	private Integer minOrderQuantity;
	private Float targetBatchVolume;
	private Float volume;
	private Float sellPrice;
	private String sellCurrency;
	private Float buyPrice;
	private String buyCurrency;
	private Integer stockOnHand;
	private Integer stockOnOrder;

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", category=" + category + ", subCategory=" + subCategory + ", brand=" + brand + ", supplier="
				+ supplier + ", minOrderQuantity=" + minOrderQuantity + ", targetBatchVolume=" + targetBatchVolume + ", volume=" + volume
				+ ", sellPrice=" + sellPrice + ", sellCurrency=" + sellCurrency + ", buyPrice=" + buyPrice + ", buyCurrency=" + buyCurrency
				+ ", stockOnHand=" + stockOnHand + ", stockOnOrder=" + stockOnOrder + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public Integer getMinOrderQuantity() {
		return minOrderQuantity;
	}

	public void setMinOrderQuantity(Integer minOrderQuantity) {
		this.minOrderQuantity = minOrderQuantity;
	}

	public Float getTargetBatchVolume() {
		return targetBatchVolume;
	}

	public void setTargetBatchVolume(Float targetBatchVolume) {
		this.targetBatchVolume = targetBatchVolume;
	}

	public Float getVolume() {
		return volume;
	}

	public void setVolume(Float volume) {
		this.volume = volume;
	}

	public Float getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(Float sellPrice) {
		this.sellPrice = sellPrice;
	}

	public String getSellCurrency() {
		return sellCurrency;
	}

	public void setSellCurrency(String sellCurrency) {
		this.sellCurrency = sellCurrency;
	}

	public Float getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(Float buyPrice) {
		this.buyPrice = buyPrice;
	}

	public String getBuyCurrency() {
		return buyCurrency;
	}

	public void setBuyCurrency(String buyCurrency) {
		this.buyCurrency = buyCurrency;
	}

	public Integer getStockOnHand() {
		return stockOnHand;
	}

	public void setStockOnHand(Integer stockOnHand) {
		this.stockOnHand = stockOnHand;
	}

	public Integer getStockOnOrder() {
		return stockOnOrder;
	}

	public void setStockOnOrder(Integer stockOnOrder) {
		this.stockOnOrder = stockOnOrder;
	}

	@Override
	public void buildPropduct(IProduct product, String[] values) {

	}

}
