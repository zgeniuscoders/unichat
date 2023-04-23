package cd.zgeniuscoders.unichat.repositories

import cd.zgeniuscoders.unichat.models.Post
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class LikeRepository {
    companion object {
        private const val LIKE_COLLECTION = "likes"
        val COLLECTION_REF = FirebaseFirestore.getInstance().collection(LIKE_COLLECTION)
    }

    fun isLike(postId: String, userId: String): DocumentReference {
        return COLLECTION_REF.document(postId).collection("users")
            .document(userId)
    }

    fun getLikes(post: Post): CollectionReference {
        return COLLECTION_REF.document(post.id).collection("users")
    }

    fun addLike(postId: String, userId: String, like: HashMap<String, Any>): Task<Void> {
        return COLLECTION_REF.document(postId).collection("users").document(userId)
            .set(like)
    }

}