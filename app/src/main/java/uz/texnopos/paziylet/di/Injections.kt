package uz.texnopos.paziylet.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import uz.texnopos.paziylet.data.firebase.FirebaseHelper
import uz.texnopos.paziylet.ui.auth.LoginViewModel
import uz.texnopos.paziylet.ui.praytime.PrayTimeViewModel
import uz.texnopos.paziylet.settings.Settings
import uz.texnopos.paziylet.ui.questions.category.QuestionsCategoriesAdapter
import uz.texnopos.paziylet.ui.questions.category.QuestionsCategoriesViewModel
import uz.texnopos.paziylet.ui.questions.myQuestions.MyQuestionsAdapter
import uz.texnopos.paziylet.ui.questions.myQuestions.MyQuestionsViewModel
import uz.texnopos.paziylet.ui.questions.question.QuestionAdapter
import uz.texnopos.paziylet.ui.questions.question.QuestionFragmentViewModel

private const val baseUrl: String = "http://api.paziylet.texnopos.uz/"

val sharedPreferencesModule = module {
    single {
        androidApplication().applicationContext.getSharedPreferences(
            "uz.texnopos.paziylet-uz.preferences",
            Context.MODE_PRIVATE
        )
    }
}


val firebaseModule = module {
    single { FirebaseFirestore.getInstance() }
    single { FirebaseStorage.getInstance() }
    single { FirebaseAuth.getInstance() }
    single { FirebaseHelper(
            get(),
            get()
        )
    }
}

val adapterModule = module {
    single { QuestionsCategoriesAdapter() }
    single { QuestionAdapter() }
    single { MyQuestionsAdapter() }
    single { Settings(androidApplication().applicationContext) }
}
val viewModelModule = module {
    viewModel { QuestionsCategoriesViewModel(get()) }
    viewModel { QuestionFragmentViewModel(get()) }
    viewModel { MyQuestionsViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { PrayTimeViewModel() }
}


