package uz.texnopos.paziylet.ui.questions.myQuestions

import android.os.Bundle
import android.view.View
import android.widget.Toast
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_my_questions.*
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.core.extentions.addVertDivider
import uz.texnopos.paziylet.core.extentions.onClick
import uz.texnopos.paziylet.core.extentions.visibility
import uz.texnopos.paziylet.di.ResourceState

class MyQuestionsFragment: Fragment(R.layout.fragment_my_questions) {

    private val viewModel:MyQuestionsViewModel by viewModel()
    private val adapter:MyQuestionsAdapter by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recViewPager2.adapter=adapter
        recViewPager2.addVertDivider(requireContext())
        setUpObserver()
        viewModel.getAllMyQuestions("5560563b-1089-4afb-9439-e67598892c6b")
        btnSend.onClick {
            if (etSoraw.text.isNotEmpty()){
                val question=etSoraw.text.toString()
                viewModel.addQuestion(question)
            }else{
                etSoraw.error="please fill the field"
            }
        }
    }

    private fun setUpObserver() {
        viewModel.myQuestion.observe(viewLifecycleOwner, {
            when (it.status) {
                ResourceState.LOADING -> progressBarQuestion.visibility(true)
                ResourceState.SUCCESS -> {
                    progressBarQuestion.visibility(false)
                    it.data?.let { data ->
                        adapter.models = data
                    }
                }
                ResourceState.ERROR -> progressBarQuestion.visibility(false)
            }
        })
    }
}