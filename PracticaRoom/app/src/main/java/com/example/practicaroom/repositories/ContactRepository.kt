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

    suspend fun searchContacts(query: String): ArrayList<Contact> {
        return AppDatabase.getDatabase(context)
            .contactDao()
            .searchByName("%$query%") as ArrayList<Contact>
    }

    suspend fun addContact(name: String, phoneNumber: String, email: String, address: String) {
        AppDatabase.getDatabase(context)
            .contactDao()
            .insert(Contact(0, name, phoneNumber, email, address))
    }

    suspend fun updateContact(contact: Contact) {
        AppDatabase.getDatabase(context)
            .contactDao()
            .update(contact)
    }

    suspend fun deleteContact(contact: Contact) {
        AppDatabase.getDatabase(context)
            .contactDao()
            .delete(contact)
    }

}