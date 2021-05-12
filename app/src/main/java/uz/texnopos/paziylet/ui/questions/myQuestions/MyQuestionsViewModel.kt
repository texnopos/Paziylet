package uz.texnopos.paziylet.ui.questions.myQuestions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.paziylet.data.model.Question
import uz.texnopos.paziylet.di.Resource
import uz.texnopos.paziylet.firebase.FirebaseHelper

class MyQuestionsViewModel(private val firebaseHelper:FirebaseHelper):ViewModel() {
    private val _myQuestion:MutableLiveData<Resource<List<Question>>> = MutableLiveData()
    val myQuestion: LiveData<Resource<List<Question>>> get() = _myQuestion

    fun getAllMyQuestions(id:String){
        _myQuestion.value=Resource.loading()
        firebaseHelper.getPrivateQuestion(id,{
            _myQuestion.value=Resource.success(it)
        },{
            _myQuestion.value=Resource.error(it)
        })
    }

    fun addQuestion(question:String){
        _myQuestion.value= Resource.loading()
        firebaseHelper.addQuestion(question,{
            _myQuestion.value= Resource.success(null)
        },{
            _myQuestion.value= Resource.error(it)
        })
    }
}