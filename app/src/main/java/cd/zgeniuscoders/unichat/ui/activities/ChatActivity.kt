package cd.zgeniuscoders.unichat.activities

import android.app.DownloadManager.Query
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import cd.zgeniuscoders.unichat.R
import cd.zgeniuscoders.unichat.adapters.ChatAdapter
import cd.zgeniuscoders.unichat.databinding.ActivityChatBinding
import cd.zgeniuscoders.unichat.models.Chat
import cd.zgeniuscoders.unichat.repositories.ChatRepository
import cd.zgeniuscoders.unichat.repositories.UserRepository
import com.google.firebase.firestore.ktx.toObject
import java.util.Date

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private val userRepository = UserRepository()
    private val chatRepository = ChatRepository()
    private var messages: ArrayList<Chat>? = null
    var receiverId: String? = null
    var senderId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        receiverId = intent.getStringExtra("userId")
        senderId = userRepository.currentUser()!!.uid

        userRepository.findById(receiverId!!).addSnapshotListener { querySnap, error ->
            if (error != null) return@addSnapshotListener
            if (querySnap != null) {
                binding.username.text = querySnap.get("username").toString()
                if (querySnap.get("presence") == true) {
                    binding.userOnline.text = R.string.userPresence.toString()
                } else {
                    binding.userOnline.text = R.string.userPresenceOffline.toString()
                }
            }
        }

        getMessage()

        binding.btnSend.setOnClickListener {
            val txtMsg = binding.edtMessages.text.toString()
            if (txtMsg.isNotEmpty()) {
                sendMessage()
            }
        }
    }

    private fun sendMessage() {

        val chat = Chat(
            senderId = senderId!!,
            receiverId = receiverId!!,
            message = binding.edtMessages.text.toString(),
            timestamp = Date().time
        )

        chatRepository.addChat(chat).addOnSuccessListener {
            binding.recyclerMsg.scrollToPosition(messages!!.size - 1)
        }
    }

    private fun getMessage() {

        val sentQuery = chatRepository.getChat(senderId!!, receiverId!!)
        val receiveQuery = chatRepository.getChat(receiverId!!, senderId!!)

        sentQuery.addSnapshotListener { querySnap, error ->
            if (error != null) return@addSnapshotListener
            if (querySnap != null) {
                for (doc in querySnap.documents) {
                    val message = doc.toObject(Chat::class.java)
                    message?.let {
                        message.isReceiver = false
                        if (messages!!.contains(message)) {
                            messages!!.add(message)
                        }
                    }
                }
                if (messages!!.isNotEmpty()) {
                    val messagesOrder = messages!!.sortBy { it.timestamp } as ArrayList<Chat>
                    ChatAdapter(this, messagesOrder)
                    binding.recyclerMsg.scrollToPosition(messages!!.size - 1)
                }
            }
        }
        receiveQuery.addSnapshotListener { querySnap, error ->
            if (error != null) return@addSnapshotListener
            if (querySnap != null) {
                for (doc in querySnap.documents) {
                    val message = doc.toObject(Chat::class.java)
                    message?.let {
                        message.isReceiver = true
                        if (messages!!.contains(message)) {
                            messages!!.add(message)
                        }
                    }
                }
                if (messages!!.isNotEmpty()) {
                    val messagesOrder = messages!!.sortBy { it.timestamp } as ArrayList<Chat>
                    ChatAdapter(this, messagesOrder)
                    binding.recyclerMsg.scrollToPosition(messages!!.size - 1)
                }
            }
        }
    }
}