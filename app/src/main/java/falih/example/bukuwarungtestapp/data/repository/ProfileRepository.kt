package falih.example.bukuwarungtestapp.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import falih.example.bukuwarungtestapp.api.APIServices
import falih.example.bukuwarungtestapp.common.SingleLiveEvent
import falih.example.bukuwarungtestapp.data.room.AppDatabase
import falih.example.bukuwarungtestapp.data.room.dao.ProfileDao
import falih.example.bukuwarungtestapp.data.room.entity.Profile
import falih.example.bukuwarungtestapp.model.UserListResponse
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.withContext
import retrofit2.Response

class ProfileRepository(
    context: Context,
    private val apiServices: APIServices
) {
    private val profileDao: ProfileDao
    val userList: LiveData<List<Profile>>
    val userFetchErrorSLE = SingleLiveEvent<String>()

    init {
        val db = AppDatabase.getInstance(context)
        profileDao = db!!.profileDao()
        userList = profileDao.getAll()
    }

    suspend fun fetchUsersFromAPI(page: Int): Int {
        println("calling API...")
        val response: Response<UserListResponse> = apiServices.getUserList(page)
        return if (response.isSuccessful){
            println("API successfully called")
            insertAll(response.body()?.data)
            if (response.body()?.data?.isNotEmpty() == true)
                page + 1
            else page
        } else {
            withContext(Main){
                userFetchErrorSLE.call()
            }
            page
        }
    }


    // You must call this on a non-UI thread or your app will crash.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    private fun insertAll(articles: List<Profile>?) {
        articles?.let {
            profileDao.insert(*it.toTypedArray())
        }
    }
}