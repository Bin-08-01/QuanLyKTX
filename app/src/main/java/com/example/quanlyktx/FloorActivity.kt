package com.example.quanlyktx

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.quanlyktx.adapter.RoomAdapter
import com.example.quanlyktx.databinding.ActivityFloorBinding
import com.example.quanlyktx.model.FloorModel
import com.example.quanlyktx.model.RoomModel
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue


class FloorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFloorBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var idBuild: String
    private lateinit var listFloor: ArrayList<String>
    private lateinit var listRoom: ArrayList<RoomModel>
    private lateinit var floorSelected: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFloorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        idBuild = intent.getStringExtra("idBuild").toString()
        floorSelected = "1"
        dbRef = FirebaseDatabase.getInstance().getReference("Buildings/$idBuild/floor")
        listRoom = arrayListOf()
        listFloor = arrayListOf("Tầng 1", "Tầng 2", "Tầng 3", "Tầng 4", "Tầng 5")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listFloor)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                floorSelected = parent.getItemAtPosition(position).toString().split(" ")[1]
                getRoomOfFloorSelected()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        getData()
        getRoomOfFloorSelected()
    }

    private fun getData() {
        binding.tvLoading.visibility = View.VISIBLE
        binding.rcvRoom.visibility = View.GONE
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    listFloor.clear()
                    for (child in snapshot.children) {
                        val floor = child.getValue(FloorModel::class.java)
                        if (floor != null) {
                            floor.id?.let { listFloor.add("Tầng $it") }
                        }
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Có lỗi, vui lòng thử lại sau",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    applicationContext,
                    "Có lỗi, vui lòng thử lại sau",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listFloor)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter

        binding.tvLoading.visibility = View.GONE
        binding.rcvRoom.visibility = View.VISIBLE
    }

    private fun getRoomOfFloorSelected() {
        binding.tvLoading.visibility = View.VISIBLE
        binding.rcvRoom.visibility = View.GONE
        FirebaseDatabase.getInstance()
            .getReference("/Buildings/$idBuild/floor/${floorSelected}/rooms")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listRoom.clear()
                    if (snapshot.exists()) {
                        for (child in snapshot.children) {
                            val room = child.getValue(RoomModel::class.java)
                            if (room != null) {
                                listRoom.add(room)
                            }
                        }

                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Có lỗi, vui lòng thử lại sau",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        applicationContext,
                        "Có lỗi, vui lòng thử lại sau",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        binding.rcvRoom.layoutManager = GridLayoutManager(this, 2)
        val adapterRoom = RoomAdapter(listRoom)
        adapterRoom.setOnItemClickListener(object : RoomAdapter.OnBuildClickListener {
            override fun onClickBuild(position: Int) {
                val intentRoom = Intent(applicationContext, RoomActivity::class.java)

                // Check here

                intentRoom.putExtra("link", "/Buildings/$idBuild/floor/${floorSelected}/rooms/${position+1}")
                startActivity(intentRoom)
            }
        })
        binding.rcvRoom.adapter = adapterRoom
        binding.tvLoading.visibility = View.GONE
        binding.rcvRoom.visibility = View.VISIBLE
    }
}