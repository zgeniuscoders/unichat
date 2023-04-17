package cd.zgeniuscoders.unichat.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import cd.zgeniuscoders.unichat.databinding.ActivityAddPostBinding
import cd.zgeniuscoders.unichat.models.Post
import cd.zgeniuscoders.unichat.repositories.PostRepository
import cd.zgeniuscoders.unichat.repositories.UserRepository
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class AddPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPostBinding
    private var img: Uri? = null
    private var imgUrl: String? = ""

    private var launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            img = it.data!!.data
            binding.postImg.setImageURI(img)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.postImg.setOnClickListener {
            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type = "image/*"
            launchGalleryActivity.launch(intent)
        }

        binding.addPost.setOnClickListener {
            val edtContent = binding.postContent.text.toString()
            if (img != null) {
                uploadImage()
            } else {
                if (edtContent.isNotEmpty()) {
                    addPost()
                } else {
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun uploadImage() {
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val refStorage = FirebaseStorage.getInstance().reference.child("posts/$fileName")

        refStorage.putFile(img!!).addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener { image ->
                imgUrl = image.toString()
                addPost()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun addPost() {

        val currentDate = Calendar.getInstance().time
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentDate)

        val postRepository = PostRepository()
        val postKey = postRepository.getKey()

        val post = Post(
            id = postKey,
            userId = UserRepository().currentUser()!!.uid,
            content = binding.postContent.text.toString(),
            image = imgUrl,
            postAt = date
        )

        postRepository.addPost(postKey, post).addOnCompleteListener {
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
        }

    }
}