package com.example.smartdigitalbuspass.data

import android.content.Context
import android.content.SharedPreferences
import com.example.smartdigitalbuspass.data.models.UserRole

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "BusPassSession"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USER_ID = "userId"
        private const val KEY_USER_ROLE = "userRole"
        private const val KEY_USER_NAME = "userName"
    }

    fun saveSession(userId: Long, name: String, role: UserRole) {
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putLong(KEY_USER_ID, userId)
            putString(KEY_USER_NAME, name)
            putString(KEY_USER_ROLE, role.name)
            apply()
        }
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun getUserId(): Long = prefs.getLong(KEY_USER_ID, -1L)

    fun getUserRole(): UserRole {
        val roleStr = prefs.getString(KEY_USER_ROLE, UserRole.PASSENGER.name)
        return try {
            UserRole.valueOf(roleStr ?: UserRole.PASSENGER.name)
        } catch (e: Exception) {
            UserRole.PASSENGER
        }
    }

    fun getUserName(): String = prefs.getString(KEY_USER_NAME, "User") ?: "User"

    fun updateUserName(name: String) {
        prefs.edit().putString(KEY_USER_NAME, name).apply()
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
