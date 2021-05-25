package uz.texnopos.paziylet.ui.categories.post

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_webview.*
import kotlinx.android.synthetic.main.toolbar.view.*
import kotlinx.android.synthetic.main.toolbar.view.btnHome
import kotlinx.android.synthetic.main.toolbar.view.tvToolbarTitle
import kotlinx.android.synthetic.main.toolbar_for_backspace.view.*
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.core.extentions.onClick

class WebViewFragment : Fragment(R.layout.fragment_webview) {

    private val safeArgs: WebViewFragmentArgs by navArgs()
    private lateinit var navController: NavController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        toolbar.tvToolbarTitle.text = safeArgs.path
        toolbar.btnHome.onClick {
            navController.popBackStack()
        }
        tvTitle.text = safeArgs.title
        webView.loadData(safeArgs.text, "text/html", "UTF-8")
    }

}