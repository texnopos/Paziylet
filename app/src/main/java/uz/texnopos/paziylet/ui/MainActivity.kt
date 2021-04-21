package uz.texnopos.paziylet.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*
import uz.texnopos.paziylet.R

class MainActivity : AppCompatActivity() {
    private lateinit var navHostController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navHostController = findNavController(R.id.fragmentContainer)
        NavigationUI.setupWithNavController(bottomNavigation, navHostController)
    }
}