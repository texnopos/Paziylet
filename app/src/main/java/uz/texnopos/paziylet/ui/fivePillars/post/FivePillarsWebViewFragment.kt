package uz.texnopos.paziylet.ui.fivePillars.post

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_webview.*
import kotlinx.android.synthetic.main.fragment_webview.toolbar
import kotlinx.android.synthetic.main.fragment_webview.webView
import kotlinx.android.synthetic.main.news_web_view_fragment.*
import kotlinx.android.synthetic.main.toolbar.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.core.ResourceState
import uz.texnopos.paziylet.core.extentions.onClick
import uz.texnopos.paziylet.ui.fivePillars.FivePillarsViewModel

class FivePillarsWebViewFragment : Fragment(R.layout.fivepillars_web_view_fragment) {

    private val viewModel: FivePillarsViewModel by viewModel()
    private val safeArgs: FivePillarsWebViewFragmentArgs by navArgs()
    private lateinit var navController: NavController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
       // viewModel.getCategories(safeArgs.text)
        toolbar.tvToolbarTitle.text = safeArgs.text
        toolbar.btnHome.onClick {
            navController.popBackStack()
        }
        setUpObserver()
        webView.settings.defaultTextEncodingName = "UTF-8"
        webView.loadDataWithBaseURL(null, safeArgs.text, "text/html; charset=utf-8", null,null)
    }


    private fun setUpObserver() {
        viewModel.pillars.observe(viewLifecycleOwner, {
            when (it.status) {
               // ResourceState.LOADING -> progressBarCategory.visibility(true)
                ResourceState.SUCCESS -> {
                    it.data?.let { _ ->
//                        adapter.models = i
                    }
                 //   progressBarCategory.visibility(false)
                }
              //  ResourceState.ERROR -> progressBarCategory.visibility(false)
            }
        })
    }
}