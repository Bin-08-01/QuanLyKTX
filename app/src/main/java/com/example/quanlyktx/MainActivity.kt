package com.example.quanlyktx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.quanlyktx.databinding.ActivityMainBinding
import com.example.quanlyktx.fragment.UserFragment
import com.example.quanlyktx.model.UserModel
import com.example.quanlyktx.model.UserRegModel
import com.example.quanlyktx.store.LoginPreferences
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbRef = FirebaseDatabase.getInstance().getReference("Users")
        binding.btnLogin.setOnClickListener {
            handleLogin()
        }
        binding.btnRegister.setOnClickListener {
            val intentReg = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intentReg)
        }

    }

    private fun handleLogin() {
        val idStudent = binding.edtIdLogin.text.toString()
        val password = binding.edtPasswdLogin.text.toString()
        dbRef.child(idStudent).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    if (password == snapshot.child("passwd").value.toString()) {
                        val user = UserModel(
                            idStudent,
                            snapshot.child("name").value.toString(),
                            snapshot.child("date").value.toString(),
                            snapshot.child("cls").value.toString(),
                            snapshot.child("department").value.toString(),
                            password,
                            snapshot.child("dateJoined").value.toString(),
                            snapshot.child("status").value.toString(),
                            snapshot.child("expiry").value.toString(),
                            snapshot.child("gender").value.toString(),
                            snapshot.child("room").value.toString(),
                            )
                        LoginPreferences(applicationContext).saveInfo(user)
                        Toast.makeText(
                            applicationContext,
                            "Đănh nhập thành công",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(applicationContext, HomeActivity::class.java))
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "ID hoặc mật khẩu không đúng",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "ID hoặc mật khẩu không đúng",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    applicationContext,
                    "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                    Toast.LENGTH_SHORT
                ).show()

            }

        })
    }
}