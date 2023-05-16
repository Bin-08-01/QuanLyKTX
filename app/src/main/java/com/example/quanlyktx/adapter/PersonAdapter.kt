package com.example.quanlyktx.adapter

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.quanlyktx.R
import com.example.quanlyktx.model.BuildModel
import com.example.quanlyktx.model.UserModel
import com.example.quanlyktx.model.UserRegRoomModel

class PersonAdapter(private var listPerson: ArrayList<UserRegRoomModel>) :
    RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {
    private lateinit var listener: OnPersonClickListener

    interface OnPersonClickListener {
        fun onClickBuild(position: Int)
    }

    fun setOnItemClickListener(clickListener: OnPersonClickListener) {
        listener = clickListener
    }

    class PersonViewHolder(itemView: View, clickListener: OnPersonClickListener) :
        RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                clickListener.onClickBuild(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.person_item, parent, false)
        return PersonViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return listPerson.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.itemView.apply {
            val avatarContainer = findViewById<ImageView>(R.id.avatar_user_item)
            findViewById<TextView>(R.id.tv_name_person_room).text = listPerson[position].name
            findViewById<TextView>(R.id.tv_id_person_room).text = listPerson[position].id
            findViewById<TextView>(R.id.tv_date_joined_person_room).text = listPerson[position].dateJoined
            try {
                val imageBytes = Base64.decode(listPerson[position].avatar, Base64.DEFAULT)
                val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                Glide.with(this).load(decodedImage).diskCacheStrategy(
                    DiskCacheStrategy.ALL).circleCrop().into(avatarContainer)
            }catch (e: java.lang.Exception){
                Log.d("Error image", "Load image")
            }
        }
    }
}