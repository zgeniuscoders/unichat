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
    private lateinit var userRepository: UserRepository
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userRepository = UserRepository()
        userId = userRepository.currentUser()!!.uid


        binding.addPost.setOnClickListener {
            Intent(this, AddPostActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val hash = HashMap<String, Any>()
        hash["presence"] = true

//        userRepository.update(userId, hash)

    }

    override fun onPause() {
        super.onPause()

        val hash = HashMap<String, Any>()
        hash["presence"] = false

//        userRepository.update(userId, hash)
    }
}