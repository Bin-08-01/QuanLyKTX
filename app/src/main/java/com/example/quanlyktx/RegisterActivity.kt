package com.example.quanlyktx

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.quanlyktx.databinding.ActivityRegisterBinding
import com.example.quanlyktx.dialog.GenderDialog
import com.example.quanlyktx.model.UserModel
import com.example.quanlyktx.model.UserRegModel
import com.google.firebase.database.*
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var dbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbRef = FirebaseDatabase.getInstance().getReference("Users")
        binding.btnRegister.setOnClickListener {
            handleRegister()
        }
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }
        binding.edtDateReg.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->

                    val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
                    binding.edtDateReg.setText(selectedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }
        binding.edtGender.setOnClickListener {
            val dialog = GenderDialog(this, object : GenderDialog.GenderDialogListener {
                override fun onGenderSelected(gender: String) {
                    binding.edtGender.setText(gender)
                }
            })
            dialog.setCancelable(true)
            dialog.show()
        }
    }

    private fun handleRegister() {
        val idStudent = binding.edtIdReg.text.toString()
        val passwd = binding.edtPasswdReg.text.toString()
        val passwd2 = binding.edtPasswd2Reg.text.toString()
        val name = binding.edtNameReg.text.toString()
        val date = binding.edtDateReg.text.toString()
        val cls = binding.edtClassReg.text.toString()
        val dpm = binding.edtDpmReg.text.toString()
        if(idStudent.isEmpty()){
            Toast.makeText(applicationContext, "ID không được để trống", Toast.LENGTH_SHORT).show()
            return
        }
        if(name.isEmpty()){
            Toast.makeText(applicationContext, "Tên không được để trống", Toast.LENGTH_SHORT).show()
            return
        }
        if(date.isEmpty()){
            Toast.makeText(applicationContext, "Ngày sinh không được để trống", Toast.LENGTH_SHORT).show()
            return
        }
        if(cls.isEmpty()){
            Toast.makeText(applicationContext, "Lớp sinh hoạt không được để trống", Toast.LENGTH_SHORT).show()
            return
        }
        if(dpm.isEmpty()){
            Toast.makeText(applicationContext, "Khoa không được để trống", Toast.LENGTH_SHORT).show()
            return
        }
        if(passwd.length < 6){
            Toast.makeText(applicationContext, "Mật khẩu phải có 6 ký tự", Toast.LENGTH_SHORT).show()
            binding.edtPasswdReg.error = "MMật khẩu phải có 6 ký tự"
            return
        }
        if(passwd != passwd2){
            Toast.makeText(applicationContext, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show()
            binding.edtPasswd2Reg.error = "Mật khẩu không trùng khớp"
            return
        }
        dbRef.child(idStudent).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(applicationContext, "ID đã được đăng ký", Toast.LENGTH_SHORT).show()
                    binding.edtIdReg.error = "ID đã được đăng ký"
                } else {
                    val student = UserModel(idStudent, name, date, cls, dpm, passwd, "", "", "", binding.edtGender.text.toString(), "")
                    dbRef.child(idStudent).setValue(student)
                    Toast.makeText(applicationContext, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                }

            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(applicationContext, "Đã có lỗi xảy ra. Vui lòng thử lại!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}