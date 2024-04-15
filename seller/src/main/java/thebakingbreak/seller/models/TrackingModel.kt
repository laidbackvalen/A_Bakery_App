package thebakingbreak.seller.models

import java.io.Serializable

data class TrackingModel(
    val productId: String,
    val userId: String,
    val VendorId: String,
    val orderStatus: Boolean,
    val paymentStatus: Boolean,
    val paymentId: String,
    val confirmStatus: Boolean,
    val completedStatus: Boolean,
): Serializable