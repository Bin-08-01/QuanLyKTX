package com.example.quanlyktx

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.quanlyktx.databinding.ActivityUserInfoBinding
import com.example.quanlyktx.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserInfoBinding
    private lateinit var idUser: String
    private lateinit var userCurrent: UserModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        idUser = intent.getStringExtra("idUser").toString()
        setContentView(binding.root)
        getData()
    }

    private fun getData() {
        FirebaseDatabase.getInstance().getReference("Users/$idUser")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        binding.nameUser.text = snapshot.child("name").value.toString()
                        binding.classInfo.text = snapshot.child("cls").value.toString()
                        binding.idInfo.text = snapshot.child("id").value.toString()
                        binding.dateInfo.text = snapshot.child("date").value.toString()
                        binding.departmentInfo.text = snapshot.child("department").value.toString()
                        binding.genderInfo.text = snapshot.child("gender").value.toString()
                        binding.dateJoinedInfo.text = snapshot.child("dateJoined").value.toString()
                        binding.roomInfo.text = snapshot.child("room").value.toString()
                        binding.statusPriceInfo.text = snapshot.child("status").value.toString()
                        binding.expiryInfo.text = snapshot.child("expiry").value.toString()
                        try {
                            val imageBytes = Base64.decode(snapshot.child("avatar").value.toString(), Base64.DEFAULT)
                            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                            Glide.with(applicationContext).load(decodedImage).diskCacheStrategy(
                                DiskCacheStrategy.ALL).circleCrop().into(binding.imageDetailUser)
                        }catch (e: java.lang.Exception){
                            Log.d("Error image", "Load image")
                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Người dùng này không tồn tại!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        applicationContext,
                        "Có lỗi, vui lòng thử lại sau!",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            })
    }
}