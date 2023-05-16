package com.example.quanlyktx

import ChangePasswordDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import android.util.Base64
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.quanlyktx.databinding.ActivityUserBinding
import com.example.quanlyktx.dialog.GenderDialog
import com.example.quanlyktx.model.UserModel
import com.example.quanlyktx.store.LoginPreferences
import com.google.android.gms.common.api.Response
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding
    private lateinit var userInfo: UserModel
    private lateinit var avatar: String
    private val PICK_IMAGE_REQUEST = 1
    private var filePath: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userInfo = LoginPreferences(applicationContext).getUserInfo()
        avatar = userInfo.avatar.toString()
        getData()

        binding.btnSaveInfo.setOnClickListener {
            saveInfo()
        }
        binding.btnChangePasswd.setOnClickListener {
            val dialog =
                ChangePasswordDialog(this, object : ChangePasswordDialog.PasswordDialogListener {
                    override fun onChangePasswordSelected(
                        currentPassword: String,
                        newPassword: String,
                        confirmNewPassword: String
                    ) {
                        changePassword(currentPassword, newPassword, confirmNewPassword)
                    }
                })
            dialog.setCancelable(true)
            dialog.show()
        }
        binding.blockGender.setOnClickListener {
            val dialog = GenderDialog(this, object : GenderDialog.GenderDialogListener {
                override fun onGenderSelected(gender: String) {
                    binding.etGender.text = gender
                }
            })
            dialog.setCancelable(true)
            dialog.show()
        }
        binding.blockDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->

                    val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
                    binding.etDate.text = selectedDate
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }
        binding.avatarUser.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            try {
                val inputStream = contentResolver.openInputStream(filePath!!)
                val bytes = ByteArrayOutputStream()
                val buffer = ByteArray(1024)
                var bytesRead: Int
                while (inputStream!!.read(buffer).also { bytesRead = it } != -1) {
                    bytes.write(buffer, 0, bytesRead)
                }
                avatar = Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT)
                try {
                    val imageBytes = Base64.decode(avatar, Base64.DEFAULT)
                    val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    Glide.with(this).load(decodedImage).diskCacheStrategy(
                        DiskCacheStrategy.ALL).circleCrop().into(binding.imageUser)
                }catch (e: java.lang.Exception){
                    Toast.makeText(applicationContext, "Cannot load image now", Toast.LENGTH_SHORT).show()
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ) {
        if (newPassword != confirmNewPassword) {
            Toast.makeText(applicationContext, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT)
                .show()
            return
        } else if (newPassword.length < 6) {
            Toast.makeText(
                applicationContext,
                "Mật khẩu phải có ít nhất 6 ký tự",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        FirebaseDatabase.getInstance().getReference("Users/${userInfo.id}/passwd")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val passwd = snapshot.getValue(String::class.java)
                        if (passwd != currentPassword) {
                            Toast.makeText(
                                applicationContext,
                                "Mật khẩu hiện tại không đúng",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            val updates = hashMapOf<String, Any>(
                                "passwd" to newPassword
                            )
                            FirebaseDatabase.getInstance().getReference("Users/${userInfo.id}")
                                .updateChildren(updates)
                            Toast.makeText(
                                applicationContext,
                                "Đã đổi mật khẩu, vui lòng đăng nhập lại!",
                                Toast.LENGTH_SHORT
                            ).show()
                            LoginPreferences(applicationContext).clearInfo()
                            startActivity(Intent(applicationContext, MainActivity::class.java))
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        applicationContext,
                        "Có lỗi xảy ra, vui lòng thử lại sau",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            })


    }

    private fun saveInfo() {
        val db = FirebaseDatabase.getInstance().getReference("Users/${userInfo.id}")
        val updates = hashMapOf<String, Any>(
            "name" to binding.etName.text.toString(),
            "cls" to binding.etClass.text.toString(),
            "department" to binding.etDepartment.text.toString(),
            "gender" to binding.etGender.text.toString(),
            "date" to binding.etDate.text.toString(),
            "avatar" to avatar
        )
        db.updateChildren(updates)
        LoginPreferences(applicationContext).setValue("name", binding.etName.text.toString())
        LoginPreferences(applicationContext).setValue("cls", binding.etClass.text.toString())
        LoginPreferences(applicationContext).setValue(
            "department",
            binding.etDepartment.text.toString()
        )
        LoginPreferences(applicationContext).setValue("gender", binding.etGender.text.toString())
        LoginPreferences(applicationContext).setValue("date", binding.etDate.text.toString())
        LoginPreferences(applicationContext).setValue("avatar", avatar)
        Toast.makeText(applicationContext, "Đã thay đổi thông tin thành công!", Toast.LENGTH_SHORT)
            .show()
        startActivity(Intent(applicationContext, MainActivity::class.java))
    }

    private fun getData() {
        binding.etName.setText(userInfo.name)
        binding.etClass.setText(userInfo.cls)
        binding.etDepartment.setText(userInfo.department)
        binding.etGender.text = userInfo.gender
        binding.etDate.text = userInfo.date
        binding.etId.setText(userInfo.id)
        try {
            val imageBytes = Base64.decode(userInfo.avatar, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            Glide.with(this).load(decodedImage).diskCacheStrategy(
                DiskCacheStrategy.ALL).circleCrop().into(binding.imageUser)
        }catch (e: java.lang.Exception){
            Toast.makeText(applicationContext, "Cannot load image now", Toast.LENGTH_SHORT).show()
        }
    }
}