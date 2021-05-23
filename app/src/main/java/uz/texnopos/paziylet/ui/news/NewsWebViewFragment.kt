package uz.texnopos.paziylet.ui.news

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import kotlinx.android.synthetic.main.news_web_view_fragment.*
import kotlinx.android.synthetic.main.toolbar_for_backspace.view.*
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.data.model.News

class NewsWebViewFragment : Fragment(R.layout.news_web_view_fragment) {
    private val safeArgs: NewsWebViewFragmentArgs by navArgs()
    lateinit var navController: NavController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        val gson = Gson()
        val safeArgs = gson.fromJson(safeArgs.objectToJson, News::class.java)
        webView.loadData(safeArgs.description, "text/html", "UTF-8")
        tvTitle.text = safeArgs.title
        toolbar.tvToolbarTitle.text = getString(R.string.news)
        toolbar.btnHome.setOnClickListener {
            navController.popBackStack()
        }
    }
}