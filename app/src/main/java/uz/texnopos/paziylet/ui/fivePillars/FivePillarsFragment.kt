package uz.texnopos.paziylet.ui.fivePillars

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.five_pillars_fragment.*
import kotlinx.android.synthetic.main.fragment_category.toolbar
import kotlinx.android.synthetic.main.toolbar.view.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.core.ResourceState
import uz.texnopos.paziylet.core.extentions.onClick
import uz.texnopos.paziylet.core.extentions.visibility
import uz.texnopos.paziylet.ui.fivePillars.post.FivePillarsWebViewFragmentArgs

class FivePillarsFragment : Fragment(R.layout.five_pillars_fragment) {
    private val adapter: FivePillarsAdapter by inject()
    private val safeArgs: FivePillarsFragmentArgs by navArgs()
    private lateinit var navController: NavController
    private val viewModel: FivePillarsViewModel by viewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        recyclerViewPillars.adapter = adapter
        when(safeArgs.text){
           "iyman" -> {toolbar.tvToolbarTitle.text = "Ийман"}
           "namaz" -> {toolbar.tvToolbarTitle.text = "Намаз"}
           "zakat" -> {toolbar.tvToolbarTitle.text = "Закат"}
           "oraza" -> {toolbar.tvToolbarTitle.text = "Ораза"}
           "xaj"   -> {toolbar.tvToolbarTitle.text = "Хаж"}
        }
        viewModel.getPillarsData(safeArgs.text)
        setUpObserver()
        toolbar.btnHome.onClick {
            navController.popBackStack()
        }
        adapter.onItemClickListener { text, title ->
            val action = FivePillarsFragmentDirections.actionFivePillarsFragmentToFivePillarsWebViewFragment(text, title)
            navController.navigate(action)
        }
    }

    private fun setUpObserver() {
        viewModel.pillars.observe(viewLifecycleOwner, {
            when (it.status) {
                ResourceState.LOADING -> progressBarPillars.visibility(true)
                ResourceState.SUCCESS -> {
                    it.data?.let { i ->
                        adapter.models = i
                    }
                    progressBarPillars.visibility(false)
                }
                ResourceState.ERROR -> progressBarPillars.visibility(false)
            }
        })
    }
}