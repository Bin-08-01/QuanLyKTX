package com.example.quanlyktx.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quanlyktx.R
import com.example.quanlyktx.model.BillModel
import com.example.quanlyktx.model.BuildModel
import com.example.quanlyktx.store.LoginPreferences
import com.google.firebase.database.FirebaseDatabase
import kotlin.coroutines.coroutineContext

class BillAdapter(private var listBill: ArrayList<BillModel>, contextB: Context) :
    RecyclerView.Adapter<BillAdapter.BillViewHolder>() {
    private lateinit var listener: OnBuildClickListener
private var context = contextB
    interface OnBuildClickListener {
        fun onClickBuild(position: Int)
    }

    fun setOnItemClickListener(clickListener: OnBuildClickListener) {
        listener = clickListener
    }

    class BillViewHolder(itemView: View, clickListener: OnBuildClickListener) :
        RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                clickListener.onClickBuild(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bill_item, parent, false)
        return BillViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return listBill.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BillViewHolder, position: Int) {
        holder.itemView.apply {
            findViewById<TextView>(R.id.n_order_bill_item).text = "Hoá đơn của ${listBill[position].name}"
            findViewById<TextView>(R.id.date_bill_item).text = listBill[position].date
            findViewById<TextView>(R.id.name_id_user_bill_item).text = "${listBill[position].name} - ${listBill[position].idUser}"
            findViewById<TextView>(R.id.txt_build_room_item).text = "${listBill[position].room}"
            findViewById<TextView>(R.id.price_bill_item).text = "${listBill[position].price}"
            findViewById<Button>(R.id.btn_confirm_bill_item).setOnClickListener {
                handleConfirmBill(listBill[position].idUser.toString(), listBill[position].id.toString())
            }
        }
    }

    private fun handleConfirmBill(idUser: String, idBill: String) {
        val db = FirebaseDatabase.getInstance().getReference("Users/$idUser")
        val updates = hashMapOf<String, Any>(
            "expiry" to "01/01/2024",
            "status" to "Đã thanh toán",
        )
        db.updateChildren(updates)
        val dbR = FirebaseDatabase.getInstance().getReference("Bills/${idBill}")
        val update = hashMapOf<String, Any>(
            "status" to "Yes"
        )
        dbR.updateChildren(update)
        val user = LoginPreferences(context)
        user.setValue("status", "Đã thanh toán")
        user.setValue("expiry", "01/01/2024")
    }
}