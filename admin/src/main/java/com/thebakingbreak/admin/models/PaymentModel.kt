package com.thebakingbreak.admin.models

import java.io.Serializable

data class PaymentModel(
    val productName: String,
    val transactionId: String,
    val totalAmt: String,
    val status: Boolean,
    val productId: String,
    val userId: String,
    val vendorId: String,
): Serializable
