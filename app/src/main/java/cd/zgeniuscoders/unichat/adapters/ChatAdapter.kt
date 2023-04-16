package cd.zgeniuscoders.unichat.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import cd.zgeniuscoders.unichat.R
import cd.zgeniuscoders.unichat.databinding.ActivityCommentBinding
import cd.zgeniuscoders.unichat.databinding.DeleteLayoutBinding
import cd.zgeniuscoders.unichat.databinding.ItemChatReceiverBinding
import cd.zgeniuscoders.unichat.databinding.ItemChatSenderBinding
import cd.zgeniuscoders.unichat.models.Chat
import cd.zgeniuscoders.unichat.repositories.ChatRepository
import cd.zgeniuscoders.unichat.repositories.UserRepository
import com.bumptech.glide.Glide

class ChatAdapter(
    private val context: Context,
    private val messages: ArrayList<Chat>
) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {

    private val ITEM_SENT = 1
    private val ITEM_RECEIVE = 2

    inner class SentMsgViewHolder(binding: ItemChatSenderBinding) : ViewHolder(binding.root) {
        val binding = ItemChatSenderBinding.bind(binding.root)
    }

    inner class ReceiveMsgViewHolder(binding: ItemChatReceiverBinding) : ViewHolder(binding.root) {
        val binding = ItemChatReceiverBinding.bind(binding.root)
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        val userId = UserRepository().currentUser()!!.uid
        return if (message.senderId === userId) {
            ITEM_SENT
        } else {
            ITEM_RECEIVE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == ITEM_SENT) {
            val binding = ItemChatSenderBinding.inflate(LayoutInflater.from(context), parent, false)
            SentMsgViewHolder(binding)
        } else {
            val binding =
                ItemChatReceiverBinding.inflate(LayoutInflater.from(context), parent, false)
            ReceiveMsgViewHolder(binding)
        }
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messages[position]
        if (holder.javaClass == SentMsgViewHolder::class.java) {
            val viewHolder = holder as SentMsgViewHolder

            if (message.isImage) {
                viewHolder.binding.msgImage.visibility = View.VISIBLE
                viewHolder.binding.msgBox.visibility = View.GONE
                Glide.with(context).load(message.image).into(viewHolder.binding.msgImage)
            }

            viewHolder.binding.txtMsg.text = message.message
            viewHolder.itemView.setOnLongClickListener {
                val view = LayoutInflater.from(context).inflate(R.layout.delete_layout, null)
                val binding = DeleteLayoutBinding.bind(view)

                val dialog =
                    AlertDialog.Builder(context)
                        .setTitle(R.string.deleteMsg)
                        .setView(binding.root)
                        .create()

                binding.deleteForEverything.setOnClickListener {
                    message.message = R.string.msgDelete.toString()

//                    sender
                    message.receiverId.let {
                        val hash = HashMap<String, Any>()
                        hash["message"] = message.message!!
//                        update msg
                    }

                    dialog.dismiss()
                }
                binding.deleteForMe.setOnClickListener {
                    message.message = R.string.msgDelete.toString()

//                    sender
                    message.receiverId.let {
                        val hash = HashMap<String, Any>()
//                        msg null
                    }

                    dialog.dismiss()
                }
                binding.cancel.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.show()
                false
            }

        } else {
            val viewHolder = holder as SentMsgViewHolder

            if (message.isImage) {
                viewHolder.binding.msgImage.visibility = View.VISIBLE
                viewHolder.binding.msgBox.visibility = View.GONE
                Glide.with(context).load(message.image).into(viewHolder.binding.msgImage)
            }

            viewHolder.binding.txtMsg.text = message.message
            viewHolder.itemView.setOnLongClickListener {
                val view = LayoutInflater.from(context).inflate(R.layout.delete_layout, null)
                val binding = DeleteLayoutBinding.bind(view)

                val dialog =
                    AlertDialog.Builder(context)
                        .setTitle(R.string.deleteMsg)
                        .setView(binding.root)
                        .create()

                binding.deleteForEverything.setOnClickListener {
                    message.message = R.string.msgDelete.toString()

//                    sender
                    message.senderId.let {
                        val hash = HashMap<String, Any>()
                        hash["message"] = message.message!!
                    }

                    dialog.dismiss()
                }
                binding.deleteForMe.setOnClickListener {
                    message.message = R.string.msgDelete.toString()

//                    sender
                    message.senderId.let {
                        val hash = HashMap<String, Any>()
//                        msg null
                    }

                    dialog.dismiss()
                }
                binding.cancel.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.show()
                false
            }
        }
    }
}