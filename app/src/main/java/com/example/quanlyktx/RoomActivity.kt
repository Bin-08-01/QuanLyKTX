package com.example.quanlyktx

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.quanlyktx.adapter.PersonAdapter
import com.example.quanlyktx.databinding.ActivityRoomBinding
import com.example.quanlyktx.model.RoomModel
import com.example.quanlyktx.model.UserModel
import com.example.quanlyktx.model.UserRegRoomModel
import com.example.quanlyktx.store.LoginPreferences
import com.google.firebase.database.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
            }
//            else if (roomInfo.persons?.isEmpty() != true) {
//                if (roomInfo.numPersons?.toInt()!! <= roomInfo.persons?.size!!) {
//                    Toast.makeText(
//                        this@RoomActivity,
//                        "Phòng này đã đủ người!!!",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//            }
            else {
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
            .orderByKey().limitToLast(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var lastKey = "0"
                    for (child in snapshot.children) {
                        lastKey = child.key!!
                    }
                    val newKey = (lastKey.toInt() + 1).toString()
                    val currentTime = LocalDateTime.now()
                    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    val timeJoined = currentTime.format(formatter)
                    FirebaseDatabase.getInstance()
                        .getReference("${intent.getStringExtra("link").toString()}/persons")
                        .child(newKey).setValue(
                            UserRegRoomModel(
                                userInfo.cls.toString(),
                                userInfo.date.toString(),
                                timeJoined,
                                userInfo.department.toString(),
                                "Đang chờ xử lý",
                                userInfo.id.toString(),
                                userInfo.name.toString(),
                                "Đang chờ xử lý",
                                userInfo.avatar
                            )
                        )
                    Toast.makeText(
                        this@RoomActivity,
                        "Đăng ký thành công, vui lòng thanh toán ở tổ ktx!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
                    LoginPreferences(applicationContext).setValue("room", roomInfo.id.toString())
                    LoginPreferences(applicationContext).setValue("dateJoined", timeJoined)
                    LoginPreferences(applicationContext).setValue("status", "Đang chờ xử lý")
                    updateInfoUser(
                        LoginPreferences(applicationContext).getUserInfo().id.toString(),
                        roomInfo.id.toString(),
                        timeJoined
                    )
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@RoomActivity,
                        "Có lỗi xảy ra, vui lòng thử lại sau!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })

    }

    private fun updateInfoUser(toString: String, room: String, timeJoined: String) {
        val db = FirebaseDatabase.getInstance().getReference("Users/$toString")
        val updates = hashMapOf<String, Any>(
            "room" to room,
            "dateJoined" to timeJoined,
            "status" to "Đang chờ xử lý"
        )
        db.updateChildren(updates)
    }

    private fun getData() {
        dbRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    roomInfo = snapshot.getValue(RoomModel::class.java)!!
                    binding.tvIdroomDetail.text = "Phòng ${roomInfo.id}"
                    binding.priceRoomDetail.text = roomInfo.price
                    binding.noteRoomDetail.text = roomInfo.note
                    binding.statusRoomDetail.text = roomInfo.status
                    binding.typeRoomDetail.text = roomInfo.type
                    binding.personRoomDetail.text = roomInfo.numPersons
                    binding.titleListUser.text = "Danh sách người ở (${roomInfo.persons?.size?.minus(
                        1
                    )}/${roomInfo.numPersons})"
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
                                val intentUserInfo = Intent(applicationContext, UserInfoActivity::class.java)
                                intentUserInfo.putExtra("idUser", listUser[position].id)
                                startActivity(intentUserInfo)
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