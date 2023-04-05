package cd.zgeniuscoders.unichat.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class PostRepository {
    companion object {
        private const val POST_COLLECTION = "posts"
        val COLLECTION_REF = FirebaseFirestore.getInstance().collection(POST_COLLECTION)
    }

    fun getPosts(): DocumentReference {
        return COLLECTION_REF.document()
    }

    fun getPost(postId: String): DocumentReference {
        return COLLECTION_REF.document(postId)
    }

    fun addPost(key: String, post: HashMap<String, Any>): Task<Void> {
        return ChatRepository.COLLECTION_REF.document(key).set(post)
    }

    fun updatePost(postId: String, post: HashMap<String, Any>): Task<Void> {
        return COLLECTION_REF.document(postId).update(post)
    }

    fun deletePost(postId: String): Task<Void> {
        return COLLECTION_REF.document(postId).delete()
    }
}