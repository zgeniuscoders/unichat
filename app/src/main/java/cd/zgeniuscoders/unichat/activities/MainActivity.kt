package cd.zgeniuscoders.unichat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cd.zgeniuscoders.unichat.R
import cd.zgeniuscoders.unichat.databinding.ActivityMainBinding
import cd.zgeniuscoders.unichat.repositories.UserRepository
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
    }
}