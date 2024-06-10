package com.example.edupulse.presentation.ui.personal_library

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.nasizae_edu_pulse.App
import com.example.nasizae_edu_pulse.data.local.room.LibraryDatabase
import com.example.nasizae_edu_pulse.databinding.FragmentPersonalLibraryBinding
import com.example.nasizae_edu_pulse.presentation.ui.personal_library.PersonalLibraryViewModel
import com.example.nasizae_edu_pulse.presentation.ui.personal_library.adapter.PersonalLibraryAdapter
import com.google.firebase.firestore.FirebaseFirestore

class PersonalLibraryFragment : Fragment() {

    private var _binding: FragmentPersonalLibraryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PersonalLibraryAdapter
    private val firestoreDb=FirebaseFirestore.getInstance()
    private val personalLibraryViewModel=PersonalLibraryViewModel(App.personalLibraryDataBase)
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
        adapter= PersonalLibraryAdapter(this::onItemClick)
        personalLibraryViewModel.allItems.let {
            adapter.updateData(it)
        }
        binding.rvPersonalLibrary.adapter=adapter
    }

    private fun initRoom() {
        binding.search.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val query=s.toString()
                if(query.isNotEmpty()){
                    personalLibraryViewModel.search(query){
                        adapter.updateData(it)
                    }
                }
                else{
                    adapter.updateData(personalLibraryViewModel.allItems)
                }
            }
            override fun afterTextChanged(s: Editable?) {}

        })
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