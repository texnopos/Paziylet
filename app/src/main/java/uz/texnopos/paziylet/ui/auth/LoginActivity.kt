package uz.texnopos.paziylet.ui.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
        btnSignIn.setOnClickListener {
            val user = auth.currentUser
            if (user != null) {
                sendToMain()
            }
            val phone = etPhoneNumber.text.toString()
            if (phone.isNotEmpty() && phone.length >= 13) {
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
                Toast.makeText(this, "Telefon nomerińizdi tolıq kiritiń", Toast.LENGTH_LONG).show()
                btnSignIn.isEnabled = true
                btnAnonymous.isEnabled = true
            }
        }

        btnSignInMain.setOnClickListener {
            val verificationCode =
                etFirstNumber.text.toString() + etSecondNumber.text.toString() + etThirdNumber.text.toString() + etFourthNumber.text.toString() + etFifthNumber.text.toString() + etSixthNumber.text.toString()
            if (etFirstNumber.text!!.isNotEmpty() || etSecondNumber.text!!.isNotEmpty() || etThirdNumber.text!!.isNotEmpty() || etFourthNumber.text!!.isNotEmpty() || etFifthNumber.text!!.isNotEmpty() || etSixthNumber.text!!.isNotEmpty()) {
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
                btnSignIn.isEnabled = true
                btnAnonymous.isEnabled = true
                Toast.makeText(applicationContext, "${e.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                mCodeS = s
                Handler(Looper.getMainLooper()).postDelayed({
                    btnSignIn.visibility = View.GONE
                    btnAnonymous.visibility = View.GONE
                    etPhoneNumber.visibility = View.GONE
                    etFirstNumber.visibility = View.VISIBLE
                    etSecondNumber.visibility = View.VISIBLE
                    etThirdNumber.visibility = View.VISIBLE
                    etFourthNumber.visibility = View.VISIBLE
                    etFifthNumber.visibility = View.VISIBLE
                    etSixthNumber.visibility = View.VISIBLE
                    btnSignInMain.visibility = View.VISIBLE
                }, 5000)
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
                Toast.makeText(this, "Kiritilgen sanlardi tekserip shig'in", Toast.LENGTH_SHORT).show()
            }
        }
    }
}