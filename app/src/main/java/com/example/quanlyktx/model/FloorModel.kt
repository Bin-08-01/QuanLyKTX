package com.example.quanlyktx.model

class FloorModel(
    val id: String? = null,
    val status: String? = null,
    val note: String? = null,
    val numRooms: String? = null,
    val emptyRooms: String?=null,
    val rooms: ArrayList<RoomModel>? = null
)