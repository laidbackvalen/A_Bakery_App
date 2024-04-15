package com.thebakingbreak.models

import java.io.Serializable

data class UserModel(
    val name: String,
    val phoneNumber: String,
    val email: String,
    val userType: String,
    val createdAt: Long,
    val updatedAt: Long,
) : Serializable
