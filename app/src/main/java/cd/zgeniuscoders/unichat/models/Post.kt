package cd.zgeniuscoders.unichat.models

data class Post(
    val id: String,
    val userId: String,
    val content: String? = "",
    val image: String? = "",
    val isEvent: Boolean? = false,
    val postAt: String
) {
    constructor() : this("", "", "", "", false, "")
}
