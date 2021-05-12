package uz.texnopos.paziylet.ui.auth

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import kotlinx.android.synthetic.main.activity_auth.*
import org.koin.android.ext.android.inject
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.core.extentions.onClick
import uz.texnopos.paziylet.setting.Setting
import uz.texnopos.paziylet.ui.MainActivity
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {
    lateinit var setting: Setting
    private val auth: FirebaseAuth by inject()
    private lateinit var mCallBacks: OnVerificationStateChangedCallbacks
    lateinit var mCodeS: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        setting = Setting(this)
        btnAnonymous.onClick {
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
            finish()
        }
        btnSignIn.setOnClickListener {
            val user = auth.currentUser
            if (user != null) {
                sendToMain()
            }
            val phone = etPhoneNumber.text.toString()
            if (phone.isNotEmpty()) {
                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(phone)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(this)
                    .setCallbacks(mCallBacks)
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)
            } else {
                processText.text = getString(R.string.phone_number)
                processText.setTextColor(Color.RED)
                processText.visibility = View.VISIBLE
            }
        }

        btnSignInMain.setOnClickListener{
            val verificationCode = etCodeType.text.toString()
            if (verificationCode.isNotEmpty()) {
                val credential = PhoneAuthProvider.getCredential(mCodeS, verificationCode)
                signIn(credential)
            } else {
                Toast.makeText(this, "Kod kiritin", Toast.LENGTH_SHORT).show()
            }
        }
        mCallBacks = object : OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                signIn(phoneAuthCredential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                processText.text = e.message
                processText.setTextColor(Color.RED)
                processText.visibility = View.VISIBLE
            }

            override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                mCodeS = s
                    Handler().postDelayed({
                   processText.visibility = View.VISIBLE
                   btnSignIn.visibility = View.GONE
                   btnAnonymous.visibility = View.GONE
                   etPhoneNumber.visibility = View.GONE
                   etCodeType.visibility = View.VISIBLE
                   btnSignInMain.visibility = View.VISIBLE
                }, 6000)
            }
        }
    }

    private fun sendToMain() {
        setting.setFirstLaunched()
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
        finish()
    }

    private fun signIn(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                sendToMain()
            } else {
                processText!!.text = task.exception!!.message
                processText!!.setTextColor(Color.RED)
                processText!!.visibility = View.VISIBLE
            }

        }
    }
}