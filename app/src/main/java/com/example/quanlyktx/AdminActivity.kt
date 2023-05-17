package com.example.quanlyktx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.quanlyktx.databinding.ActivityAdminBinding
import com.example.quanlyktx.model.BillModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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
        binding.btnUserAdmin.setOnClickListener { }
        getData()
    }

    private fun getData() {
        FirebaseDatabase.getInstance().getReference("Bills").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var i = 0
                if(snapshot.exists()){
                    for(child in snapshot.children){
                        val billData = child.getValue(BillModel::class.java)
                        if(billData?.status == "No"){
                            i++
                        }
                    }
                    binding.totalBill.text = "Đang có $i đơn cần xét duyệt"
                }else{
                    binding.totalBill.text = "Đang có 0 đơn cần xét duyệt"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                binding.totalBill.text = "Đang có 0 đơn cần xét duyệt"
            }
        })
    }
}