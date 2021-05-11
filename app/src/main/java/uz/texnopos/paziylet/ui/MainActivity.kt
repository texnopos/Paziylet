package uz.texnopos.paziylet.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.LocationSettingsResult
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import uz.texnopos.paziylet.R

class MainActivity : AppCompatActivity(), ResultCallback<LocationSettingsResult> {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.bottomNavigation)
        navController = findNavController(R.id.fragmentContainer)
        navView.setupWithNavController(navController)
    }

    override fun onResult(p0: LocationSettingsResult) {
        Toast.makeText(applicationContext, p0.status.statusCode, Toast.LENGTH_SHORT).show()
    }
}