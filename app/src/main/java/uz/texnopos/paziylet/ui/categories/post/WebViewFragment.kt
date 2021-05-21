package uz.texnopos.paziylet.ui.categories.post

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_webview.*
import uz.texnopos.paziylet.R

class WebViewFragment:Fragment(R.layout.fragment_webview) {

    private val safeArgs:WebViewFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView.loadData(safeArgs.text,"text/html","UTF-8")
    }


}