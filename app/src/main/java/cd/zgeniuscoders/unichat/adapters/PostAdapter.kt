package cd.zgeniuscoders.unichat.adapters

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
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
    private val uuid = userRepository.currentUser()!!.uid

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

        if (post.content == "") holder.binding.postContent.visibility = View.GONE
        if (post.image == "") holder.binding.postImage.visibility = View.GONE
        if (post.image != "") Glide.with(context).load(post.image).into(holder.binding.postImage)

        getUserInfo(post, holder)

        holder.binding.postContent.text = post.content

        // get like
        getLikes(post, holder)
        // check if the current user liked the current post
        isLikeByMe(post, holder)

        getCommentInfo(post, holder)

        holder.binding.btnPostLike.setOnClickListener {
            val likeHash = HashMap<String, Any>()
            likeHash["postId"] = post.id
            likeHash["userId"] = uuid

            likeRepository.addLike(post.id, uuid, likeHash)
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
            } else {
                val clipboard =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("label", post.content)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, "Texte copié dans le presse-papier.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun getLikes(post: Post, holder: PostAdapter.PostViewHolder) {
        likeRepository.getLikes(post).addSnapshotListener { querySnap, error ->
            if (error != null) return@addSnapshotListener
            if (querySnap != null) {
                holder.binding.likeCount.text = querySnap.size().toString()
            }
        }
    }

    private fun isLikeByMe(post: Post, holder: PostAdapter.PostViewHolder) {
        likeRepository.isLike(post.id, userRepository.currentUser()!!.uid)
            .addSnapshotListener { querySnap, error ->
                if (error != null) return@addSnapshotListener
                if (querySnap != null) {
                    if (querySnap.exists()) {
                        holder.binding.imgLikeBtn.setImageResource(R.drawable.baseline_favorite_24)
                    } else {
                        holder.binding.imgLikeBtn.setImageResource(R.drawable.baseline_unfavorite_24)
                    }
                }
            }
    }

    private fun getCommentInfo(post: Post, holder: PostViewHolder) {
        val commentRepository = CommentRepository()
        commentRepository.findById(post.id)
            .addSnapshotListener { querySnap, error ->
                if (error != null) return@addSnapshotListener
                if (querySnap != null) {
                    holder.binding.commentCount.text = querySnap.size().toString()
                }
            }
    }

    private fun getUserInfo(post: Post, holder: PostViewHolder) {
        val userRepository = UserRepository()
        userRepository.findById(post.userId).addSnapshotListener { querySnap, error ->
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