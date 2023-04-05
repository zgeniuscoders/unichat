package cd.zgeniuscoders.unichat.models

import android.net.Uri

data class User(
    val id: String,
    val username: String,
    val name: String,
    val lastname: String,
    val profile: Uri? = null
)
