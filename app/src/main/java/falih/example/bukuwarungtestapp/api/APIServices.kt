package falih.example.bukuwarungtestapp.api

import falih.example.bukuwarungtestapp.common.API_USERS
import falih.example.bukuwarungtestapp.model.UserListResponse
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIServices {

    @GET(API_USERS)
    suspend fun getUserList(
        @Query("page") page: Int
    ): Response<UserListResponse>
}