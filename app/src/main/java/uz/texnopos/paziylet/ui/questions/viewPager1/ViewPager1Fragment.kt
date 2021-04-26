package uz.texnopos.paziylet.ui.questions.viewPager1

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_view_pager1.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.core.extentions.visibility
import uz.texnopos.paziylet.di.ResourceState

class ViewPager1Fragment: Fragment(R.layout.fragment_view_pager1) {
        private val viewModel: ViewPagerViewModel by viewModel()
        private val adapter: ViewPager1Adapter by inject()
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recViewPager1.adapter = adapter
        viewModel.getAllQuestionCategories()
            setUpObserver()
    }
    private fun setUpObserver(){
        viewModel.questionCategories.observe(viewLifecycleOwner,{
            when(it.status){
                ResourceState.LOADING-> progressBarViewPager.visibility(true)
                ResourceState.SUCCESS->{
                    viewModel.questionCategories.observe(viewLifecycleOwner,{ i->
                    adapter.models = i.data!!
                    progressBarViewPager.visibility(false)
                    })
                }
                ResourceState.ERROR-> progressBarViewPager.visibility(false)
            }

        })
    }
}