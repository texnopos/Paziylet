package uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.paziylet.core.Resource
import uz.texnopos.paziylet.data.firebase.FirebaseHelper

class MediaViewModel(private val firebaseHelper: FirebaseHelper): ViewModel() {
    private var _video:MutableLiveData<Resource<List<VideoModel>>> = MutableLiveData()
    val video:LiveData<Resource<List<VideoModel>>> get() = _video

    fun getVideos(){
        _video.value= Resource.loading()
        firebaseHelper.getVideos({
            _video.value= Resource.success(it)
        },{
            _video.value=Resource.error(it)
        })
    }
}