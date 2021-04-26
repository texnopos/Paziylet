package uz.texnopos.paziylet.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import uz.texnopos.paziylet.data.model.FirebaseHelper
import uz.texnopos.paziylet.ui.questions.ViewPagerAdapter
import uz.texnopos.paziylet.ui.questions.viewPager1.ViewPager1Adapter
import uz.texnopos.paziylet.ui.questions.viewPager1.ViewPagerViewModel

val firebaseModule = module {
    single { FirebaseFirestore.getInstance() }
    single { FirebaseStorage.getInstance() }
    single { FirebaseAuth.getInstance() }
    single { FirebaseHelper(androidApplication().applicationContext,get()) }
}

val adapterModule = module {
    single { ViewPagerAdapter(get(),get(),get()) }
    single { ViewPager1Adapter() }
}
val viewModelModule = module {
    viewModel { ViewPagerViewModel(get()) }
   }


