package cd.zgeniuscoders.unichat.models

import android.net.Uri

data class Group(
    val id: String,
    val name: String,
    val description: String,
    val isPrivate: Boolean,
    val profile: Uri
)
