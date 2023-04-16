package cd.zgeniuscoders.unichat.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import cd.zgeniuscoders.unichat.R
import cd.zgeniuscoders.unichat.databinding.FragmentUserProfileInfoBinding
import cd.zgeniuscoders.unichat.repositories.UserRepository
import com.google.firebase.auth.FirebaseAuth

class UserProfileInfoFragment : Fragment() {
    private lateinit var binding: FragmentUserProfileInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserProfileInfoBinding.inflate(layoutInflater)
        val edtUsername = binding.username.text
        val edtEmail = binding.emailAddress.text

        binding.btnNext.setOnClickListener {

            if (edtEmail!!.isNotEmpty() && edtUsername!!.isNotEmpty()) {
                savaData(edtEmail.toString(), edtUsername.toString())
            } else {
                binding.emailAddress.error = "Veuillez remplir cette champs"
                binding.username.error = "Veuillez remplir cette champs"
            }
        }
        return binding.root
    }

    private fun savaData(edtEmail: String, edtUsername: String) {

        val uuid = FirebaseAuth.getInstance().currentUser!!.uid

        val hash = HashMap<String, Any>()
        hash["id"] = uuid
        hash["username"] = edtUsername
        hash["email"] = edtEmail

        UserRepository().updateUser(uuid, hash)
        findNavController().navigate(R.id.action_userProfileInfoFragment_to_userFacultyInfoFragment)
    }

}