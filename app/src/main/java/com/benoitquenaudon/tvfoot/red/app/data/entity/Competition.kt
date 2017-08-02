package com.benoitquenaudon.tvfoot.red.app.data.entity

data class Competition(
    val code: String,
    val name: String,
    val country: String?,
    val url: String?,
    val gender: String?
)