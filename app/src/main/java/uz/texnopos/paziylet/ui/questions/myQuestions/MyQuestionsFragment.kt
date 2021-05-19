package uz.texnopos.paziylet.ui.questions.myQuestions

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_my_questions.*
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.core.extentions.addVertDivider
import uz.texnopos.paziylet.core.extentions.onClick
import uz.texnopos.paziylet.core.extentions.visibility
import uz.texnopos.paziylet.di.ResourceState
import uz.texnopos.paziylet.setting.Setting
import uz.texnopos.paziylet.ui.auth.LoginActivity
import java.util.*

class MyQuestionsFragment: Fragment(R.layout.fragment_my_questions) {

    private val viewModel:MyQuestionsViewModel by viewModel()
    private val adapter:MyQuestionsAdapter by inject()
    private val settings: Setting by inject()
    private var userId=""
    private val mAuth:FirebaseAuth by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter=adapter
        if (settings.isAppFirstLaunched()){
            val dialog=AlertDialog.Builder(requireContext())
            dialog.setTitle(getString(R.string.sign_in_dialog_title))
                .setMessage(getString(R.string.sign_in_dialog_message))
                .setPositiveButton(getString(R.string.sign_in_dialog_positive_button)){ _, _->
                    val intent=Intent(requireContext(),LoginActivity::class.java)
                    startActivity(intent)
                }
                .setNegativeButton(getString(R.string.sign_in_dialog_negative_button)){ _, _->
                    userId= UUID.randomUUID().toString()
                }
            dialog.setCancelable(false).show()
        }else{
            userId=mAuth.currentUser!!.uid
        }
        recyclerView.addVertDivider(requireContext())
        setUpObserver()
        viewModel.getAllMyQuestions(userId)
        btnSend.onClick {
            if (etSoraw.text.isNotEmpty()){
                val question=etSoraw.text.toString()
                viewModel.addQuestion(question,userId)
                etSoraw.text.clear()
                viewModel.getAllMyQuestions(userId)
            }else{
                etSoraw.error=getString(R.string.please_fill_all_the_fields)
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