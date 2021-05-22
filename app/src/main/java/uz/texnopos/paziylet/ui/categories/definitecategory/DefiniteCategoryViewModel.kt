package uz.texnopos.paziylet.ui.categories.definitecategory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.paziylet.core.Resource
import uz.texnopos.paziylet.data.firebase.FirebaseHelper
import uz.texnopos.paziylet.data.model.Patwa

class DefiniteCategoryViewModel(private val firebaseHelper: FirebaseHelper) : ViewModel() {
    private var _definiteCat: MutableLiveData<Resource<List<Patwa>>> = MutableLiveData()
    val definiteCat: LiveData<Resource<List<Patwa>>> get() = _definiteCat

    fun getData(path: String) {
        _definiteCat.value = Resource.loading()
        firebaseHelper.getData(path, {
            _definiteCat.value = Resource.success(it)
        }, {
            _definiteCat.value = Resource.error(it)
        })
    }
}