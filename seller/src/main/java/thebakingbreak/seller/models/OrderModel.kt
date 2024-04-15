package thebakingbreak.seller.models

import java.io.Serializable

data class OrderModel(
    val productName: String,
    val price: String,
    val maxPrice: String,
    val qtyBuy: String,
    val address: String,
    val phone: String,
    val email: String,
    val createdAt: Long,
    val paymentId: String,
    val paymentStatus: Boolean,
    val vendorId: String,
    val userId: String,
    val imageUrl: String,
    val payableAmt: String,
    val sellerName: String,
    val userName: String,
    val key: String
) : Serializable