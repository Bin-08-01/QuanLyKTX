package com.example.quanlyktx.model

class UserRegModel(
    val id: String? = null,
    val name: String? = null,
    val date: String? = null,
    val cls: String? = null,
    val department: String? = null,
    val passwd: String? = null,
)

class UserModel(
    val id: String? = null,
    val name: String? = null,
    val date: String? = null,
    val cls: String? = null,
    val department: String? = null,
    val passwd: String? = null,
    val dateJoined: String? = null,
    val status: String? = null,
    val expiry: String? = null,
    val gender: String? = null,
    val room: String? = null,
    val avatar: String? = null
)

class UserRegRoomModel(
    val cls: String? = null,
    val date: String? = null,
    val dateJoined: String? = null,
    val department: String? = null,
    val expiry: String? = null,
    val id: String? = null,
    val name: String? = null,
    val status: String? = null,
    val avatar: String? = null
)