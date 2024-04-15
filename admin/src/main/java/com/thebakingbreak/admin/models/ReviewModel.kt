package com.thebakingbreak.admin.models

data class ReviewModel(
    val ratingCount: Double = 0.0,
    val message: String,
    val createdAt: Long,
    val vendorId: String,
    val cuId: String,
    val productId: String,
)