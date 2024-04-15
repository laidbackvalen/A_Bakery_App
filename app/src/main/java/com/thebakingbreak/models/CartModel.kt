package com.thebakingbreak.models

import java.io.Serializable


data class CartModel(
    val productName: String?,
    val imgUrl: String?,
    val price: String?,
    val countProduct: String,
    val maxPrice: String?,
    val qty: String?,
    val productId: String?,
    val vendorId: String?,
    val userId: String?,
    val CreatedAt: Long?,
) : Serializable