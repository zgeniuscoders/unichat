package cd.zgeniuscoders.unichat.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cd.zgeniuscoders.unichat.R
import cd.zgeniuscoders.unichat.databinding.FragmentGroupBinding


class GroupFragment : Fragment() {
    private lateinit var binding: FragmentGroupBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupBinding.inflate(layoutInflater)
        return binding.root
    }

}