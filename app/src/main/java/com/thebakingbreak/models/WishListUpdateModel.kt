package com.thebakingbreak.models

import java.io.Serializable

data class WishListUpdateModel(
    val productName: String?,
    val imgUrl: String?,
    val price: String?,
    val maxPrice: String?,
    val productId: String?,
    val vendorId: String?,
    val userId: String?,
    val wishListId: String?,
    val CreatedAt: Long?
) : Serializable