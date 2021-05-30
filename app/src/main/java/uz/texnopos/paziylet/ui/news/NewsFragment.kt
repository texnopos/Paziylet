package uz.texnopos.paziylet.ui.news

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_news.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.core.ResourceState
import uz.texnopos.paziylet.core.extentions.onClick
import uz.texnopos.paziylet.core.extentions.toast
import uz.texnopos.paziylet.core.extentions.visibility
import uz.texnopos.paziylet.data.model.News
import uz.texnopos.paziylet.data.model.UpdatedNews

class NewsFragment : Fragment(R.layout.fragment_news) {
    private val adapter: NewsAdapter by inject()
    private val viewModel: NewsViewModel by viewModel()
    lateinit var navController: NavController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        rvNews.adapter = adapter
        setUpObserver()
        viewModel.getAllNews()

        adapter.onItemClicked {
            val action = NewsFragmentDirections.actionHomeFragmentToNewsWebViewFragment(it)
            navController.navigate(action)
        }

        btnIman.onClick {
                val action = NewsFragmentDirections.actionHomeFragmentToFivePillarsFragment(getString(
                                    R.string.iyman_eng))
                navController.navigate(action)
            }
            btnNamaz.onClick {
                val action = NewsFragmentDirections.actionHomeFragmentToFivePillarsFragment(getString(
                                    R.string.namaz_eng))
                navController.navigate(action)
            }
            btnZakat.onClick {
                val action = NewsFragmentDirections.actionHomeFragmentToFivePillarsFragment(getString(
                                    R.string.zakat_eng))
                navController.navigate(action)
            }
            btnOraza.onClick {
                val action = NewsFragmentDirections.actionHomeFragmentToFivePillarsFragment(getString(
                                    R.string.oraza_eng))
                navController.navigate(action)
            }
            btnHaj.onClick {
                val action = NewsFragmentDirections.actionHomeFragmentToFivePillarsFragment(getString(
                                    R.string.xaj_eng))
                navController.navigate(action)
        }

    }

    private fun setUpObserver() {
        viewModel.news.observe(viewLifecycleOwner, {
            when (it.status) {
                ResourceState.LOADING -> {
                    progressBarNews.visibility(true)
                }
                ResourceState.SUCCESS -> {
                    adapter.models = it.data!!
                    viewModel.updated.observe(viewLifecycleOwner,{i->
                        adapter.models = it.data
                    })
                    progressBarNews.visibility(false)
                }
                ResourceState.ERROR -> {
                    Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
                    progressBarNews.visibility(false)
                }
            }
        })
    }
}