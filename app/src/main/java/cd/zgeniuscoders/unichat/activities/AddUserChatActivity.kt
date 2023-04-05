package cd.zgeniuscoders.unichat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cd.zgeniuscoders.unichat.databinding.ActivityAddUserChatBinding

class AddUserChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddUserChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUserChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}