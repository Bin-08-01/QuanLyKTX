package com.example.quanlyktx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.quanlyktx.databinding.ActivityHomeBinding
import com.example.quanlyktx.fragment.BuildFragment
import com.example.quanlyktx.fragment.UserFragment
import com.example.quanlyktx.store.LoginPreferences
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, BuildFragment.newInstance())
            addToBackStack(null)
            commit()
        }
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragment_container, BuildFragment.newInstance())
                        addToBackStack(null)
                        commit()
                    }
                    true
                }
                R.id.navigation_search -> {
                    if (LoginPreferences(applicationContext).getUserInfo().id === "admin") {
                        Toast.makeText(applicationContext, "Admin", Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(applicationContext, "Chỉ quản trị viên mới được sử dụng chức năng này", Toast.LENGTH_SHORT).show()
                    }
                    true
                }
                R.id.navigation_user -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragment_container, UserFragment.newInstance())
                        addToBackStack(null)
                        commit()
                    }
                    true
                }
                else -> false
            }
        }
    }
}