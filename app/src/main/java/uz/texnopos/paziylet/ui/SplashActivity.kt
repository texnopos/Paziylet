package uz.texnopos.paziylet.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.settings.Settings
import uz.texnopos.paziylet.ui.auth.LoginActivity

class SplashActivity : AppCompatActivity() {
    lateinit var settings: Settings
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()
        settings = Settings(this)
        Handler(Looper.getMainLooper()).postDelayed({
            if (settings.isAppFirstLaunched()) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 1000)
    }
}
