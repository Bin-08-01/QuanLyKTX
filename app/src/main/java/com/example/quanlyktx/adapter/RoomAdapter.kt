package com.example.quanlyktx.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quanlyktx.R
import com.example.quanlyktx.model.BuildModel
import com.example.quanlyktx.model.RoomModel

class RoomAdapter(private var listRoom: ArrayList<RoomModel>) :
    RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {
    private lateinit var listener: OnBuildClickListener

    interface OnBuildClickListener {
        fun onClickBuild(position: Int)
    }

    fun setOnItemClickListener(clickListener: OnBuildClickListener) {
        listener = clickListener
    }

    class RoomViewHolder(itemView: View, clickListener: OnBuildClickListener) :
        RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                clickListener.onClickBuild(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomAdapter.RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.room_item, parent, false)
        return RoomAdapter.RoomViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return listRoom.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RoomAdapter.RoomViewHolder, position: Int) {
        holder.itemView.apply {
            val size = if(listRoom[position].persons?.size == null) {0} else{listRoom[position].persons?.size}
            findViewById<TextView>(R.id.tv_idroom).text = "Phòng ${listRoom[position].id}"
            findViewById<TextView>(R.id.tv_status_room).text = listRoom[position].status
            findViewById<TextView>(R.id.tv_persons_room).text = "$size/${listRoom[position].numPersons}"
            findViewById<TextView>(R.id.tv_type_room).text = "Loại phòng: ${listRoom[position].type}"
        }
    }
}