package cd.zgeniuscoders.unichat.repositories

import cd.zgeniuscoders.unichat.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class UserRepository {
    companion object {
        private const val USER_COLLECTION = "users"
        val COLLECTION_REF = FirebaseFirestore.getInstance().collection(USER_COLLECTION)
    }

    fun currentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    fun getUserByNumber(contactList: List<String>): Query {
        return COLLECTION_REF.whereArrayContains("number", contactList)
    }

    fun getUsers(): DocumentReference {
        return COLLECTION_REF.document()
    }

    fun getUser(userId: String): DocumentReference {
        return COLLECTION_REF.document(userId)
    }

    fun addUser(key: String, user: User): Task<Void> {
        return COLLECTION_REF.document(key).set(user)
    }

    fun updateUser(userId: String, user: HashMap<String, Any>): Task<Void> {
        return COLLECTION_REF.document(userId).update(user)
    }

    fun deleteUser(userId: String): Task<Void> {
        return COLLECTION_REF.document(userId).delete()
    }
}