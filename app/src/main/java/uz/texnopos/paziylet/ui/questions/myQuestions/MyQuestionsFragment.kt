package uz.texnopos.paziylet.ui.questions.myQuestions

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_my_questions.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.core.ResourceState
import uz.texnopos.paziylet.core.extentions.onClick
import uz.texnopos.paziylet.core.extentions.toast
import uz.texnopos.paziylet.core.extentions.visibility
import uz.texnopos.paziylet.settings.Settings
import uz.texnopos.paziylet.ui.auth.LoginActivity

class MyQuestionsFragment : Fragment(R.layout.fragment_my_questions) {

    private val viewModel: MyQuestionsViewModel by viewModel()
    private val adapter: MyQuestionsAdapter by inject()
    private val settings: Settings by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = adapter
        swipeRefresh.setOnRefreshListener {
            viewModel.getAllMyQuestions()
            swipeRefresh.isRefreshing = false
        }
        if (settings.isAppFirstLaunched()) {
            val dialog = AlertDialog.Builder(requireContext())
            dialog.setTitle(getString(R.string.sign_in_dialog_title))
                .setMessage(getString(R.string.sign_in_dialog_message))
                .setPositiveButton(getString(R.string.sign_in_dialog_positive_button)) { _, _ ->
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                }
                .setNegativeButton(getString(R.string.sign_in_dialog_negative_button)) { dialog, _ ->
                    dialog.dismiss()
                }
            dialog.setCancelable(false).show()
        }
        adapter.setOnClickQuestion { q, a ->
            val bundle = bundleOf("question" to q, "answer" to a)
            Navigation.findNavController(view)
                .navigate(R.id.action_questionsFragment_to_questionAnswerFragment, bundle)
        }
        setUpObserver()
        viewModel.getAllMyQuestions()
        btnSend.onClick {
            if (etSoraw.text.isNotEmpty()) {
                val question = etSoraw.text.toString()
                viewModel.addQuestion(question)
                etSoraw.text.clear()
                viewModel.getAllMyQuestions()
            } else {
                etSoraw.error = getString(R.string.please_fill_all_the_fields)
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

        viewModel.addQuestion.observe(viewLifecycleOwner, {
            when (it.status) {
                ResourceState.LOADING -> progressBarQuestion.visibility(true)
                ResourceState.SUCCESS -> {
                    progressBarQuestion.visibility(false)
                    toast(getString(R.string.your_questions_sended))
                }
                ResourceState.ERROR -> {
                    progressBarQuestion.visibility(false)
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}