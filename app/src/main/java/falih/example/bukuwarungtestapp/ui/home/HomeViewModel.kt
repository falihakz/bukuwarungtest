package falih.example.bukuwarungtestapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import falih.example.bukuwarungtestapp.common.SingleLiveEvent
import falih.example.bukuwarungtestapp.data.repository.UserRepository
import falih.example.bukuwarungtestapp.data.room.entity.Profile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _loadingProfileListState = MutableLiveData<Boolean>()
    val loadingProfileListState: LiveData<Boolean> = _loadingProfileListState
    val userList: LiveData<List<Profile>> = Transformations.map(userRepository.userList){
        it
    }
    val userFetchErrorEvent = userRepository.userFetchErrorEvent
    val showUserDetailEvent = SingleLiveEvent<Profile>()
    val listEmpty: LiveData<Boolean> = Transformations.map(userList){
        it.isNullOrEmpty() && mPage > 1
    }

    // for data filter and pagination
    var mPage = 1
    private set

    init {
        fetchUserListFromRemote()
    }

    fun fetchUserListFromRemote(){
        _loadingProfileListState.value = true
        println("get ready to call API...")
        CoroutineScope(IO).launch {
            val nextPage = userRepository.fetchUsersFromAPI(mPage)
            withContext(Main){
                mPage = nextPage
                _loadingProfileListState.value = false
            }
        }
    }
    //region databinding methods
    fun openUserDetailAt(position: Int){
        showUserDetailEvent.postValue(userList.value?.get(position))
    }

    fun getFirstNameAt(position: Int): String? {
        return userList.value?.get(position)?.first_name
    }

    fun getLastNameAt(position: Int): String? {
        return userList.value?.get(position)?.last_name
    }

    //endregion
    override fun onCleared() {
        CoroutineScope(IO).cancel()
        super.onCleared()
    }
}