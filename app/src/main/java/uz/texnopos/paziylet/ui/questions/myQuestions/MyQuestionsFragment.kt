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
import uz.texnopos.paziylet.core.ResourceState
import uz.texnopos.paziylet.core.extentions.addVertDivider
import uz.texnopos.paziylet.core.extentions.onClick
import uz.texnopos.paziylet.core.extentions.visibility
import uz.texnopos.paziylet.ui.mAuth.LoginActivity
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
            dialog.setTitle("Дыққат")
                .setMessage("Сораў бериў ушин дизимнен өтиң!")
                .setPositiveButton("Дизимнен өтиў"){_,_->
                    val intent=Intent(requireContext(),LoginActivity::class.java)
                    startActivity(intent)
                }
                .setNegativeButton("Анонимно сораў бериў"){_,_->
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