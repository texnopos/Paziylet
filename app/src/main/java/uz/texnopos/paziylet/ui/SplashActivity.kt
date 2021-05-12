package uz.texnopos.paziylet.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.setting.Setting
import uz.texnopos.paziylet.ui.auth.LoginActivity


class SplashActivity : AppCompatActivity() {
        lateinit var setting: Setting
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()
        setting = Setting(this)
        Handler(Looper.getMainLooper()).postDelayed({
            if (setting.isAppFirstLaunched()) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 3000)
    }
}
