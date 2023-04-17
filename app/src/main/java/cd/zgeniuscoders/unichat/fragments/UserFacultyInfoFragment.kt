package cd.zgeniuscoders.unichat.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import cd.zgeniuscoders.uniDepartment.repositories.DepartementRepository
import cd.zgeniuscoders.uniFaculty.repositories.FacultyRepository
import cd.zgeniuscoders.unichat.R
import cd.zgeniuscoders.unichat.databinding.FragmentUserFacultyInfoBinding
import cd.zgeniuscoders.unichat.models.Department
import cd.zgeniuscoders.unichat.models.Faculty
import cd.zgeniuscoders.unichat.repositories.UserRepository
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.toObject

class UserFacultyInfoFragment : Fragment() {
    private lateinit var binding: FragmentUserFacultyInfoBinding
    private var facultyList = arrayListOf<String>()
    private var departmentList = arrayListOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserFacultyInfoBinding.inflate(layoutInflater)

        getFaculties()

        binding.faculty.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                p1: View?,
                positon: Int,
                p3: Long
            ) {
                val itemPos = parent!!.getItemAtPosition(positon).toString().toInt()
                val facultyName = facultyList[itemPos]
                getDepartment(facultyName)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        binding.btnNext.setOnClickListener {

            val department = departmentList[binding.departement.selectedItemPosition]
            val faculty = facultyList[binding.faculty.selectedItemPosition]

            val uuid = FirebaseAuth.getInstance().currentUser!!.uid

            val hash = HashMap<String, Any>()
            hash["faculty"] = faculty
            hash["department"] = department

            UserRepository().updateUser(uuid, hash).addOnCompleteListener {
//                findNavController().navigate(R.id.action_userFacultyInfoFragment_to_userProfileImageFragment)
            }
        }

        return binding.root
    }

    private fun getFaculties() {
        val facultyRepo = FacultyRepository()
        facultyRepo.getFaculties().addSnapshotListener { querySnap, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            if (querySnap != null) {
                facultyList.clear()
                for (doc in querySnap.documents) {
                    val data = doc.toObject(Faculty::class.java)
                    facultyList.add(data!!.name)
                }
                facultyList.add(0, R.string.choose_faculty.toString())
                val arrayAdapter =
                    ArrayAdapter(requireContext(), R.layout.dropdownn_item, facultyList)
                binding.faculty.adapter = arrayAdapter
            }
        }


    }

    private fun getDepartment(faculty: String) {
        departmentList = arrayListOf<String>()
        val departmentRepo = DepartementRepository()
        departmentRepo.getDepartments().whereEqualTo("faculty", faculty)
            .addSnapshotListener { querySnap, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                if (querySnap != null) {
                    departmentList.clear()
                    for (doc in querySnap.documents) {
                        val data = doc.toObject(Department::class.java)
                        departmentList.add(data!!.name)
                    }

                    departmentList.add(0, R.string.choose_department.toString())
                    val arrayAdapter =
                        ArrayAdapter(requireContext(), R.layout.dropdownn_item, departmentList)
                    binding.faculty.adapter = arrayAdapter
                }
            }


    }
}