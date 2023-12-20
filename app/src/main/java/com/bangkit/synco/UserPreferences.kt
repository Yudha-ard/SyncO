package com.bangkit.synco

import android.content.Context
import android.content.SharedPreferences
import androidx.core.net.ParseException
import com.bangkit.synco.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UserPreferences(context: Context) {
    private val pref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private val _usrSessionFlow = MutableStateFlow(getLoginData())

    init {
        pref.registerOnSharedPreferenceChangeListener { _, key ->
            if (key in listOf(USER_ID_KEY,FIRST_NAME_KEY,LAST_NAME_KEY,DOB_KEY, HEIGHT_KEY,
                    WEIGHT_KEY ,TOKEN_KEY, STATE_KEY)) {
                _usrSessionFlow.value = getLoginData()
            }
        }
    }

    fun setUserLogin(user: User) {
        with(pref.edit()) {
            putString(USER_ID_KEY, user.userId)
            putString(FIRST_NAME_KEY, user.firstName)
            putString(LAST_NAME_KEY, user.lastName)
            putString(DOB_KEY, formatDate(user.dob))
            putString(LAST_NAME_KEY, user.lastName)
            putString(TOKEN_KEY, user.token)
            putBoolean(STATE_KEY, user.isLogin)
            apply()
        }
        _usrSessionFlow.value = user
    }

    fun updateUsername(firstName: String, lastName: String) {
        val currentUser = getLoginData()
        val updatedUser = currentUser.copy(firstName = firstName, lastName = lastName)

        with(pref.edit()) {
            putString(FIRST_NAME_KEY, updatedUser.firstName)
            putString(LAST_NAME_KEY, updatedUser.lastName)
            apply()
        }
        _usrSessionFlow.value = updatedUser
    }



    fun getUserEmail(): String {
        return pref.getString(USER_EMAIL_KEY, "") ?: ""
    }

    fun getFirstName(): String {
        return pref.getString(FIRST_NAME_KEY, "") ?: ""
    }
    fun getLastName(): String {
        return pref.getString(LAST_NAME_KEY, "") ?: ""
    }

    fun logout() {
        with(pref.edit()) {
            remove(FIRST_NAME_KEY)
            remove(LAST_NAME_KEY)
            remove(TOKEN_KEY)
            remove(USER_ID_KEY)
            putBoolean(STATE_KEY, false)
            apply()
        }
        _usrSessionFlow.value = User("","", "", null,0,0, "",false)
    }

    fun getLoginData(): User {
        val dobString = pref.getString(DOB_KEY, "") ?: ""
        val dob: Date? = if (dobString.isNotEmpty()) {
            try {
                SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dobString)
            } catch (e: ParseException) {
                null // Handle parsing error, you may want to log it or handle it differently
            }
        } else {
            null
        }
        return User(
            pref.getString(USER_ID_KEY, "") ?: "",
            pref.getString(FIRST_NAME_KEY, "") ?: "",
            pref.getString(LAST_NAME_KEY, "") ?: "",
            null,
            0,
            0,
            pref.getString(TOKEN_KEY, "") ?: "",
            pref.getBoolean(STATE_KEY, false)
        )
    }


    private fun parseDate(dateString: String): Date {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")  // Use the same date format as in getLoginData
        return dateFormat.parse(dateString) ?: Date()
    }

    private fun formatDate(date: Date?): String {
        return date?.let {
            SimpleDateFormat("yyyy-MM-dd", Locale.US).format(it)
        } ?: ""
    }
    companion object {
        private const val FIRST_NAME_KEY = "FIRST_NAME"
        private const val LAST_NAME_KEY = "LAST_NAME"
        private const val DOB_KEY = "DATEOFBIRTH"
        private const val WEIGHT_KEY = "WEIGHT"
        private const val HEIGHT_KEY = "HEIGHT"
        private const val TOKEN_KEY = "TOKEN"
        private const val USER_ID_KEY = "USER_ID"
        private const val STATE_KEY = "STATE"
        private const val PREF_NAME = "UserPrefs"
        private const val USER_EMAIL_KEY = "USER_EMAIL"
    }

}