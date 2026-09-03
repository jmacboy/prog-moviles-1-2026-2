package com.example.practicalista2

class Contact(val name: String, val phoneNumber: String) {
    override fun toString(): String {
        return "$name - $phoneNumber"
    }
}