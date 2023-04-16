package cd.zgeniuscoders.unichat.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class CommentRepository {
    companion object {
        private const val COMMENT_COLLECTION = "comments"
        val COLLECTION_REF = FirebaseFirestore.getInstance().collection(COMMENT_COLLECTION)
    }

    fun getComments(): CollectionReference {
        return COLLECTION_REF
    }

    fun getComment(commentId: String): DocumentReference {
        return COLLECTION_REF.document(commentId)
    }

    fun addComment(key: String, comment: HashMap<String, Any>): Task<Void> {
        return ChatRepository.COLLECTION_REF.document(key).set(comment)
    }

    fun updateComment(commentId: String, comment: HashMap<String, Any>): Task<Void> {
        return COLLECTION_REF.document(commentId).update(comment)
    }

    fun deleteComment(commentId: String): Task<Void> {
        return COLLECTION_REF.document(commentId).delete()
    }
}