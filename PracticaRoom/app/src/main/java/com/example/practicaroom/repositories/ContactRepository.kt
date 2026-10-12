package com.example.practicaroom.repositories

import android.content.Context
import com.example.practicaroom.db.AppDatabase
import com.example.practicaroom.db.entities.Contact

class ContactRepository(
    private val context: Context
) {
    suspend fun getContacts(): ArrayList<Contact> {
        return AppDatabase.getDatabase(context)
            .contactDao()
            .getAll() as ArrayList<Contact>
    }

    suspend fun addContact(name: String, phoneNumber: String, email: String, address: String) {
        AppDatabase.getDatabase(context)
            .contactDao()
            .insert(Contact(0, name, phoneNumber, email, address))
    }

    fun updateContact(contact: Contact) {
        //TODO: Cambiar para que se actualice en la base de datos
    }

    fun deleteContact(contact: Contact) {
        //TODO: Cambiar para que se elimine de la base de datos
    }

}