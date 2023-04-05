package cd.zgeniuscoders.unichat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cd.zgeniuscoders.unichat.databinding.ActivityOptverifyBinding

class OPTVerifyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOptverifyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOptverifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val edtNumber = intent.getStringExtra("phone_number")
        binding.verifyNumber.text = edtNumber
    }
}