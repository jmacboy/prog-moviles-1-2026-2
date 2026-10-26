package com.example.practicaroom.db.entities

import androidx.room3.Embedded
import androidx.room3.Relation

data class ContactWithPhones(
    @Embedded val contact: Contact,
    @Relation(parentColumns = ["id"], entityColumns = ["contact_id"])
    val phones: List<ContactPhone>
)
