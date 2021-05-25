package uz.texnopos.paziylet.ui.categories.definitecategory

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_category.*
import kotlinx.android.synthetic.main.toolbar.view.*
import kotlinx.android.synthetic.main.toolbar.view.btnHome
import kotlinx.android.synthetic.main.toolbar.view.tvToolbarTitle
import kotlinx.android.synthetic.main.toolbar_for_backspace.view.*
import org.koin.android.ext.android.inject
import uz.texnopos.paziylet.R
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.paziylet.core.ResourceState
import uz.texnopos.paziylet.core.extentions.onClick
import uz.texnopos.paziylet.core.extentions.visibility

class DefiniteCategoryFragment : Fragment(R.layout.fragment_category) {

    private val viewModel: DefiniteCategoryViewModel by viewModel()
    private val adapter: DefiniteCategoryAdapter by inject()
    private val safeArgs: DefiniteCategoryFragmentArgs by navArgs()
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        recyclerViewCategory.adapter = adapter
        toolbar.tvToolbarTitle.text = safeArgs.path
        viewModel.getData(safeArgs.path)
        setUpObserver()
        toolbar.btnHome.onClick {
            navController.popBackStack()
        }
        adapter.onItemClickListener { text, title ->
            val action = DefiniteCategoryFragmentDirections.actionDefiniteCategoryToWebViewFragment(
                text,
                safeArgs.path,
                title
            )
            navController.navigate(action)
        }
    }

    private fun setUpObserver() {
        viewModel.definiteCat.observe(viewLifecycleOwner, {
            when (it.status) {
                ResourceState.LOADING -> progressBarCategory.visibility(true)
                ResourceState.SUCCESS -> {
                    it.data?.let { i ->
                        adapter.models = i
                    }
                    progressBarCategory.visibility(false)
                }
                ResourceState.ERROR -> progressBarCategory.visibility(false)
            }
        })
    }
}