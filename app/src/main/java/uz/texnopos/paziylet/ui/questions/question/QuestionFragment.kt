package uz.texnopos.paziylet.ui.questions.question

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_question.*
import kotlinx.android.synthetic.main.toolbar.view.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.core.extentions.addVertDivider
import uz.texnopos.paziylet.core.extentions.visibility
import uz.texnopos.paziylet.di.ResourceState

class QuestionFragment : Fragment(R.layout.fragment_question) {
    private lateinit var navController: NavController
    private val viewModel: QuestionFragmentViewModel by viewModel()
    private val adapter: QuestionAdapter by inject()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        recViewQuestionFragment.adapter = adapter
        recViewQuestionFragment.addVertDivider(requireContext())
        setUpObserver()
        val categoryName = arguments?.getString("categoryName")!!.toString()
        adapter.setOnQuestionMoreClicked { i, q, a ->
            val bundle =
                bundleOf("id" to i, "question" to q, "answer" to a, "categoryName" to categoryName)
            Navigation.findNavController(view)
                .navigate(R.id.action_questionFragment_to_questionAnswerFragment, bundle)
        }
        toolbar.tvNameOfCategory.text = categoryName
        toolbar.ivQAnsBackspace.setOnClickListener {
            navController.popBackStack()
        }

        val categoryId = arguments?.getString("categoryId")!!.toString()
        if (categoryId.isNotEmpty()) viewModel.getAllQuestionByCategoryId(categoryId)
    }

    private fun setUpObserver() {
        viewModel.question.observe(viewLifecycleOwner, {
            when (it.status) {
                ResourceState.LOADING -> progressBarQuestion.visibility(true)
                ResourceState.SUCCESS -> {
                    viewModel.question.observe(viewLifecycleOwner, { i ->
                        adapter.models = i.data!!
                        progressBarQuestion.visibility(false)
                    })
                }
                ResourceState.ERROR -> progressBarQuestion.visibility(false)
            }

        })
    }
}