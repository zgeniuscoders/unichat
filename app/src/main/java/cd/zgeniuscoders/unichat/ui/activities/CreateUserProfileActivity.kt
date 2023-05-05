package cd.zgeniuscoders.unichat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cd.zgeniuscoders.unichat.databinding.ActivityCreateUserProfileBinding

class CreateUserProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateUserProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}