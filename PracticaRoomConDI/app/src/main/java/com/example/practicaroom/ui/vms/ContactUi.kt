package com.example.practicaroom.ui.vms

data class ContactUi(
    val id: Int = 0,
    val name: String = "",
    val email: String = "",
    val address: String = "",
    val phoneNumbers: List<String> = listOf("")
)
