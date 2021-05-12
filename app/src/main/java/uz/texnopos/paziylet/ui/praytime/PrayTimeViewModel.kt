package uz.texnopos.paziylet.ui.praytime

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.paziylet.core.Resource
import uz.texnopos.paziylet.data.model.PrayTime
import uz.texnopos.paziylet.data.retrofit.NetworkHelper

class PrayTimeViewModel(private val helper: NetworkHelper):ViewModel() {

    private var _prayTime:MutableLiveData<Resource<PrayTime>> = MutableLiveData()
    val prayTime:LiveData<Resource<PrayTime>> get() = _prayTime
    fun getTimes(latitude:Double,longitude:Double){
        helper.getTimes(latitude,longitude,{
            _prayTime.value= Resource.success(it)
        },{
            _prayTime.value= Resource.error(it)
        })
    }
}