package cd.zgeniuscoders.unichat.models

import android.net.Uri

data class User(
    val id: String,
    val username: String,
    val email: String,
    val profile: Uri? = null,
    val faculty: String? = null,
    val department: String? = null,
    val createdAt: Long? = null
)
