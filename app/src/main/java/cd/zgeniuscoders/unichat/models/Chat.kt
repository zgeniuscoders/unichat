package cd.zgeniuscoders.unichat.models

data class Chat(
    val id: String,
    val username: String,
    val message: String? = null,
    val image: String? = null,
    val isReceive: Boolean = true,
    val isRead: Boolean = false,
    val sendAt: Long
)