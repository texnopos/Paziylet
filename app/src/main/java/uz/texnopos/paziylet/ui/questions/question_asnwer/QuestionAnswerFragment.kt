package uz.texnopos.paziylet.ui.questions.question_asnwer

import android.os.Bundle
import android.view.View
import androidx.core.text.HtmlCompat
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
        val question = arguments?.getString("question")!!.toString()
        val answer = arguments?.getString("answer")!!.toString()
        tvQuestion.text = HtmlCompat.fromHtml(question, HtmlCompat.FROM_HTML_MODE_LEGACY)
        tvAnswer.text = HtmlCompat.fromHtml(answer,HtmlCompat.FROM_HTML_MODE_LEGACY)
        toolbar.tvToolbarTitle.text =getString(R.string.full_answer)
        toolbar.btnHome.setOnClickListener {
            navController.popBackStack()
        }
    }
}