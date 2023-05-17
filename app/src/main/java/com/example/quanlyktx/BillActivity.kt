package com.example.quanlyktx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quanlyktx.adapter.BillAdapter
import com.example.quanlyktx.databinding.ActivityBillBinding
import com.example.quanlyktx.model.BillModel
import com.example.quanlyktx.model.BuildModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BillActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBillBinding
    private lateinit var listBill: ArrayList<BillModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBillBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listBill = arrayListOf()
        getData()
    }

    private fun getData() {
        FirebaseDatabase.getInstance().getReference("Bills").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    listBill.clear()
                    for(child in snapshot.children){
                        val billData = child.getValue(BillModel::class.java)
                        if(billData?.status == "No"){
                            listBill.add(billData)
                        }
                    }
                    var size = 0
                    if(listBill.isNotEmpty()){
                        size = listBill.size
                    }
                    binding.txtTotalBill.text = "Hiện tại đang có ${size} đơn đang chờ phê duyệt"
                    binding.rcvListBill.layoutManager = LinearLayoutManager(applicationContext)
                    val adapter = BillAdapter(listBill, applicationContext)
                    adapter.setOnItemClickListener(object : BillAdapter.OnBuildClickListener{
                        override fun onClickBuild(position: Int) {

                        }

                    })
                    binding.rcvListBill.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}