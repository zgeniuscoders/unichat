package cd.zgeniuscoders.unichat.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class LikeRepository {
    companion object {
        private const val LIKE_COLLECTION = "likes"
        val COLLECTION_REF = FirebaseFirestore.getInstance().collection(LIKE_COLLECTION)
    }

    fun getLikes(): CollectionReference {
        return COLLECTION_REF
    }

    fun getLike(LikeId: String): DocumentReference {
        return COLLECTION_REF.document(LikeId)
    }

    fun addLike(key: String, userId: String, like: HashMap<String, Any>): Task<Void> {
        return ChatRepository.COLLECTION_REF.document(key).collection("users").document(userId)
            .set(like)
    }

    fun updateLike(LikeId: String, userId: String, like: HashMap<String, Any>): Task<Void> {
        return COLLECTION_REF.document(LikeId).collection("users").document(userId).update(like)
    }

    fun deleteLike(likeId: String, userId: String): Task<Void> {
        return COLLECTION_REF.document(likeId).collection("users").document(userId).delete()
    }
}