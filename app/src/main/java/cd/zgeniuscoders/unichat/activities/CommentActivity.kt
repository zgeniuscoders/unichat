package cd.zgeniuscoders.unichat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import cd.zgeniuscoders.unichat.adapters.CommentAdapter
import cd.zgeniuscoders.unichat.databinding.ActivityCommentBinding
import cd.zgeniuscoders.unichat.models.Comment
import cd.zgeniuscoders.unichat.repositories.CommentRepository
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.toObject

class CommentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val postId = intent.getStringExtra("postId")
        val postContent = intent.getStringExtra("postContent")
        val postImage = intent.getStringExtra("postImage")
        val postAt = intent.getStringExtra("postCreated")

        binding.postUserName.text = intent.getStringExtra("postUsername")

        if (postContent!!.isNotEmpty()) {
            binding.postContent.text = postContent
        } else {
            binding.postContent.visibility = View.GONE
        }

        if (postImage!!.isNotEmpty()) {
            Glide.with(this).load(postImage).into(binding.postImage)
        } else {
            binding.postImage.visibility = View.GONE
        }

        getComments(postId)
        val commentsList = ArrayList<Comment>()

        CommentRepository().all().addSnapshotListener { querySnap, error ->
            if (error != null) return@addSnapshotListener
            if (querySnap != null) {
                for (doc in querySnap.documents) {
                    val data = doc.toObject(Comment::class.java)
                    commentsList.add(data!!)
                }
                binding.commentRecycler.adapter = CommentAdapter(this, commentsList)
            }
        }
    }

    private fun getComments(postId: String?) {

    }


}