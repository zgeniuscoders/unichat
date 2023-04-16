package cd.zgeniuscoders.unichat.models

data class Chat(
    val senderId: String,
    val receiverId: String,
    var message: String? = null,
    val image: String? = null,
    val isImage: Boolean = false,
    val isRead: Boolean = false,
    var isReceiver: Boolean = false,
    val timestamp: Long
)