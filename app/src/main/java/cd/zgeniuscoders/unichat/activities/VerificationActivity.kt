package cd.zgeniuscoders.unichat.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cd.zgeniuscoders.unichat.R
import cd.zgeniuscoders.unichat.databinding.ActivityVerificationBinding

class VerificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSend.setOnClickListener {
            val edtNumber = binding.phoneNumberEdt.text.toString()
            if (edtNumber.isEmpty()) {
                binding.layoutTextInput.error = getString(R.string.required_field)
                binding.layoutTextInput.isErrorEnabled = true
            } else {
                Intent(this, OPTVerifyActivity::class.java).apply {
                    this.putExtra("phone_number", edtNumber)
                    startActivity(this)
                }
            }
        }
    }
}