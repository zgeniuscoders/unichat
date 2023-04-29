package cd.zgeniuscoders.unichat.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
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
    private var postList = ArrayList<Post>()

    private lateinit var postRepository: PostRepository
    private lateinit var adapter: PostAdapter
    private lateinit var lm: LinearLayoutManager

    private val PAGE_SIZE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(layoutInflater)
        postRepository = PostRepository()

        lm = LinearLayoutManager(requireContext())
        lm.orientation = LinearLayoutManager.VERTICAL

        adapter = PostAdapter(requireContext(), postList)
        binding.recyclerPost.layoutManager = lm

        loadPosts()

        // faire apparaitre les element lorsque l'utilisateur scroll
        binding.recyclerPost.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @SuppressLint("NotifyDataSetChanged")
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItem = lm.findLastVisibleItemPosition()
                if (lastVisibleItem >= postList.size - PAGE_SIZE) {
                    val lastData = postList[postList.size - 1]

                    postRepository.getPosts().orderBy("postAt").startAfter(lastData.postAt)
                        .limit(PAGE_SIZE.toLong()).addSnapshotListener { querySnap, error ->

                            if (error != null) return@addSnapshotListener

                            if (querySnap != null) {

                                for (doc in querySnap.documents) {
                                    val data = doc.toObject(Post::class.java)
                                    postList.add(data!!)
                                }

                                adapter.notifyDataSetChanged()

                            }

                        }
                }
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUser()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadPosts() {
        postRepository.getPosts().orderBy("postAt").limit(PAGE_SIZE.toLong())
            .addSnapshotListener { querySnap, error ->

                if (error != null) return@addSnapshotListener

                if (querySnap != null) {

                    for (doc in querySnap.documents) {
                        val data = doc.toObject(Post::class.java)
                        postList.add(data!!)
                    }

                    adapter.notifyDataSetChanged()
                }

            }
    }

    private fun loadUser() {

        val userRepository = UserRepository()
        val uuid = userRepository.currentUser()!!.uid

        userRepository.findById(uuid).addSnapshotListener { querySnap, error ->

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