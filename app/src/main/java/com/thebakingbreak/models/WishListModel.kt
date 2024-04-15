package com.thebakingbreak.models

import java.io.Serializable

data class WishListModel(
    val productName: String?,
    val imgUrl: String?,
    val price: String?,
    val maxPrice: String?,
    val productId: String?,
    val vendorId: String?,
    val userId: String?,
    val CreatedAt: Long?
) : Serializable