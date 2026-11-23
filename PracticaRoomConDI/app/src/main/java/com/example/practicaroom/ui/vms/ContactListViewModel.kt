package com.example.practicaroom.ui.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaroom.repositories.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactListViewModel  @Inject constructor(
    private val contactRepository: ContactRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ContactUiState())
    val uiState: StateFlow<ContactUiState> = _uiState.asStateFlow()

    fun loadContacts() {
        viewModelScope.launch {
            val searchText = _uiState.value.searchText
            val contacts = if (searchText.isNotBlank()) {
                contactRepository.searchContacts(searchText)
            } else {
                contactRepository.getContacts()
            }
            updateContactList(contacts)
        }
    }

    fun loadContactById(id: Int) {
        if (id == 0) {
            resetSelectedContact()
        } else {
            viewModelScope.launch {
                val contact = contactRepository.getContactById(id)
                if (contact != null) {
                    _uiState.update { currentState ->
                        currentState.copy(selectedContact = contact)
                    }
                } else {
                    resetSelectedContact()
                }
            }
        }
    }

    private fun updateContactList(contacts: List<ContactUi>) {
        val contactList = ArrayList(contacts)
        _uiState.update { currentState ->
            currentState.copy(contactList = contactList)
        }
    }

    fun resetSelectedContact() {
        _uiState.update {
            it.copy(
                selectedContact = ContactUi()
            )
        }
    }

    fun setSelectedContactName(name: String) {
        _uiState.update { currentState ->
            val selectedContact = currentState.selectedContact.copy(name = name)
            currentState.copy(selectedContact = selectedContact)
        }
    }

    fun setSelectedPhoneNumber(index: Int, phoneNumber: String) {
        _uiState.update { currentState ->
            if (index !in currentState.selectedContact.phoneNumbers.indices) {
                return@update currentState
            }

            val phoneNumbers = currentState.selectedContact.phoneNumbers.toMutableList()
            phoneNumbers[index] = phoneNumber
            val selectedContact = currentState.selectedContact.copy(phoneNumbers = phoneNumbers)
            currentState.copy(selectedContact = selectedContact)
        }
    }

    fun addPhoneNumber() {
        _uiState.update { currentState ->
            val phoneNumbers = currentState.selectedContact.phoneNumbers + ""
            val selectedContact = currentState.selectedContact.copy(phoneNumbers = phoneNumbers)
            currentState.copy(selectedContact = selectedContact)
        }
    }

    fun removePhoneNumber(index: Int) {
        _uiState.update { currentState ->
            if (index !in currentState.selectedContact.phoneNumbers.indices) {
                return@update currentState
            }

            val phoneNumbers = currentState.selectedContact.phoneNumbers.toMutableList()
            phoneNumbers.removeAt(index)
            val selectedContact = currentState.selectedContact.copy(phoneNumbers = phoneNumbers)
            currentState.copy(selectedContact = selectedContact)
        }
    }

    fun setSelectedContactEmail(email: String) {
        _uiState.update { currentState ->
            val selectedContact = currentState.selectedContact.copy(email = email)
            currentState.copy(selectedContact = selectedContact)
        }
    }

    fun setSelectedContactAddress(address: String) {
        _uiState.update { currentState ->
            val selectedContact = currentState.selectedContact.copy(address = address)
            currentState.copy(selectedContact = selectedContact)
        }
    }

    fun saveContact() {
        viewModelScope.launch {
            val contact = _uiState.value.selectedContact

            if (contact.name.isNotBlank() && contact.phoneNumbers.any(String::isNotBlank)) {
                if (contact.id == 0) {
                    contactRepository.addContact(
                        contact.name,
                        contact.phoneNumbers,
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

    fun deleteContact(contact: ContactUi) {
        viewModelScope.launch {
            contactRepository.deleteContact(contact)
            loadContacts()
        }
    }

    fun setSearchText(text: String) {
        _uiState.update { currentState ->
            currentState.copy(searchText = text)
        }
        loadContacts()
    }
}

data class ContactUiState(
    val contactList: ArrayList<ContactUi> = arrayListOf(),
    val selectedContact: ContactUi = ContactUi(),
    val searchText: String = ""
)
