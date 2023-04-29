package cd.zgeniuscoders.unichat.repositories

import cd.zgeniuscoders.unichat.models.Comment
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class CommentRepository {
    companion object {
        private const val COMMENT_COLLECTION = "comments"
        val COLLECTION_REF = FirebaseFirestore.getInstance().collection(COMMENT_COLLECTION)
    }

    fun all(): CollectionReference {
        return COLLECTION_REF.document().collection("users")
    }

    fun findById(postId: String): CollectionReference {
        return COLLECTION_REF.document(postId).collection("users")
    }

    fun create(postId: String, comment: Comment): Task<Void> {
        return ChatRepository.COLLECTION_REF.document(postId).collection("users").document()
            .set(comment)
    }

    fun update(postId: String, commentId: String, comment: HashMap<String, Any>): Task<Void> {
        return COLLECTION_REF.document(postId).collection("users").document(commentId)
            .update(comment)
    }

    fun delete(postId: String,commentId: String): Task<Void> {
        return COLLECTION_REF.document(postId).collection("users").document(commentId).delete()
    }
}