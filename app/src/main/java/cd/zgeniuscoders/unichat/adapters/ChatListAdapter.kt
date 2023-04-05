package cd.zgeniuscoders.unichat.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import cd.zgeniuscoders.unichat.databinding.ItemUserChatBinding
import cd.zgeniuscoders.unichat.models.ChatList

class ChatListAdapter(private val context: Context, private val chat: ArrayList<ChatList>) :
    RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>() {

    inner class ChatListViewHolder(binding: ItemUserChatBinding) : ViewHolder(binding.root) {
        val binding = ItemUserChatBinding.bind(binding.root)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        val binding = ItemUserChatBinding.inflate(LayoutInflater.from(context), parent, false)
        return ChatListViewHolder(binding)
    }

    override fun getItemCount() = chat.size
    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        holder.binding.userMessage.text = chat[position].message
    }

}