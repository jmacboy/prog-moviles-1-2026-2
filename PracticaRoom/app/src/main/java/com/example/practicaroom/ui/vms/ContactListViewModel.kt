package com.example.practicaroom.ui.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaroom.db.entities.Contact
import com.example.practicaroom.repositories.ContactRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ContactListViewModel(
    private val contactRepository: ContactRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ContactUiState())
    val uiState: StateFlow<ContactUiState> = _uiState.asStateFlow()

    fun loadContacts() {
        viewModelScope.launch {
            val contacts = contactRepository.getContacts()
            _uiState.update {
                it.copy(contactList = ArrayList(contacts))
            }
        }
    }

    fun selectContact(selectedContact: Contact) {
        _uiState.update {
            it.copy(selectedContact = selectedContact)
        }
    }

    fun resetSelectedContact() {
        _uiState.update {
            it.copy(
                selectedContact = Contact(
                    id = 0,
                    name = "",
                    phoneNumber = "",
                    email = "",
                    address = ""
                )
            )
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
        viewModelScope.launch {
            val contact = _uiState.value.selectedContact

            if (contact.name.isNotBlank() && contact.phoneNumber.isNotBlank()) {
                if (contact.id == 0) {
                    contactRepository.addContact(
                        contact.name,
                        contact.phoneNumber,
                        contact.email,
                        contact.address
                    )
                } else {
                    contactRepository.updateContact(contact)
                }
                resetSelectedContact()
            }
            loadContacts()
        }
    }

    fun deleteContact(contact: Contact) {
        contactRepository.deleteContact(contact)
        loadContacts()
    }

    fun insertContact(name: String, phoneNumber: String) {
        viewModelScope.launch {
            contactRepository.addContact(name, phoneNumber, "", "")
            loadContacts()
        }
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