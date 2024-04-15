package com.thebakingbreak.admin.models

import java.io.Serializable

data class CategoryModel(
    val category: String,
    val createdAt: Long,
    val updatedTo: Long,
    val key: String
) : Serializable