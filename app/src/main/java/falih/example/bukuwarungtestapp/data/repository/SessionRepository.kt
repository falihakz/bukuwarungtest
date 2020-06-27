package falih.example.bukuwarungtestapp.data.repository

import android.content.SharedPreferences

class SessionRepository(
    private val sharedPreferences: SharedPreferences
){

    companion object {
        const val KEY_FIRST_NAME = "first_name"
        const val KEY_LAST_NAME = "last_name"
        const val KEY_EMAIL = "email"
        const val KEY_AVATAR_PATH = "avatar_path"
    }

    fun getFirstName(): String {
        val firstName = sharedPreferences.getString(KEY_FIRST_NAME, "") ?: ""
        return firstName
    }

    fun getLastName() = sharedPreferences.getString(KEY_LAST_NAME, "") ?: ""

    fun getEmail() = sharedPreferences.getString(KEY_EMAIL, "") ?: ""

    fun getAvatar() = sharedPreferences.getString(KEY_AVATAR_PATH, "") ?: ""

    fun setFirstName(firstName: String){
        sharedPreferences.edit().putString(KEY_FIRST_NAME, firstName).apply()
    }
    fun setLastName(lastName: String){
        sharedPreferences.edit().putString(KEY_LAST_NAME, lastName).apply()
    }
    fun setEmail(email: String){
        sharedPreferences.edit().putString(KEY_EMAIL, email).apply()
    }
    fun setAvatar(path: String){
        sharedPreferences.edit().putString(KEY_AVATAR_PATH, path).apply()
    }
}