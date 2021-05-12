package uz.texnopos.paziylet.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.texnopos.paziylet.firebase.FirebaseHelper
import uz.texnopos.paziylet.ui.namazwaqti.ApiInterface
import uz.texnopos.paziylet.ui.questions.category.QuestionsCategoriesAdapter
import uz.texnopos.paziylet.ui.questions.category.QuestionsCategoriesViewModel
import uz.texnopos.paziylet.ui.questions.question.QuestionAdapter
import uz.texnopos.paziylet.ui.questions.question.QuestionFragmentViewModel
import uz.texnopos.paziylet.ui.questions.category.QuestionsCategoriesFragmentViewModel
import java.util.concurrent.TimeUnit

private const val baseUrl: String = "http://api.paziylet.texnopos.uz/sample.php"

val firebaseModule = module {
    single { FirebaseFirestore.getInstance() }
    single { FirebaseStorage.getInstance() }
    single { FirebaseAuth.getInstance() }
    single { FirebaseHelper(androidApplication().applicationContext,get()) }
}
val remoteModule = module {
    single {
        GsonBuilder().setLenient().create()
    }
    single {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(25, TimeUnit.SECONDS)
                .readTimeout(25, TimeUnit.SECONDS)
                .writeTimeout(25, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .build()

    }
    single {
        Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create(get()))
                .client(get()).build()
    }
    single { get<Retrofit>().create(ApiInterface::class.java) }
}
val adapterModule = module {
    single { QuestionsCategoriesAdapter() }
    single { QuestionAdapter() }
}
val viewModelModule = module {
    viewModel { QuestionsCategoriesViewModel(get()) }
    viewModel { QuestionFragmentViewModel(get()) }
   }


