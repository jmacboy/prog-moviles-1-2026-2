package com.example.practicaroom.db.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import androidx.room3.Transaction
import com.example.practicaroom.db.entities.ContactPhone
import com.example.practicaroom.db.entities.Contact
import com.example.practicaroom.db.entities.ContactWithPhones

@Dao
interface ContactDao {
    @Transaction
    @Query("SELECT * FROM contact")
    suspend fun getAll(): List<ContactWithPhones>

    @Transaction
    @Query("SELECT * FROM contact WHERE id = :id")
    suspend fun getById(id: Int): ContactWithPhones?

    @Transaction
    @Query("SELECT * FROM contact WHERE name LIKE :name")
    suspend fun searchByName(name: String): List<ContactWithPhones>

    @Insert
    suspend fun insert(contact: Contact): Long //devuelve el id del contacto insertado

    @Update
    suspend fun update(contact: Contact): Int //devuelve el número de filas afectadas

    @Delete
    suspend fun delete(contact: Contact): Int //devuelve el número de filas afectadas

    @Insert
    suspend fun insertPhones(phones: List<ContactPhone>)

    @Query("DELETE FROM contact_phone WHERE contact_id = :contactId")
    suspend fun deletePhones(contactId: Int)
}
