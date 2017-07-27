package com.benoitquenaudon.tvfoot.red.app.domain.libraries

data class Library(
    val name: String,
    val description: String,
    val link: String,
    val imageUrl: String,
    val circleCrop: Boolean
)