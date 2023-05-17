package com.example.quanlyktx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.quanlyktx.databinding.ActivityAdminBinding

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBill.setOnClickListener {
            startActivity(Intent(applicationContext, BillActivity::class.java))
        }
        binding.btnBuildAdmin.setOnClickListener {

        }
        binding.btnUserAdmin.setOnClickListener {  }
    }
}