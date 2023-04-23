package com.example.cookit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cookit.adapters.RecipeAdapter
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var rpRecyclerview: RecyclerView? = null
    private lateinit var RecipeList: ArrayList<Recipe>
    private lateinit var dbref: DatabaseReference
    private lateinit var database: FirebaseDatabase



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        database = FirebaseDatabase.getInstance("https://cookit-bda45-default-rtdb.europe-west1.firebasedatabase.app/")


        rpRecyclerview = view.findViewById(R.id.recyclerView)

        rpRecyclerview?.layoutManager = LinearLayoutManager(requireActivity())
        rpRecyclerview?.setHasFixedSize(true)

        RecipeList = arrayListOf()


        getRecipeData()

        val addbtn = view.findViewById<FloatingActionButton>(R.id.add)
        addbtn.setOnClickListener {
            val intent = Intent(requireContext(), RecipeInfo::class.java)
            startActivity(intent)
        }
        // Inflate the layout for this fragment
        return view

    }

    private fun getRecipeData(){

        dbref = FirebaseDatabase.getInstance().getReference("Recipes")
        dbref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                RecipeList.clear()
                if (snapshot.exists()){
                    for (Recipesnap in snapshot.children){
                        val Recipedata = Recipesnap.getValue(Recipe::class.java)
                        RecipeList.add(Recipedata!!)
                    }
                    val mAdapter = RecipeAdapter(RecipeList)
                    rpRecyclerview?.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object:RecipeAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(context, RecipeDetails::class.java)

                            intent.putExtra("Recipe Name", RecipeList[position].name)
                            intent.putExtra("Description", RecipeList[position].description)
                            intent.putExtra("Time", RecipeList[position].time)
                            intent.putExtra("Ingredients", RecipeList[position].ingredients)
                            intent.putExtra("Youtube", RecipeList[position].youtube_url)
                            intent.putExtra("Steps", RecipeList[position].stepsInfo)
                            intent.putExtra("Image", RecipeList[position].imagePath)
                            startActivity(intent)
                        }

                    })

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}