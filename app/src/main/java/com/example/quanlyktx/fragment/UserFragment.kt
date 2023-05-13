package com.example.quanlyktx.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.quanlyktx.MainActivity
import com.example.quanlyktx.R
import com.example.quanlyktx.databinding.FragmentUserBinding
import com.example.quanlyktx.model.UserModel
import com.example.quanlyktx.store.LoginPreferences

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

        }
        binding.changeRoomBtn.setOnClickListener {

        }
        binding.expiryBtn.setOnClickListener {

        }
        binding.cancelBtn.setOnClickListener {

        }
        binding.regRoomBtn.setOnClickListener {  }
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
        binding.genderInfo.text = userInfo.gender?. let { "Chưa cập nhập" }
        binding.dateInfo.text = userInfo.date
        binding.classInfo.text = userInfo.cls
        binding.departmentInfo.text = userInfo.department
        binding.roomInfo.text = userInfo.room?. let { "Bạn chưa đăng ký" }
        binding.dateJoinedInfo.text = userInfo.dateJoined?. let { "Đang cập nhập" }
        binding.expiryInfo.text = userInfo.expiry?. let { "Đang cập nhập" }
        binding.statusPriceInfo.text = userInfo.status?. let { "Đang cập nhập" }
    }

    companion object {
        fun newInstance() = UserFragment()
    }
}