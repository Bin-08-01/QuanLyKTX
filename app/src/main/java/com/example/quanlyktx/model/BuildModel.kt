package com.example.quanlyktx.model

class BuildModel(
    val id: String? = null,
    val status: String? = null,
    val note: String? = null,
    val dateMaintain: String? = null,
    val numFloors: String? = null,
    val numRooms: String? = null,
    val emptyRooms: String? = null,
    val floor: ArrayList<FloorModel>? = null
)