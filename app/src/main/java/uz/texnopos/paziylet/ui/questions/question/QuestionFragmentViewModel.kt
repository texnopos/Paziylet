package uz.texnopos.paziylet.ui.questions.question

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.paziylet.data.model.Question
import uz.texnopos.paziylet.core.Resource
import uz.texnopos.paziylet.data.firebase.FirebaseHelper

class QuestionFragmentViewModel(private val firebaseHelper: FirebaseHelper) : ViewModel() {
    private var _question: MutableLiveData<Resource<List<Question>>> = MutableLiveData()
    val question: LiveData<Resource<List<Question>>>
        get() = _question

    fun getAllQuestionByCategoryId(id: String) {
        _question.value = Resource.loading()
        firebaseHelper.getQuestions(id, {
            _question.value = Resource.success(it)
        },
            {
                _question.value = Resource.error(it)
            })
    }

}