package uz.texnopos.paziylet.ui.questions.myQuestions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.paziylet.core.Resource
import uz.texnopos.paziylet.data.firebase.FirebaseHelper
import uz.texnopos.paziylet.data.model.Question

class MyQuestionsViewModel(private val firebaseHelper: FirebaseHelper) : ViewModel() {
    private val _myQuestion: MutableLiveData<Resource<List<Question>>> = MutableLiveData()
    val myQuestion: LiveData<Resource<List<Question>>> get() = _myQuestion

    private val _addQuestion: MutableLiveData<Resource<String>> = MutableLiveData()
    val addQuestion: LiveData<Resource<String>> get() = _addQuestion

    fun getAllMyQuestions() {
        if (firebaseHelper.userId.isNotEmpty()){
            _myQuestion.value = Resource.loading()
            firebaseHelper.getPrivateQuestion({
                _myQuestion.value = Resource.success(it)
            }, {
                _myQuestion.value = Resource.error(it)
            })
        }
    }

    fun addQuestion(question: String) {
        _addQuestion.value = Resource.loading()
        firebaseHelper.addQuestion(question,
            {
                _addQuestion.value = Resource.success(it)
            },
            {
                _addQuestion.value = Resource.error(it)
            })
    }
}