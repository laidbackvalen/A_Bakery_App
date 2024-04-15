package com.thebakingbreak.admin.models

import java.io.Serializable

data class ProductModel(
    val name: String?,
    val shortDes: String,
    val category: String?,
    val price: String?,
    val maxPrice: String?,
    val qty: String?,
    val brandName: String,
    val imageUrl: String,
    val description: String,
    val uid: String?,
    val createdAt: Long?,
    val updated: Long?,
    val banner: Boolean?,
    val sellerName: String,
    val key: String
): Serializable