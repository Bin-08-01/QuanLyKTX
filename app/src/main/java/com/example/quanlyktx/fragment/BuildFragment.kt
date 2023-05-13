package com.example.quanlyktx.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quanlyktx.FloorActivity
import com.example.quanlyktx.R
import com.example.quanlyktx.adapter.BuildingAdapter
import com.example.quanlyktx.databinding.FragmentBuildBinding
import com.example.quanlyktx.model.BuildModel
import com.example.quanlyktx.model.FloorModel
import com.google.firebase.database.*


class BuildFragment : Fragment() {
    private lateinit var binding: FragmentBuildBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var listBuilding: ArrayList<BuildModel>
//    private lateinit var listFloor: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        listBuilding = ArrayList()
//        listFloor = ArrayList()
        dbRef = FirebaseDatabase.getInstance().getReference("Buildings")
        binding = FragmentBuildBinding.inflate(layoutInflater)
        getBuildings()
        return binding.root
    }

    private fun getBuildings() {
        binding.rcvBuilding.visibility = View.GONE
        binding.tvLoading.visibility = View.VISIBLE
        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listBuilding.clear()
                if(snapshot.exists()){
                    for(child in snapshot.children){
                        val buildData = child.getValue(BuildModel::class.java)
                        listBuilding.add(buildData!!)
                    }
                    binding.rcvBuilding.layoutManager = GridLayoutManager(context, 2)
                    val adapter = BuildingAdapter(listBuilding)
                    adapter.setOnItemClickListener(object : BuildingAdapter.OnBuildClickListener{
                        override fun onClickBuild(position: Int) {
                            val intentBuild = Intent(context, FloorActivity::class.java)
                            intentBuild.putExtra("idBuild", listBuilding[position].id)
                            startActivity(intentBuild)
                        }

                    })
                    binding.rcvBuilding.adapter = adapter
                    binding.rcvBuilding.visibility = View.VISIBLE
                    binding.tvLoading.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    companion object {
        fun newInstance() =
            BuildFragment().apply {
            }
    }
}