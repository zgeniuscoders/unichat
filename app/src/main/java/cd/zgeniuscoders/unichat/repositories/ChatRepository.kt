package cd.zgeniuscoders.unichat.repositories

import cd.zgeniuscoders.unichat.models.Chat
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ChatRepository {
    companion object {
        private const val CHAT_COLLECTION = "chats"
        val COLLECTION_REF = FirebaseFirestore.getInstance().collection(CHAT_COLLECTION)
    }

    fun getChats(): DocumentReference {
        return COLLECTION_REF.document()
    }

    fun getChat(senderId: String, receiverId: String): Query {
        return COLLECTION_REF.whereEqualTo("senderId", senderId)
            .whereEqualTo("receiverId", receiverId)
            .orderBy("timestamp", Query.Direction.ASCENDING)
    }

    fun addChat(chat: Chat): Task<DocumentReference> {
        return COLLECTION_REF.add(chat)
    }

    fun updateChat(chatId: String, chat: HashMap<String, Any>): Task<Void> {
        return COLLECTION_REF.document(chatId).update(chat)
    }

    fun deleteChat(chatId: String): Task<Void> {
        return COLLECTION_REF.document(chatId).delete()
    }
}