package uz.texnopos.paziylet.ui.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_category.*
import uz.texnopos.paziylet.R
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.paziylet.core.ResourceState
import uz.texnopos.paziylet.core.extentions.visibility

class CategoryFragment:Fragment(R.layout.fragment_category) {

    private val viewModel:CategoryViewModel by viewModel()
    private val adapter:CategoryAdapter by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewCategory.adapter=adapter
        viewModel.getCategories()
        setUpObserver()
    }

    private fun setUpObserver() {
        viewModel.category.observe(viewLifecycleOwner,{
            when(it.status){
                ResourceState.LOADING-> progressBarCategory.visibility(true)
                ResourceState.SUCCESS->{
                    progressBarCategory.visibility(false)
                    viewModel.category.observe(viewLifecycleOwner,{d->
                        d.data?.let { i->
                            adapter.models=i
                        }
                    })
                }
                ResourceState.ERROR-> progressBarCategory.visibility(false)
            }
        })
    }
}