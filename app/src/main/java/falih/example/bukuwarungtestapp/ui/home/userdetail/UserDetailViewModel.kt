package falih.example.bukuwarungtestapp.ui.home.userdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import falih.example.bukuwarungtestapp.data.room.entity.Profile

class UserDetailViewModel: ViewModel() {

    private val _profile = MutableLiveData<Profile>()
    val firstName: LiveData<String> = Transformations.map(_profile){
        it.first_name
    }
    val lastName: LiveData<String> = Transformations.map(_profile){
        it.last_name
    }
    val email: LiveData<String> = Transformations.map(_profile){
        it.email
    }

    fun setProfile(profile: Profile?){
        profile?.let { _profile.value = it }
    }

    fun getUserAvatar(): String {
        return _profile.value?.avatar ?: ""
    }
}