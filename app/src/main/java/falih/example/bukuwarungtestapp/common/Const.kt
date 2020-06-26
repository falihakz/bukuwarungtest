package falih.example.bukuwarungtestapp.common

import falih.example.bukuwarungtestapp.BuildConfig

// region API configs & endpoints
const val API_BASE_URL = "https://reqres.in/"
const val API_KEY = ""
const val API_TIMEOUT = 10L

const val API_USERS = "api/users"
// endregion

// region local database
const val DATABASE_NAME = BuildConfig.APPLICATION_ID + "_database"
const val DATABASE_VERSION = 1
// endregion