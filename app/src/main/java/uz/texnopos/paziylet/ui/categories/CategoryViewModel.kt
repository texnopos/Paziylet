package uz.texnopos.paziylet.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.paziylet.core.Resource
import uz.texnopos.paziylet.data.firebase.FirebaseHelper
import uz.texnopos.paziylet.data.model.QuestionCategories

class CategoryViewModel(private val firebaseHelper: FirebaseHelper) : ViewModel() {

    private var _category: MutableLiveData<Resource<List<QuestionCategories>>> = MutableLiveData()
    val category: LiveData<Resource<List<QuestionCategories>>> get() = _category

    fun getCategories() {
        _category.value = Resource.loading()
        firebaseHelper.getCategories({
            _category.value = Resource.success(it)
        }, {
            _category.value = Resource.error(it)
        })
    }
}