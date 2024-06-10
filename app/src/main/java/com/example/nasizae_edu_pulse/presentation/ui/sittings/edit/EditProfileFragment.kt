package com.example.edupulse.presentation.ui.sittings.edit

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.edupulse.data.model.Users
import com.example.edupulse.domain.usecase.RegistrationUseCase.Companion.USER
import com.example.nasizae_edu_pulse.R
import com.example.nasizae_edu_pulse.databinding.FragmentEditProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.network.UpdateMetadataNetworkRequest
import java.util.EventListener

class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var myDataBase:DatabaseReference
    private lateinit var auth:FirebaseAuth
    private lateinit var filePath: Uri
    private val storage=FirebaseStorage.getInstance()

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK &&
                result.data != null && result.data?.data != null
            ) {
                filePath = result.data!!.data!!
                try {
                    val bitmap: Bitmap
                    bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        filePath
                    )
                    binding.imgProfile.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentEditProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEditProfile()

        initListeners()
    }

    private fun initListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.navigation_sittings)
        }
        binding.imgProfile.setOnClickListener {
            val intent= Intent(Intent.ACTION_GET_CONTENT)
            intent.type="image/*"
            resultLauncher.launch(intent)
        }
    }

    private fun initEditProfile() {
        auth=FirebaseAuth.getInstance()
        val user:FirebaseUser?=auth.currentUser
        val uid:String?=user?.uid
        myDataBase=Firebase.database.getReference(USER)
        if(uid!=null){
            myDataBase.child(uid).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value=snapshot.getValue(Users::class.java)
                    binding.etFullName.setText(value?.fullName)
                    binding.etEmail.setText(value?.email)
                    binding.etPassword.setText(value?.password)
                    Glide.with(binding.imgProfile).load(value?.image).into(binding.imgProfile)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
                }

            })
            binding.btnEditProfile.setOnClickListener {
                val fullName=binding.etFullName.text.toString()
                val email=binding.etEmail.text.toString()
                val password=binding.etPassword.text.toString()
                upDateDataUser(uid,fullName,email,password)
                if(auth.currentUser!=null) {
                    auth.currentUser?.updatePassword(password)?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(requireContext(), "good", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
                        }
                    }
                    auth.currentUser?.verifyBeforeUpdateEmail(email)?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(requireContext(), "good email", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun upDateDataUser(uid: String, fullName: String, email: String, password: String) {
        storage.getReference().child("image/"+uid).putFile(filePath).addOnCompleteListener{
            storage.getReference().child("image/"+uid).downloadUrl.addOnSuccessListener {
                val data= Users(uid,fullName, email, password,it.toString())
                myDataBase.child(uid).setValue(data)
                findNavController().navigate(R.id.navigation_sittings)
            }
        }
    }

}