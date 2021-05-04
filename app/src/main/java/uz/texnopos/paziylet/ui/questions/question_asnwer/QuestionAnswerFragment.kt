package uz.texnopos.paziylet.ui.questions.question_asnwer

import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_question_answer.*
import kotlinx.android.synthetic.main.toolbar.view.*
import uz.texnopos.paziylet.R

class QuestionAnswerFragment : Fragment(R.layout.fragment_question_answer) {
    private lateinit var navController: NavController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        val id = arguments?.getString("id")!!.toString()
        val question = arguments?.getString("question")!!.toString()
        val answer = arguments?.getString("answer")!!.toString()
        val categoryName = arguments?.getString("categoryName")!!.toString()
        tvQuestion.text = Html.fromHtml(question)
        tvAnswer.text = Html.fromHtml(answer)
        toolbar.ivCategoryName.text = categoryName
        toolbar.btnHome.setOnClickListener {
            navController.popBackStack()
        }
    }
}