package uz.texnopos.paziylet.ui.namazwaqti

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.paziylet.di.Resource

class NamazwaqtiViewModel(private val helper:NetworkHelper):ViewModel() {

    private var _time:MutableLiveData<Resource<Time>> = MutableLiveData()
    val time:LiveData<Resource<Time>> get() = _time

    fun getTimes(){
        helper.getTimes({
            _time.value= Resource.success(it)
        },{
            _time.value= Resource.error(it)
        })
    }
}