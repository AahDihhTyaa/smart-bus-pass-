package com.example.smartdigitalbuspass.data.models

enum class UserRole {
    STUDENT,
    PASSENGER
}

data class User(
    val id: Long = 0,
    val name: String,
    val email: String,
    val phone: String,
    val role: UserRole,
    val collegeName: String? = null,
    val studentId: String? = null,
    val passwordHash: String = ""
)
