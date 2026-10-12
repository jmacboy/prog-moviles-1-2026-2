package com.example.practicalistanavegacion.repositories

import com.example.practicalistanavegacion.models.Contact

object ContactRepository {
    private val contacts: ArrayList<Contact> = arrayListOf(
        Contact(1, "John Doe", "123-456-7890"),
        Contact(2, "Jane Smith", "987-654-3210"),
        Contact(3, "Alice Johnson", "555-123-4567"),
        Contact(4, "Bob Brown", "111-222-3333"),
        Contact(5, "Charlie Davis", "444-555-6666"),
        Contact(6, "David Evans", "777-888-9999"),
        Contact(7, "Eve Foster", "000-111-2222"),
        Contact(8, "Frank Green", "333-444-5555"),
        Contact(9, "Grace Harris", "666-777-8888"),
        Contact(10, "Hannah Ingram", "999-000-1111"),
        Contact(11, "Ian Jackson", "222-333-4444"),
        Contact(12, "Jackie King", "555-666-7777"),
        Contact(13, "Kevin Lee", "888-999-0000"),
        Contact(14, "Laura Martinez", "111-222-3333"),
        Contact(15, "Mike Nelson", "444-555-6666"),
    )

    fun getContacts(): ArrayList<Contact> {
        return contacts
    }

    fun addContact(name: String, phoneNumber: String, email: String, address: String) {
        contacts.add(Contact(contacts.size + 1, name, phoneNumber, email, address))
    }

    fun updateContact(contact: Contact) {
        val index = contacts.indexOfFirst { it.id == contact.id }
        if (index != -1) {
            contacts[index] = contact
        }
    }

    fun deleteContact(contact: Contact) {
        contacts.removeIf { it.id == contact.id }
    }

}