package cd.zgeniuscoders.unichat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cd.zgeniuscoders.unichat.databinding.ActivityCommentBinding

class CommentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}