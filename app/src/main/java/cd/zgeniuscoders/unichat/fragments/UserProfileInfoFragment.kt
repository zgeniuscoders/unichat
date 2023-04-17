package cd.zgeniuscoders.unichat.fragments

import android.content.Context
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

        binding.btnNext.setOnClickListener {

            val edtUsername = binding.username.text.toString()
            val edtEmail = binding.emailAddress.text.toString()

            if (edtEmail.isEmpty() && edtUsername.isEmpty()) {
                binding.textInputLayout.error = getString(R.string.required_field)
                binding.textInputLayout.isErrorEnabled = true

                binding.textInputLayout2.error = getString(R.string.required_field)
                binding.textInputLayout2.isErrorEnabled = true
            } else {
                savaData(edtEmail, edtUsername)
            }
        }

        return binding.root

    }

    private fun savaData(edtEmail: String, edtUsername: String) {

        val sharedPreferences =
            requireContext().getSharedPreferences("auth.number", Context.MODE_PRIVATE)
        val number = sharedPreferences.getString("number", "")


        val uuid = FirebaseAuth.getInstance().currentUser!!.uid

        val user = User(
            number = number!!, id = uuid, username = edtUsername, email = edtEmail
        )

        UserRepository().addUser(uuid, user).addOnCompleteListener {
            findNavController().navigate(R.id.action_userProfileInfoFragment_to_userProfileImageFragment)
        }

    }

}