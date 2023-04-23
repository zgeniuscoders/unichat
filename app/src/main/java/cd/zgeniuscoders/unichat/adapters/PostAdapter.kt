package cd.zgeniuscoders.unichat.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import cd.zgeniuscoders.unichat.R
import cd.zgeniuscoders.unichat.SaveFile
import cd.zgeniuscoders.unichat.activities.CommentActivity
import cd.zgeniuscoders.unichat.databinding.ItemPostBinding
import cd.zgeniuscoders.unichat.models.Post
import cd.zgeniuscoders.unichat.repositories.CommentRepository
import cd.zgeniuscoders.unichat.repositories.LikeRepository
import cd.zgeniuscoders.unichat.repositories.UserRepository
import com.bumptech.glide.Glide

class PostAdapter(private val context: Context, private val posts: ArrayList<Post>) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>(), Filterable {

    private var postsFilterdList: List<Post> = ArrayList()
    private val likeRepository = LikeRepository()
    private val userRepository = UserRepository()

    init {
        postsFilterdList = posts
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                postsFilterdList = if (charSearch.isEmpty()) {
                    posts
                } else {
                    val result = ArrayList<Post>()
                    for (post in posts) {
                        if (post.content?.lowercase()!!.contains(charSearch.lowercase())) {
                            result.add(post)
                        }
                    }
                    result
                }
                val fresult = FilterResults()
                fresult.values = postsFilterdList
                return fresult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                postsFilterdList = results?.values as ArrayList<Post>
                notifyDataSetChanged()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(context), parent, false)
        return PostViewHolder(binding)
    }

    override fun getItemCount() = posts.size
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postsFilterdList[position]

        getUserInfo(post, holder)

        if (post.content == "") holder.binding.postContent.visibility = View.GONE
        if (post.image == "") holder.binding.postImage.visibility = View.GONE

        holder.binding.postContent.text = post.content

        if (post.image != "") {
            Glide.with(context).load(post.image).into(holder.binding.postImage)
        }

        getLikeInfo(post, holder)
        getCommentInfo(post, holder)

        holder.binding.btnPostLike.setOnClickListener {
            likePost(post.id)
        }

        holder.binding.btnPostComment.setOnClickListener {
            Intent(context, CommentActivity::class.java).apply {
                this.putExtra("postId", post.id)
                context.startActivity(this)
            }
        }

        holder.binding.btnPostSave.setOnClickListener {
            if (post.image != "") {
                SaveFile(context).downloadImageFromFirestore(post.image!!)
            }
        }
    }

    private fun likePost(postId: String) {
        likeRepository.getLike(postId).collection("users")
            .document(UserRepository().currentUser()!!.uid)
            .addSnapshotListener { querySnap, error ->
                if (error != null) return@addSnapshotListener
                if (querySnap != null) {

                    val userId = userRepository.currentUser()!!.uid

                    if (querySnap.exists()) {
                        likeRepository.deleteLike(postId, userId)
                    } else {

                        val like = HashMap<String, Any>()
                        like["userId"] = userId

                        likeRepository.addLike(postId, userId, like)
                    }
                }
            }
    }

    private fun getCommentInfo(post: Post, holder: PostViewHolder) {
        val commentRepository = CommentRepository()
        commentRepository.getComment(post.id).collection("users")
            .addSnapshotListener { querySnap, error ->
                if (error != null) return@addSnapshotListener
                if (querySnap != null) {
                    holder.binding.commentCount.text = querySnap.size().toString()
                }
            }
    }

    private fun getLikeInfo(post: Post, holder: PostViewHolder) {

        likeRepository.getLike(post.id).collection("users")
            .addSnapshotListener { querySnap, error ->
                if (error != null) return@addSnapshotListener
                if (querySnap != null) {
                    holder.binding.likeCount.text = querySnap.size().toString()
                }
            }

        likeRepository.getLike(post.id).collection("users")
            .document(UserRepository().currentUser()!!.uid)
            .addSnapshotListener { querySnap, error ->
                if (error != null) return@addSnapshotListener
                if (querySnap != null) {
//                    if (querySnap.exists()) {
//                        holder.binding.imgLikeBtn.background =
//                            ColorDrawable(Color.parseColor(R.color.blue.toString()))
//                    } else {
//                        holder.binding.imgLikeBtn.background =
//                            ColorDrawable(Color.parseColor(R.color.dark_black.toString()))
//                    }
                }
            }

    }

    private fun getUserInfo(post: Post, holder: PostViewHolder) {
        val userRepository = UserRepository()
        userRepository.getUser(post.userId).addSnapshotListener { querySnap, error ->
            if (error != null) return@addSnapshotListener
            if (querySnap != null) {
                holder.binding.postUserName.text = querySnap.get("username").toString()
                Glide.with(context).load(querySnap.get("profile"))
                    .into(holder.binding.postUserProfile)
            }
        }
    }

    inner class PostViewHolder(binding: ItemPostBinding) : ViewHolder(binding.root) {
        val binding = ItemPostBinding.bind(binding.root)
    }

}