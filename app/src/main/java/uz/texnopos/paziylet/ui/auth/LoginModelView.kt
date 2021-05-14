package uz.texnopos.paziylet.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.PhoneAuthCredential
import uz.texnopos.paziylet.data.model.Question
import uz.texnopos.paziylet.di.Resource
import uz.texnopos.paziylet.firebase.FirebaseHelper

class LoginModelView(private val firebaseHelper: FirebaseHelper): ViewModel() {
    private var _registration: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val registration: LiveData<Resource<Boolean>>
        get() = _registration

    fun signIn(credential: PhoneAuthCredential){
        _registration.value = Resource.loading()
        firebaseHelper.auth( credential,
            {
            _registration.value = Resource.success(it)
            }
            ,
            {
                _registration.value = Resource.error(it)
            }
            )
    }

}