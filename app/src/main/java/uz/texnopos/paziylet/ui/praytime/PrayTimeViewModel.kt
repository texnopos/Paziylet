package uz.texnopos.paziylet.ui.praytime

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.paziylet.data.model.PrayTimeModel

class PrayTimeViewModel : ViewModel() {

    private var _prayTime: MutableLiveData<PrayTimeModel> = MutableLiveData()
    val prayTime: LiveData<PrayTimeModel> get() = _prayTime

    fun getTimes(timezone: Int, latitude: Double, longitude: Double) {
        _prayTime.value = PrayTime.getPrayTime(latitude, longitude, timezone)
    }
}