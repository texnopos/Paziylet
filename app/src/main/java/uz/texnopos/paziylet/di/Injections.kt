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
import uz.texnopos.paziylet.ui.categories.CategoryAdapter
import uz.texnopos.paziylet.ui.categories.CategoryViewModel
import uz.texnopos.paziylet.ui.categories.definitecategory.DefiniteCategoryAdapter
import uz.texnopos.paziylet.ui.categories.definitecategory.DefiniteCategoryViewModel
import uz.texnopos.paziylet.ui.news.NewsAdapter
import uz.texnopos.paziylet.ui.news.NewsViewModel
import uz.texnopos.paziylet.ui.praytime.PrayTimeViewModel
import uz.texnopos.paziylet.settings.Settings
import uz.texnopos.paziylet.ui.fivePillars.FivePillarsAdapter
import uz.texnopos.paziylet.ui.fivePillars.FivePillarsViewModel
import uz.texnopos.paziylet.ui.questions.category.QuestionsCategoriesAdapter
import uz.texnopos.paziylet.ui.questions.category.QuestionsCategoriesViewModel
import uz.texnopos.paziylet.ui.questions.myQuestions.MyQuestionsAdapter
import uz.texnopos.paziylet.ui.questions.myQuestions.MyQuestionsViewModel
import uz.texnopos.paziylet.ui.questions.question.QuestionAdapter
import uz.texnopos.paziylet.ui.questions.question.QuestionFragmentViewModel

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
    single { CategoryAdapter() }
    single { MyQuestionsAdapter(androidApplication().applicationContext) }
    single { Settings(androidApplication().applicationContext) }
    single { DefiniteCategoryAdapter() }
    single { NewsAdapter() }
    single { FivePillarsAdapter() }
}
val viewModelModule = module {
    viewModel { QuestionsCategoriesViewModel(get()) }
    viewModel { QuestionFragmentViewModel(get()) }
    viewModel { CategoryViewModel(get()) }
    viewModel { MyQuestionsViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { PrayTimeViewModel() }
    viewModel { DefiniteCategoryViewModel(get()) }
    viewModel { NewsViewModel(get()) }
    viewModel { FivePillarsViewModel(get()) }
}


