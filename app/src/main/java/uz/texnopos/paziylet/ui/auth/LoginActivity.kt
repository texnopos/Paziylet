package uz.texnopos.paziylet.ui.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isNotEmpty
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
import uz.texnopos.paziylet.core.ResourceState
import uz.texnopos.paziylet.core.extentions.hideKeyboard
import uz.texnopos.paziylet.core.extentions.onClick
import uz.texnopos.paziylet.core.extentions.visibility
import uz.texnopos.paziylet.settings.Settings
import uz.texnopos.paziylet.ui.MainActivity
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

    lateinit var settings: Settings
    private val auth: FirebaseAuth by inject()
    private lateinit var mCallBacks: OnVerificationStateChangedCallbacks
    lateinit var mCodeS: String
    private val viewModel: LoginViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        setUpObserver()
        settings = Settings(this)
        btnAnonymous.onClick {
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        btnSignIn.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            hideKeyboard()
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
                btnSignIn.isEnabled = false
                btnAnonymous.isEnabled = false
            } else {
                Toast.makeText(this, getString(R.string.input_phone_number), Toast.LENGTH_LONG).show()
                btnSignIn.isEnabled = true
                btnAnonymous.isEnabled = true
                progressBar.visibility = View.GONE
            }
        }

        btnSignInMain.setOnClickListener {
            val verificationCode = smsCodeView
            if (smsCodeView.isNotEmpty()) {
                val credential =
                    PhoneAuthProvider.getCredential(mCodeS, verificationCode.enteredCode)
                viewModel.signIn(credential)
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.input_sms_code),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        mCallBacks = object : OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                viewModel.signIn(phoneAuthCredential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                btnSignIn.isEnabled = true
                btnAnonymous.isEnabled = true
                Toast.makeText(applicationContext, "${e.message}", Toast.LENGTH_LONG).show()
                progressBar.visibility = View.GONE
            }

            override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                mCodeS = s
                Handler(Looper.getMainLooper()).postDelayed({
                    progressBar.visibility = View.GONE
                    btnSignIn.visibility = View.GONE
                    btnAnonymous.visibility = View.GONE
                    etPhoneNumber.visibility = View.GONE
                    smsCodeView.visibility(true)
                    btnSignInMain.visibility = View.VISIBLE
                }, 7000)
            }
        }
    }

    private fun sendToMain() {
        settings.setFirstLaunched()
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
        finish()
    }

    private fun setUpObserver() {
        viewModel.registration.observe(this, {
            when (it.status) {
                ResourceState.LOADING -> progressBar.visibility(true)
                ResourceState.SUCCESS -> {
                    sendToMain()
                    progressBar.visibility(false)
                }
                ResourceState.ERROR -> {
                    progressBar.visibility(false)
                    Toast.makeText(this, getString(R.string.confirmation_code_error), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }
}