package uz.texnopos.paziylet.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import uz.texnopos.paziylet.firebase.FirebaseHelper
import uz.texnopos.paziylet.ui.questions.category.QuestionsCategoriesAdapter
import uz.texnopos.paziylet.ui.questions.category.QuestionsCategoriesFragmentViewModel
import uz.texnopos.paziylet.ui.questions.question.QuestionFragmentViewModel

val firebaseModule = module {
    single { FirebaseFirestore.getInstance() }
    single { FirebaseStorage.getInstance() }
    single { FirebaseAuth.getInstance() }
    single { FirebaseHelper(androidApplication().applicationContext,get()) }
}

val adapterModule = module {
    single { QuestionsCategoriesAdapter() }
}
val viewModelModule = module {
    viewModel { QuestionsCategoriesFragmentViewModel(get()) }
    viewModel { QuestionFragmentViewModel(get()) }
   }


