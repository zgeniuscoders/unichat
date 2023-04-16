package cd.zgeniuscoders.unichat.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cd.zgeniuscoders.unichat.R
import cd.zgeniuscoders.unichat.adapters.PostAdapter
import cd.zgeniuscoders.unichat.databinding.FragmentHomeBinding
import cd.zgeniuscoders.unichat.models.Post
import cd.zgeniuscoders.unichat.repositories.PostRepository
import cd.zgeniuscoders.unichat.repositories.UserRepository
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.toObject

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var postList: ArrayList<Post>
    private lateinit var postAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        postList = ArrayList()
        postAdapter = PostAdapter(requireContext(), postList)

        loadUser()

        loadPosts()

        return binding.root
    }

    private fun loadPosts() {
        val postRepository = PostRepository()
        postRepository.getPosts()
            .addSnapshotListener { querySnap, error ->


                if (error != null) {
                    return@addSnapshotListener
                }

                if (querySnap != null) {
                    postList.clear()

                    for (doc in querySnap.documents) {
                        val data = doc.toObject(Post::class.java)
                        postList.add(data!!)
                    }

                    binding.recyclerPost.adapter = postAdapter
                }

            }
    }

    private fun loadUser() {

        val userRepository = UserRepository()
        val uuid = userRepository.currentUser()!!.uid

        userRepository.getUser(uuid).addSnapshotListener { querySnap, error ->

            if (error != null) {
                return@addSnapshotListener
            }

            if (querySnap != null) {
                val userProfile = querySnap.get("profile")
                Glide.with(this).load(userProfile).into(binding.userProfile)
            }

        }
    }

}