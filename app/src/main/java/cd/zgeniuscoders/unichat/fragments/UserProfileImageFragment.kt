package cd.zgeniuscoders.unichat.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import cd.zgeniuscoders.unichat.R
import cd.zgeniuscoders.unichat.activities.MainActivity
import cd.zgeniuscoders.unichat.databinding.FragmentUserProfileImageBinding
import cd.zgeniuscoders.unichat.repositories.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class UserProfileImageFragment : Fragment() {
    private lateinit var binding: FragmentUserProfileImageBinding
    private var dialog: ProgressDialog? = null
    private var img: Uri? = null
    private var imgUrl: String? = ""

    private var launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            img = it.data!!.data
            binding.userProfile.setImageURI(img)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserProfileImageBinding.inflate(layoutInflater)

        dialog = ProgressDialog(requireContext())
        dialog!!.setMessage(R.string.send_opt.toString())
        dialog!!.setCancelable(false)

        binding.userProfile.setOnClickListener {
            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type = "image/*"
            launchGalleryActivity.launch(intent)
        }

        binding.btnCreate.setOnClickListener {
            dialog!!.show()
            uploadImage()
        }
        return binding.root
    }

    private fun uploadImage() {
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val refStorage = FirebaseStorage.getInstance().reference.child("profiles/$fileName")

        refStorage.putFile(img!!).addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener { image ->
                imgUrl = image.toString()
                saveUser()
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun saveUser() {
        val uuid = FirebaseAuth.getInstance().currentUser!!.uid
        val currentDate = Calendar.getInstance().time
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentDate)

        val hash = HashMap<String, Any>()
        hash["createdAt"] = date
        hash["profile"] = imgUrl.toString()

        UserRepository().updateUser(uuid, hash)
        dialog!!.dismiss()

        val sharedPreferences = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isAuth", true)
        editor.apply()

        Intent(requireContext(), MainActivity::class.java).apply {
            startActivity(this)
        }
    }

}