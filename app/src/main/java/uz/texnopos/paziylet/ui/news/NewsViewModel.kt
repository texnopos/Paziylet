package uz.texnopos.paziylet.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.paziylet.core.Resource
import uz.texnopos.paziylet.data.firebase.FirebaseHelper
import uz.texnopos.paziylet.data.model.News

class NewsViewModel(private val firebaseHelper: FirebaseHelper) : ViewModel() {
    private var _news: MutableLiveData<Resource<List<News>>> = MutableLiveData()
    val news: LiveData<Resource<List<News>>>
        get() = _news

    fun getAllNews() {
        _news.value = Resource.loading()
        firebaseHelper.getNews({
            _news.value = Resource.success(it)
        },
            {
                _news.value = Resource.error(it)
            }
        )
    }

}