package com.example.practicalistanavegacion.models

data class Contact(
    val id: Int,
    val name: String,
    val phoneNumber: String,
    val email: String = "",
    val address: String = ""
)