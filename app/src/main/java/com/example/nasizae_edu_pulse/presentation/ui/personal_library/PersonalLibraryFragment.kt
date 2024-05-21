package com.example.edupulse.presentation.ui.personal_library

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.nasizae_edu_pulse.App
import com.example.nasizae_edu_pulse.databinding.FragmentPersonalLibraryBinding
import com.example.nasizae_edu_pulse.presentation.ui.personal_library.adapter.PersonalLibraryAdapter
import com.google.firebase.firestore.FirebaseFirestore

class PersonalLibraryFragment : Fragment() {

    private var _binding: FragmentPersonalLibraryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PersonalLibraryAdapter
    private val firestoreDb=FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalLibraryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRoom()
    }

    private fun initRoom() {
        val data= App.personalLibraryDataBase.libraryDao().getAll()
        adapter= PersonalLibraryAdapter(data,this::onItemClick)
        binding.rvPersonalLibrary.adapter=adapter
    }

    private fun onItemClick(numberLinks: Int) {
        firestoreDb.collection("Library").document("lybrarys").get().addOnSuccessListener {
           val links = (it.get("links") as? List<String>)
            if(links!=null) {
                val url=links[numberLinks]
                val uri=Uri.parse(url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
            }
            Log.e("ololo", "onItemClick: $numberLinks",)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}