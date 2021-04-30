package uz.texnopos.paziylet.ui.questions.question

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.android.inject
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.ui.questions.category.QuestionsCategoriesAdapter

class QuestionFragment(): Fragment(R.layout.fragment_question) {
    lateinit var navController: NavController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)


    }
}