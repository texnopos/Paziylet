package uz.texnopos.paziylet.ui.questions

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_questions.*
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.ui.questions.category.QuestionCategoriesFragment
import uz.texnopos.paziylet.ui.questions.myQuestions.MyQuestionsFragment

class QuestionsFragment: Fragment(R.layout.fragment_questions) {
    private lateinit var navController: NavController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        val fragmentList = arrayListOf(
            QuestionCategoriesFragment(),
            MyQuestionsFragment()
        )
        val adapter= QuestionsAdapter(fragmentList,requireActivity().supportFragmentManager,lifecycle)
        viewPager.adapter=adapter
        val tabLayoutMediator =
            TabLayoutMediator(tabs,viewPager
            ) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.questions_answers)
                    1 -> tab.text = getString(R.string.my_questions)
                }
            }
        tabLayoutMediator.attach()

    }

    }