package com.example.practicaroom.db.entities

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey
import androidx.room3.ColumnInfo

@Entity(
    tableName = "contact_phone",
    foreignKeys = [ForeignKey(entity = Contact::class, parentColumns = ["id"], childColumns = ["contact_id"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("contact_id")]
)
data class ContactPhone(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "contact_id") val contactId: Int,
    val number: String
)
