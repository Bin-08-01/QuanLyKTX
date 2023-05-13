package com.example.quanlyktx.model

class RoomModel(
    val id: String? = null,
    val status: String? = null,
    val note: String? = null,
    val numPersons: String? = null,
    val type: String? = null,
    val price: String? = null,
    val persons: ArrayList<UserRegRoomModel>? = null
)