package uz.texnopos.paziylet.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import uz.texnopos.paziylet.firebase.FirebaseHelper
import uz.texnopos.paziylet.setting.Setting
import uz.texnopos.paziylet.ui.questions.category.QuestionsCategoriesAdapter
import uz.texnopos.paziylet.ui.questions.category.QuestionsCategoriesViewModel
import uz.texnopos.paziylet.ui.questions.myQuestions.MyQuestionsAdapter
import uz.texnopos.paziylet.ui.questions.myQuestions.MyQuestionsViewModel
import uz.texnopos.paziylet.ui.questions.question.QuestionAdapter
import uz.texnopos.paziylet.ui.questions.question.QuestionFragmentViewModel


val firebaseModule = module {
    single { FirebaseFirestore.getInstance() }
    single { FirebaseStorage.getInstance() }
    single { FirebaseAuth.getInstance() }
    single { FirebaseHelper(get()) }
}

val adapterModule = module {
    single { QuestionsCategoriesAdapter() }
    single { QuestionAdapter() }
    single { MyQuestionsAdapter() }
    single { Setting(androidApplication().applicationContext) }
}
val viewModelModule = module {
    viewModel { QuestionsCategoriesViewModel(get()) }
    viewModel { QuestionFragmentViewModel(get()) }
    viewModel { MyQuestionsViewModel(get()) }
   }


