package uz.texnopos.paziylet.ui.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_category.*
import kotlinx.android.synthetic.main.toolbar.view.*
import kotlinx.android.synthetic.main.toolbar.view.btnHome
import kotlinx.android.synthetic.main.toolbar.view.tvToolbarTitle
import kotlinx.android.synthetic.main.toolbar_for_backspace.view.*
import uz.texnopos.paziylet.R
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.paziylet.core.ResourceState
import uz.texnopos.paziylet.core.extentions.visibility
import java.io.IOException

class CategoryFragment : Fragment(R.layout.fragment_category) {

    private lateinit var navController: NavController
    private val viewModel: CategoryViewModel by viewModel()
    private val adapter: CategoryAdapter by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        recyclerViewCategory.adapter = adapter
        viewModel.getCategories()
        toolbar.btnHome.visibility(false)
        toolbar.tvToolbarTitle.text = getString(R.string.categories)
        adapter.onItemClickListener {
            val action = CategoryFragmentDirections.actionCategoryFragmentToDefiniteCategory(it)
            navController.navigate(action)
        }
        setUpObserver()
    }

    private fun setUpObserver() {
        viewModel.category.observe(viewLifecycleOwner, {
            when (it.status) {
                ResourceState.LOADING -> progressBarCategory.visibility(true)
                ResourceState.SUCCESS -> {
                    progressBarCategory.visibility(false)
                    it.data?.let { i ->
                        adapter.models = i
                    }
                }
                ResourceState.ERROR -> progressBarCategory.visibility(false)
            }
        })
    }
}