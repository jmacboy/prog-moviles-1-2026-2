package com.example.practicaroom.repositories

import com.example.practicaroom.db.AppDatabase
import com.example.practicaroom.db.entities.Contact
import com.example.practicaroom.db.entities.ContactPhone
import com.example.practicaroom.db.entities.ContactWithPhones
import com.example.practicaroom.ui.vms.ContactUi
import javax.inject.Inject

class ContactRepository @Inject constructor(
    private val db: AppDatabase
) {
    suspend fun getContacts(): ArrayList<ContactUi> {
        val contacts = db
            .contactDao()
            .getAll()
            .map { it.toContactUi() }
        return ArrayList(contacts)
    }

    suspend fun getContactById(id: Int): ContactUi? {
        val contactWithPhones = db.contactDao().getById(id)
        return contactWithPhones?.toContactUi()
    }

    suspend fun searchContacts(query: String): ArrayList<ContactUi> {
        val contacts = db
            .contactDao()
            .searchByName("%$query%")
            .map { it.toContactUi() }
        return ArrayList(contacts)
    }

    suspend fun addContact(
        name: String,
        phoneNumbers: List<String>,
        email: String,
        address: String
    ) {
        val dao = db.contactDao()
        val cleanedPhones = phoneNumbers.filter(String::isNotBlank)
        if (cleanedPhones.isEmpty()) return
        val id = dao.insert(Contact(0, name, email = email, address = address)).toInt()
        val contactPhones = cleanedPhones.map { ContactPhone(contactId = id, number = it) }
        dao.insertPhones(contactPhones)
    }

    suspend fun updateContact(contact: ContactUi) {
        val dao = db.contactDao()
        val cleanedPhones = contact.phoneNumbers.filter(String::isNotBlank)
        if (cleanedPhones.isEmpty()) return
        dao.update(Contact(contact.id, contact.name, contact.email, contact.address))
        dao.deletePhones(contact.id)
        val contactPhones = cleanedPhones.map { phoneNumber ->
            ContactPhone(contactId = contact.id, number = phoneNumber)
        }
        dao.insertPhones(contactPhones)
    }

    suspend fun deleteContact(contact: ContactUi) {
        db
            .contactDao()
            .delete(Contact(contact.id, contact.name, contact.email, contact.address))
    }

    private fun ContactWithPhones.toContactUi() = ContactUi(
        id = contact.id,
        name = contact.name,
        email = contact.email,
        address = contact.address,
        phoneNumbers = phones.map(ContactPhone::number)
    )

}
