package cd.zgeniuscoders.uniDepartment.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class DepartementRepository {
    companion object {
        private const val DEPARTMENT_COLLECTION = "departments"
        val COLLECTION_REF = FirebaseFirestore.getInstance().collection(DEPARTMENT_COLLECTION)
    }

    fun getDepartments(): CollectionReference {
        return COLLECTION_REF
    }

    fun getDepartment(DepartmentId: String): DocumentReference {
        return COLLECTION_REF.document(DepartmentId)
    }

    fun addDepartment(key: String, Department: HashMap<String, Any>): Task<Void> {
        return COLLECTION_REF.document(key).set(Department)
    }

    fun updateDepartment(DepartmentId: String, Department: HashMap<String, Any>): Task<Void> {
        return COLLECTION_REF.document(DepartmentId).update(Department)
    }

    fun deleteDepartment(DepartmentId: String): Task<Void> {
        return COLLECTION_REF.document(DepartmentId).delete()
    }
}