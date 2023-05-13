package com.example.quanlyktx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.quanlyktx.adapter.PersonAdapter
import com.example.quanlyktx.databinding.ActivityRoomBinding
import com.example.quanlyktx.model.RoomModel
import com.example.quanlyktx.model.UserModel
import com.example.quanlyktx.model.UserRegRoomModel
import com.example.quanlyktx.store.LoginPreferences
import com.google.firebase.database.*

class RoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRoomBinding
    private lateinit var roomInfo: RoomModel
    private lateinit var dbRef: DatabaseReference
    private lateinit var listUser: ArrayList<UserRegRoomModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listUser = arrayListOf()
        binding = ActivityRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbRef =
            FirebaseDatabase.getInstance().getReference(intent.getStringExtra("link").toString())
        binding.btnRegRoomDetailroom.setOnClickListener {
            if (LoginPreferences(applicationContext).getUserInfo().room?.length == 0) {
                handleRegister()
            } else {
                Toast.makeText(
                    this@RoomActivity,
                    "Bạn đã đăng ký phòng khác, vui lòng truy cập vào hồ sơ cá nhân!!!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        getData()
    }

    private fun handleRegister() {
        val userInfo = LoginPreferences(applicationContext).getUserInfo()
        FirebaseDatabase.getInstance()
            .getReference("${intent.getStringExtra("link").toString()}/persons")
            .child(userInfo.id.toString())
            .setValue(UserRegRoomModel(
                userInfo.cls.toString(),
                userInfo.date.toString(),
                userInfo.dateJoined.toString(),
                userInfo.department.toString(),
                userInfo.expiry.toString(),
                userInfo.id.toString(),
                userInfo.name.toString(),
                userInfo.status.toString()
            ))
        Toast.makeText(
            this@RoomActivity,
            "Đăng ký thành công, vui lòng thanh toán ở tổ ktx!!!",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun getData() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    roomInfo = snapshot.getValue(RoomModel::class.java)!!
                    binding.tvIdroomDetail.text = "Phòng ${roomInfo.id}"
                    binding.priceRoomDetail.text = roomInfo.price
                    binding.noteRoomDetail.text = roomInfo.note
                    binding.statusRoomDetail.text = roomInfo.status
                    binding.typeRoomDetail.text = roomInfo.type
                    binding.personRoomDetail.text = roomInfo.numPersons

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@RoomActivity,
                    "Có lỗi, vui lòng thử lại sau!!!",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
        FirebaseDatabase.getInstance()
            .getReference("${intent.getStringExtra("link").toString()}/persons")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (child in snapshot.children) {
                            child.getValue(UserRegRoomModel::class.java)?.let { listUser.add(it) }
                        }
                        binding.rcvListPerson.layoutManager =
                            LinearLayoutManager(applicationContext)
                        val adapter = PersonAdapter(listUser)
                        adapter.setOnItemClickListener(object :
                            PersonAdapter.OnPersonClickListener {
                            override fun onClickBuild(position: Int) {
                                Toast.makeText(
                                    this@RoomActivity,
                                    listUser[position].name,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        })
                        binding.rcvListPerson.adapter = adapter
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@RoomActivity,
                        "Có lỗi, vui lòng thử lại sau!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
    }
}