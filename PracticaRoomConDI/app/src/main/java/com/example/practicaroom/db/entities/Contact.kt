package com.example.practicaroom.db.entities

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity
data class Contact(
    @PrimaryKey(
        autoGenerate = true
    )
    val id: Int,
    val name: String,
    val email: String = "",
    val address: String = ""
)
