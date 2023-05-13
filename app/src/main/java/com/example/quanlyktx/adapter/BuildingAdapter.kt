package com.example.quanlyktx.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quanlyktx.R
import com.example.quanlyktx.model.BuildModel

class BuildingAdapter(private var listBuild: ArrayList<BuildModel>) :
    RecyclerView.Adapter<BuildingAdapter.BuildingViewHolder>() {
    private lateinit var listener: OnBuildClickListener

    interface OnBuildClickListener {
        fun onClickBuild(position: Int)
    }

    fun setOnItemClickListener(clickListener: OnBuildClickListener) {
        listener = clickListener
    }

    class BuildingViewHolder(itemView: View, clickListener: OnBuildClickListener) :
        RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                clickListener.onClickBuild(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuildingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.build_item, parent, false)
        return BuildingViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return listBuild.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BuildingViewHolder, position: Int) {
        holder.itemView.apply {
            findViewById<TextView>(R.id.tv_name_build).text = "Toà nhà số ${listBuild[position].id}"
            findViewById<TextView>(R.id.tv_status_build).text = "${listBuild[position].status}"
            findViewById<TextView>(R.id.tv_roomStatus_build).text = "Phòng đã sử dụng: ${
                listBuild[position].emptyRooms?.toInt()?.let {
                    listBuild[position].numRooms?.toInt()?.minus(
                        it
                    )
                }
            }/${listBuild[position].numRooms}"
        }
    }
}