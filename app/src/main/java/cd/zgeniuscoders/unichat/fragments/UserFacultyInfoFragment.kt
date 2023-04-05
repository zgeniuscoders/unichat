package cd.zgeniuscoders.unichat.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import cd.zgeniuscoders.uniDepartment.repositories.DepartementRepository
import cd.zgeniuscoders.uniFaculty.repositories.FacultyRepository
import cd.zgeniuscoders.unichat.R
import cd.zgeniuscoders.unichat.databinding.FragmentUserFacultyInfoBinding
import cd.zgeniuscoders.unichat.models.Department
import cd.zgeniuscoders.unichat.models.Faculty
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.toObject

class UserFacultyInfoFragment : Fragment() {
    private lateinit var binding: FragmentUserFacultyInfoBinding
    private var facultyList = arrayListOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserFacultyInfoBinding.inflate(layoutInflater)

        getFaculties()

        binding.faculty.setOnItemClickListener { adapterView, view, i, l ->
            val facultyName = facultyList[binding.faculty.selectedItemPosition]
            getDepartment(facultyName)
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
        val departmentList = arrayListOf<String>()
        val departmentRepo = DepartementRepository()
        departmentRepo.getDepartments()
            .whereEqualTo("faculty", faculty)
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