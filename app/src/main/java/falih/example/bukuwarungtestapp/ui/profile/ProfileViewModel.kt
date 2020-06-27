package falih.example.bukuwarungtestapp.ui.profile

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import falih.example.bukuwarungtestapp.data.repository.SessionRepository

class ProfileViewModel(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val firstNameObserver = Observer<String> {
        sessionRepository.setFirstName(it)
    }
    private val lastNameObserver = Observer<String> {
        sessionRepository.setLastName(it)
    }
    private val emailObserver = Observer<String> {
        sessionRepository.setEmail(it)
    }

    val firstName = MutableLiveData<String>()
    val lastName = MutableLiveData<String>()
    val email = MutableLiveData<String>()

    init {
        firstName.value = sessionRepository.getFirstName()
        lastName.value = sessionRepository.getLastName()
        email.value = sessionRepository.getEmail()
        firstName.observeForever(firstNameObserver)
        lastName.observeForever(lastNameObserver)
        email.observeForever(emailObserver)
    }

    fun getUserAvatar(): String {
        return "https://image.shutterstock.com/image-vector/ui-image-placeholder-wireframes-apps-260nw-1037719204.jpg"
    }

    override fun onCleared() {
        super.onCleared()
        firstName.removeObserver(firstNameObserver)
        lastName.removeObserver(lastNameObserver)
        email.removeObserver(emailObserver)
    }
}