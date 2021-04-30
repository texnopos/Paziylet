package uz.texnopos.paziylet.ui.questions.category

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import kotlinx.android.synthetic.main.fragment_questions_categories.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.core.extentions.visibility
import uz.texnopos.paziylet.di.ResourceState

class QuestionCategoriesFragment: Fragment(R.layout.fragment_questions_categories) {
    private val viewModel: QuestionsCategoriesFragmentViewModel by viewModel()
        private val adapter: QuestionsCategoriesAdapter by inject()
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            recViewQuestionsCategories.adapter = adapter
            viewModel.getAllQuestionCategories()
            setUpObserver()
            adapter.setOnQuestionCategoryItemClicked {
                Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
                findNavController(view).navigate(R.id.action_questionsFragment_to_questionFragment)
            }
    }
    private fun setUpObserver(){
        viewModel.questionCategories.observe(viewLifecycleOwner,{
            when(it.status){
                ResourceState.LOADING-> progressBarQuestionsCategories.visibility(true)
                ResourceState.SUCCESS->{
                    viewModel.questionCategories.observe(viewLifecycleOwner,{ i->
                    adapter.models = i.data!!
                    progressBarQuestionsCategories.visibility(false)
                    })
                }
                ResourceState.ERROR-> progressBarQuestionsCategories.visibility(false)
            }

        })
    }
}