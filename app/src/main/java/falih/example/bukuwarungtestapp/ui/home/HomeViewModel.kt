package falih.example.bukuwarungtestapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import falih.example.bukuwarungtestapp.data.repository.ProfileRepository
import falih.example.bukuwarungtestapp.data.room.entity.Profile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _loadingProfileListEvent = MutableLiveData<Boolean>()
    val loadingProfileListEvent: LiveData<Boolean> = _loadingProfileListEvent
    private val _userList = profileRepository.userList
    val userList: LiveData<List<Profile>> = _userList
    val userFetchErrorSLE = profileRepository.userFetchErrorSLE

    // for data filter and pagination
    private var mPage = 1

    init {
        refreshUserListFromRemote()
    }

    fun refreshUserListFromRemote(){
        _loadingProfileListEvent.value = true
        println("get ready to call API...")
        CoroutineScope(IO).launch {
            val nextPage = profileRepository.fetchUsersFromAPI(mPage)
            withContext(Main){
                mPage = nextPage
                _loadingProfileListEvent.value = false
            }
        }
    }
    //region databinding methods
    fun openUserDetailAt(position: Int){

    }

    fun getFirstNameAt(position: Int): String? {
        return null
    }

    fun getLastNameAt(position: Int): String? {
        return null
    }

    //endregion
    override fun onCleared() {
        CoroutineScope(IO).cancel()
        super.onCleared()
    }
}