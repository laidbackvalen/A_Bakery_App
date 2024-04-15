package com.thebakingbreak.models;

import java.io.Serializable;

public class ProductModel implements Serializable{
    String name;
    String shortDes;
    String category;
    String price;
    String maxPrice;
    String qty;
    String brandName;
    String imageUrl;
    String description;
    String uid;
    long createdAt;
    long updated;
    boolean banner;
    String key;
    String sellerName;
    String uidCus;

    public ProductModel(String name, String shortDes, String category, String price, String maxPrice,
                        String qty, String brandName, String imageUrl, String description,
                        String uid, long createdAt, long updated, boolean banner, String key, String sellerName,String uidCus) {
        this.name = name;
        this.shortDes = shortDes;
        this.category = category;
        this.price = price;
        this.maxPrice = maxPrice;
        this.qty = qty;
        this.brandName = brandName;
        this.imageUrl = imageUrl;
        this.description = description;
        this.uid = uid;
        this.createdAt = createdAt;
        this.updated = updated;
        this.banner = banner;
        this.key = key;
        this.sellerName = sellerName;
        this.uidCus = uidCus;
    }

    public ProductModel() {
    }

    public String getUidCus() {
        return uidCus;
    }

    public void setUidCus(String uidCus) {
        this.uidCus = uidCus;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDes() {
        return shortDes;
    }

    public void setShortDes(String shortDes) {
        this.shortDes = shortDes;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public boolean isBanner() {
        return banner;
    }

    public void setBanner(boolean banner) {
        this.banner = banner;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}