package cd.zgeniuscoders.uniFaculty.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class FacultyRepository {
    companion object {
        private const val FACULTY_COLLECTION = "Facultys"
        val COLLECTION_REF = FirebaseFirestore.getInstance().collection(FACULTY_COLLECTION)
    }

    fun getFaculties(): CollectionReference {
        return COLLECTION_REF
    }

    fun getFaculty(FacultyId: String): DocumentReference {
        return COLLECTION_REF.document(FacultyId)
    }

    fun addFaculty(key: String, Faculty: HashMap<String, Any>): Task<Void> {
        return COLLECTION_REF.document(key).set(Faculty)
    }

    fun updateFaculty(FacultyId: String, Faculty: HashMap<String, Any>): Task<Void> {
        return COLLECTION_REF.document(FacultyId).update(Faculty)
    }

    fun deleteFaculty(FacultyId: String): Task<Void> {
        return COLLECTION_REF.document(FacultyId).delete()
    }
}