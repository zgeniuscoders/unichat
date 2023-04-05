package cd.zgeniuscoders.unichat.models

import android.net.Uri

data class Post(
    val id: String,
    val userId: String,
    val content: String? = null,
    val image: Uri? = null,
    val isEvent: Boolean? = false,
    val postAt: Long
)
