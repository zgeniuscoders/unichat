package cd.zgeniuscoders.unichat.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cd.zgeniuscoders.unichat.R
import cd.zgeniuscoders.unichat.databinding.ActivityMainBinding
import cd.zgeniuscoders.unichat.repositories.UserRepository
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val userRepository = UserRepository()
    private val userId = userRepository.currentUser()!!.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.addPost.setOnClickListener {
            Intent(this,AddPostActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val hash = HashMap<String, Any>()
        hash["presence"] = true

        userRepository.updateUser(userId, hash)

    }

    override fun onPause() {
        super.onPause()

        val hash = HashMap<String, Any>()
        hash["presence"] = false

        userRepository.updateUser(userId, hash)
    }
}