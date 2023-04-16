package cd.zgeniuscoders.unichat.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import cd.zgeniuscoders.unichat.R
import cd.zgeniuscoders.unichat.databinding.FragmentUserProfileInfoBinding
import cd.zgeniuscoders.unichat.models.User
import cd.zgeniuscoders.unichat.repositories.UserRepository
import com.google.firebase.auth.FirebaseAuth

class UserProfileInfoFragment : Fragment() {

    private lateinit var binding: FragmentUserProfileInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentUserProfileInfoBinding.inflate(layoutInflater)

        val edtUsername = binding.username.text.toString()
        val edtEmail = binding.emailAddress.text.toString()

        binding.btnNext.setOnClickListener {

            if (edtEmail.isNotEmpty() && edtUsername.isNotEmpty()) {
                savaData(edtEmail, edtUsername)
            } else {
                binding.textInputLayout.error = getString(R.string.required_field)
                binding.textInputLayout.isErrorEnabled = true

                binding.textInputLayout2.error = getString(R.string.required_field)
                binding.textInputLayout2.isErrorEnabled = true
            }
        }

        return binding.root

    }

    private fun savaData(edtEmail: String, edtUsername: String) {

        val uuid = FirebaseAuth.getInstance().currentUser!!.uid

        val user = User(
            id = uuid, username = edtUsername, email = edtEmail
        )

        UserRepository().addUser(uuid, user)
        findNavController().navigate(R.id.action_userProfileInfoFragment_to_userFacultyInfoFragment)

    }

}