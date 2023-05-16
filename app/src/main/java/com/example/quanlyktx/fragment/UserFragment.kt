package com.example.quanlyktx.fragment

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.telecom.Call
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.quanlyktx.MainActivity
import com.example.quanlyktx.R
import com.example.quanlyktx.UserActivity
import com.example.quanlyktx.databinding.FragmentUserBinding
import com.example.quanlyktx.model.UserModel
import com.example.quanlyktx.store.LoginPreferences
import com.google.android.gms.common.api.Response
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    private lateinit var userInfo: UserModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserBinding.inflate(layoutInflater)
        // Button Event
        binding.btnChangeInfo.setOnClickListener {
            startActivity(Intent(context, UserActivity::class.java))
        }
        binding.btnLogout.setOnClickListener {
            LoginPreferences(requireContext()).clearInfo()
            startActivity(Intent(context, MainActivity::class.java))
        }
        getData()
        return binding.root
    }

    private fun getData() {
        userInfo = LoginPreferences(requireContext()).getUserInfo()
        binding.nameInfo.text = userInfo.name
        binding.idInfo.text = userInfo.id
        binding.genderInfo.text = userInfo.gender?.ifEmpty { "Chưa cập nhập" }
        binding.dateInfo.text = userInfo.date
        binding.classInfo.text = userInfo.cls
        binding.departmentInfo.text = userInfo.department
        binding.roomInfo.text = userInfo.room?.ifEmpty { "Chưa cập nhập" }
        binding.dateJoinedInfo.text = userInfo.dateJoined?.ifEmpty { "Chưa cập nhập" }
        binding.expiryInfo.text = userInfo.expiry?.ifEmpty { "Chưa cập nhập" }
        binding.statusPriceInfo.text = userInfo.status?.ifEmpty { "Chưa cập nhập" }
        try {
            val imageBytes = Base64.decode(userInfo.avatar, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            Glide.with(this).load(decodedImage).diskCacheStrategy(
                DiskCacheStrategy.ALL).circleCrop().into(binding.avatarUser)
        }catch (e: java.lang.Exception){
            Toast.makeText(context, "Cannot load image now", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun newInstance() = UserFragment()
    }
}