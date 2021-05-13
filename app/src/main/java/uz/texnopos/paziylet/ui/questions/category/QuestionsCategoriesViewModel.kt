package uz.texnopos.paziylet.ui.questions.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.paziylet.core.Resource
import uz.texnopos.paziylet.data.model.QuestionCategories
import uz.texnopos.paziylet.data.firebase.FirebaseHelper

class QuestionsCategoriesViewModel(private val firebaseHelper: FirebaseHelper) : ViewModel() {

    private var _questionCategories: MutableLiveData<Resource<List<QuestionCategories>>> =
        MutableLiveData()
    val questionCategories: LiveData<Resource<List<QuestionCategories>>>
        get() = _questionCategories

    fun getAllQuestionCategories() {
        _questionCategories.value = Resource.loading()
        firebaseHelper.getQuestionCategories(
            {
                _questionCategories.value = Resource.success(it)
            }, {
                _questionCategories.value = Resource.error(it)
            }
        )
    }
}