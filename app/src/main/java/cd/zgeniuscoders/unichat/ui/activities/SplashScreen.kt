package cd.zgeniuscoders.unichat.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import cd.zgeniuscoders.unichat.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = this.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val isAuthenticated = sharedPreferences.getBoolean("isAuth", false)

        handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            if (isAuthenticated) {
                Intent(this, MainActivity::class.java).apply {
                    startActivity(this)
                }
                finish()
            } else {
                Intent(this, VerificationActivity::class.java).apply {
                    startActivity(this)
                }
                finish()
            }
        }, 3000)
    }
}