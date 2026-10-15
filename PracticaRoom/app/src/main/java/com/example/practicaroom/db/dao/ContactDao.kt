package com.example.practicaroom.db.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.practicaroom.db.entities.Contact

@Dao
interface ContactDao {
    @Query("SELECT * FROM contact")
    suspend fun getAll(): List<Contact>

    @Query("SELECT * FROM contact WHERE id = :id")
    suspend fun getById(id: Int): Contact?

    @Query("SELECT * FROM contact WHERE name LIKE :name")
    suspend fun searchByName(name: String): List<Contact>

    @Insert
    suspend fun insert(contact: Contact): Long //devuelve el id del contacto insertado

    @Update
    suspend fun update(contact: Contact): Int //devuelve el número de filas afectadas

    @Delete
    suspend fun delete(contact: Contact): Int //devuelve el número de filas afectadas
}