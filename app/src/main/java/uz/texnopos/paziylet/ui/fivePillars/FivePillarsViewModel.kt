package uz.texnopos.paziylet.ui.fivePillars

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.paziylet.core.Resource
import uz.texnopos.paziylet.data.firebase.FirebaseHelper
import uz.texnopos.paziylet.data.model.FivePillars

class FivePillarsViewModel(private val firebaseHelper: FirebaseHelper) : ViewModel() {
    private var _pillars: MutableLiveData<Resource<List<FivePillars>>> = MutableLiveData()
    val pillars: LiveData<Resource<List<FivePillars>>> get() = _pillars

    fun getPillarsData(path:String) {
        _pillars.value = Resource.loading()
        firebaseHelper.getFivePillars( path, {
            _pillars.value = Resource.success(it)
        }, {
            _pillars.value = Resource.error(it)
        })
    }
}