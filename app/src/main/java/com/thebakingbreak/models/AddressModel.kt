package com.thebakingbreak.models

data class AddressModel(
    val name : String,
    val address : String,
    val city : String,
    val pincode : String,
    val state : String,
    val country : String,
    val phone : String,
    val uid : String,
    val createdAt : Long,
    val active: Boolean
)
