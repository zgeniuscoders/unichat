package cd.zgeniuscoders.unichat.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import cd.zgeniuscoders.unichat.databinding.ItemPostBinding
import cd.zgeniuscoders.unichat.models.Post

class PostAdapter(private val context: Context, private val posts: ArrayList<Post>) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(binding: ItemPostBinding) : ViewHolder(binding.root) {
        val binding = ItemPostBinding.bind(binding.root)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(context), parent, false)
        return PostViewHolder(binding)
    }

    override fun getItemCount() = posts.size
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        getUserInfo(posts[position], holder)
        holder.binding.postContent.text = posts[position].content
        getLikeInfo(posts[position], holder)
        getCommentInfo(posts[position], holder)
    }

    private fun getCommentInfo(post: Post, holder: PostViewHolder) {
        TODO("Not yet implemented")
    }

    private fun getLikeInfo(post: Post, holder: PostViewHolder) {
        TODO("Not yet implemented")
    }

    private fun getUserInfo(post: Post, holder: PostViewHolder) {

    }


}