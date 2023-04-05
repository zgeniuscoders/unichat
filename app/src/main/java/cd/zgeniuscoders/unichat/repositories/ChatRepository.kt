package cd.zgeniuscoders.unichat.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class ChatRepository {
    companion object {
        private const val CHAT_COLLECTION = "chats"
        val COLLECTION_REF = FirebaseFirestore.getInstance().collection(CHAT_COLLECTION)
    }

    fun getPChats(): DocumentReference {
        return COLLECTION_REF.document()
    }

    fun getChat(chatId: String): DocumentReference {
        return COLLECTION_REF.document(chatId)
    }

    fun addChat(key: String, chat: HashMap<String, Any>): Task<Void> {
        return COLLECTION_REF.document(key).set(chat)
    }

    fun updateChat(chatId: String, chat: HashMap<String, Any>): Task<Void> {
        return COLLECTION_REF.document(chatId).update(chat)
    }

    fun deleteChat(chatId: String): Task<Void> {
        return COLLECTION_REF.document(chatId).delete()
    }
}