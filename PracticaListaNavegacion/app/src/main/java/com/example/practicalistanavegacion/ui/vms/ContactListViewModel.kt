package com.example.practicalistanavegacion.ui.vms

import androidx.lifecycle.ViewModel
import com.example.practicalistanavegacion.models.Contact
import com.example.practicalistanavegacion.repositories.ContactRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ContactListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ContactUiState())
    val uiState: StateFlow<ContactUiState> = _uiState.asStateFlow()

    fun loadContacts() {
        val contacts = ContactRepository.getContacts()
        _uiState.update {
            it.copy(contactList = ArrayList(contacts))
        }
    }

    fun selectContact(selectedContact: Contact) {
        _uiState.update {
            it.copy(selectedContact = selectedContact)
        }
    }

    fun resetSelectedContact() {
        _uiState.update {
            it.copy(selectedContact = Contact(id = 0, name = "", phoneNumber = "", email = "", address = ""))
        }
    }

    fun setSelectedContactName(name: String) {
        _uiState.update {
            it.copy(selectedContact = it.selectedContact.copy(name = name))
        }
    }

    fun setSelectedPhoneNumber(phoneNumber: String) {
        _uiState.update {
            it.copy(selectedContact = it.selectedContact.copy(phoneNumber = phoneNumber))
        }
    }

    fun setSelectedContactEmail(email: String) {
        _uiState.update {
            it.copy(selectedContact = it.selectedContact.copy(email = email))
        }
    }

    fun setSelectedContactAddress(address: String) {
        _uiState.update {
            it.copy(selectedContact = it.selectedContact.copy(address = address))
        }
    }

    fun saveContact() {
        val contact = _uiState.value.selectedContact

        if (contact.name.isNotBlank() && contact.phoneNumber.isNotBlank()) {
            if (contact.id == 0) {
                ContactRepository.addContact(contact.name, contact.phoneNumber, contact.email, contact.address)
            } else {
                ContactRepository.updateContact(contact)
            }
            resetSelectedContact()
        }
        loadContacts()
    }
    fun deleteContact(contact: Contact) {
        ContactRepository.deleteContact(contact)
        loadContacts()
    }
}

data class ContactUiState(
    val contactList: ArrayList<Contact> = arrayListOf(),
    val selectedContact: Contact = Contact(
        id = 0,
        name = "",
        phoneNumber = "",
        email = "",
        address = ""
    )
)