package cd.zgeniuscoders.unichat.activities

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import cd.zgeniuscoders.unichat.R
import cd.zgeniuscoders.unichat.databinding.ActivityOptverifyBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.concurrent.TimeUnit

class OPTVerifyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOptverifyBinding
    private var auth: FirebaseAuth? = null
    private var dialog: ProgressDialog? = null
    private var verificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOptverifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val edtNumber = intent.getStringExtra("phone_number")
        binding.verifyNumber.text = edtNumber

        dialog = ProgressDialog(this)
        dialog!!.setMessage(R.string.send_opt.toString())
        dialog!!.setCancelable(false)
        dialog!!.show()

        auth = FirebaseAuth.getInstance()
        val options = PhoneAuthOptions.newBuilder(auth!!).setPhoneNumber(edtNumber!!)
            .setTimeout(60L, TimeUnit.SECONDS).setActivity(this)
            .setCallbacks(object : OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(p0: FirebaseException) {

                }

                @SuppressLint("ServiceCast")
                override fun onCodeSent(
                    verifyId: String, ResendingToken: PhoneAuthProvider.ForceResendingToken
                ) {
                    super.onCodeSent(verifyId, ResendingToken)
                    dialog!!.dismiss()
                    verificationId = verifyId
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
                    binding.optView.requestFocus()
                }

            }).build()

        PhoneAuthProvider.verifyPhoneNumber(options)
        binding.optView.setOtpCompletionListener { opt ->
            val credentials = PhoneAuthProvider.getCredential(verificationId!!, opt)
            auth!!.signInWithCredential(credentials).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val sharedPreferences =
                        applicationContext.getSharedPreferences("auth", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("isAuth", true)
                    editor.apply()

                    Intent(this, CreateUserProfileActivity::class.java).apply {
                        startActivity(this)
                    }
                } else {
                    Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}