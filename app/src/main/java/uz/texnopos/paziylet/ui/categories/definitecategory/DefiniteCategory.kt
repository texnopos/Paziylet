package uz.texnopos.paziylet.ui.categories.definitecategory

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_category.*
import org.koin.android.ext.android.inject
import uz.texnopos.paziylet.R
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.paziylet.core.ResourceState
import uz.texnopos.paziylet.core.extentions.visibility

class DefiniteCategory:Fragment(R.layout.fragment_category) {

    private val viewModel:DefiniteCategoryViewModel by viewModel()
    private val adapter:DefiniteCategoryAdapter by inject()
    private val safeArgs:DefiniteCategoryArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewCategory.adapter=adapter
        tvTitle.text=safeArgs.path
        viewModel.getData(safeArgs.path)
        setUpObserver()
    }

    private fun setUpObserver() {
        viewModel.definiteCat.observe(viewLifecycleOwner,{
            when(it.status){
                ResourceState.LOADING->progressBarCategory.visibility(true)
                ResourceState.SUCCESS->{
                    viewModel.definiteCat.observe(viewLifecycleOwner,{d->
                        d.data?.let { i->
                            adapter.models=i
                        }
                    })
                    progressBarCategory.visibility(false)
                }
                ResourceState.ERROR-> progressBarCategory.visibility(false)
            }
        })
    }
}