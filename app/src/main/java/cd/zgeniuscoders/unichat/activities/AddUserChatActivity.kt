package cd.zgeniuscoders.unichat.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cd.zgeniuscoders.unichat.adapters.UserContactAdapter
import cd.zgeniuscoders.unichat.databinding.ActivityAddUserChatBinding
import cd.zgeniuscoders.unichat.models.User
import cd.zgeniuscoders.unichat.repositories.UserRepository
class AddUserChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddUserChatBinding
    private val REQUEST_CODE_READ_CONTACTS = 696
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAddUserChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // verifie si j'ai la permission d'acceder aux contact
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_CODE_READ_CONTACTS
            )
        } else {
            val contactList = getContactList()
            filterUserFirebase(contactList)
        }

    }

    @SuppressLint("Range")
    private fun getContactList(): List<String> {
        val contactList = mutableListOf<String>()
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null
        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val phone =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                contactList.add(phone)
            }
            cursor.close()
        }
        return contactList
    }

    private fun filterUserFirebase(contactList: List<String>) {

        val userList = ArrayList<User>()

        UserRepository().getUserByNumber(contactList).addSnapshotListener { querySnap, error ->
            if (error != null) return@addSnapshotListener
            if (querySnap != null) {
                userList.clear()
                for (doc in querySnap.documents) {
                    val user = doc.toObject(User::class.java)
                    userList.add(user!!)
                }
                binding.recyclerUserChat.adapter = UserContactAdapter(this, userList)
            }
        }
    }
}