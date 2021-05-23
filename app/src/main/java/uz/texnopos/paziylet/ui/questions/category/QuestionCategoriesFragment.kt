package uz.texnopos.paziylet.ui.questions.category

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import kotlinx.android.synthetic.main.fragment_questions_categories.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.core.ResourceState
import uz.texnopos.paziylet.core.extentions.visibility

class QuestionCategoriesFragment : Fragment(R.layout.fragment_questions_categories) {
    private val viewModel: QuestionsCategoriesViewModel by viewModel()
    private val adapter: QuestionsCategoriesAdapter by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getAllQuestionCategories()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recViewQuestionsCategories.adapter = adapter
        setUpObserver()
        adapter.setOnQuestionCategoryItemClicked { i, n ->
            val bundle = bundleOf("categoryId" to i, "categoryName" to n)
            findNavController(view).navigate(
                R.id.action_questionsFragment_to_questionFragment,
                bundle
            )
        }
    }

    private fun setUpObserver() {
        viewModel.questionCategories.observe(viewLifecycleOwner, {
            when (it.status) {
                ResourceState.LOADING -> progressBarCategory.visibility(true)
                ResourceState.SUCCESS -> {
                    adapter.models = it.data!!
                    progressBarCategory.visibility(false)
                }
                ResourceState.ERROR -> progressBarCategory.visibility(false)
            }

        })
    }
}