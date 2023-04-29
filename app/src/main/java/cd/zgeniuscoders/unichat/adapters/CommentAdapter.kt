package cd.zgeniuscoders.unichat.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import cd.zgeniuscoders.unichat.databinding.ItemCommentBinding
import cd.zgeniuscoders.unichat.models.Comment
import cd.zgeniuscoders.unichat.repositories.CommentRepository
import cd.zgeniuscoders.unichat.repositories.UserRepository
import com.bumptech.glide.Glide

class CommentAdapter(private val context: Context, private val comments: ArrayList<Comment>) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    private val commentRepository = CommentRepository()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(context), parent, false)
        return CommentViewHolder(binding)
    }

    override fun getItemCount() = comments.size

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        loadUser(comment, holder)
        holder.binding.comment.text = comment.comment
    }

    @SuppressLint("CheckResult")
    private fun loadUser(comment: Comment, holder: CommentViewHolder) {
        UserRepository().findById(comment.userId).addSnapshotListener { querySnap, error ->
            if (error != null) return@addSnapshotListener
            if (querySnap != null) {
                val profile = querySnap.get("profile")
                Glide.with(context).load(profile).load(holder.binding.commentUserImage)
            }
        }
    }

    inner class CommentViewHolder(binding: ItemCommentBinding) : ViewHolder(binding.root) {
        val binding = ItemCommentBinding.bind(binding.root)
    }

}