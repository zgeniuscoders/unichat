package cd.zgeniuscoders.unichat.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {
    companion object {
        private const val USER_COLLECTION = "users"
        val COLLECTION_REF = FirebaseFirestore.getInstance().collection(USER_COLLECTION)
    }

    fun getUsers(): DocumentReference {
        return COLLECTION_REF.document()
    }

    fun getUser(UserId: String): DocumentReference {
        return COLLECTION_REF.document(UserId)
    }

    fun addUser(key: String, User: HashMap<String, Any>): Task<Void> {
        return ChatRepository.COLLECTION_REF.document(key).set(User)
    }

    fun updateUser(UserId: String, User: HashMap<String, Any>): Task<Void> {
        return COLLECTION_REF.document(UserId).update(User)
    }

    fun deleteUser(UserId: String): Task<Void> {
        return COLLECTION_REF.document(UserId).delete()
    }
}