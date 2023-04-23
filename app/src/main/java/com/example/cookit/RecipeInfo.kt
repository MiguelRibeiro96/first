package com.example.cookit

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.net.URI


class RecipeInfo : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private lateinit var et_recipe: EditText
    private lateinit var et_ingredients: EditText
    private lateinit var et_description: EditText
    private lateinit var et_step_description: EditText
    private lateinit var et_cookingTime: EditText
    private lateinit var et_category: EditText
    private lateinit var savebtn: Button
    private lateinit var database: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference
    private lateinit var mChoose_image: Button
    private lateinit var mImageView: ImageView
    private lateinit var mImageUri: Uri
    private lateinit var mYotubeUrl: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_info)

        et_recipe = findViewById(R.id.et_recipe)
        et_ingredients = findViewById(R.id.et_ingredients)
        et_description = findViewById(R.id.et_description)
        et_cookingTime = findViewById(R.id.et_cookingTime)
        et_category = findViewById(R.id.et_category)
        mChoose_image = findViewById(R.id.btn_choose_image)
        mImageView = findViewById(R.id.image_view)
        savebtn = findViewById(R.id.save_recipe_btn)
        mYotubeUrl = findViewById(R.id.et_youtube)
        et_step_description = findViewById(R.id.et_step_description)



        database =
            FirebaseDatabase.getInstance("https://cookit-bda45-default-rtdb.europe-west1.firebasedatabase.app/")
        dbRef = FirebaseDatabase.getInstance().getReference("Recipes")


        mChoose_image.setOnClickListener {
            openFileChooser()
        }

        savebtn.setOnClickListener {
            Log.d("RecipeInfo", "save button clicked")
            saveRecipeData()
        }
    }


    private fun saveRecipeData() {
        //getting values
        val recipe_name = et_recipe.text.toString()
        val ingr = et_ingredients.text.toString()
        val descr = et_description.text.toString()
        val cooking_time = et_cookingTime.text.toString()
        val cat = et_category.text.toString()
        val step = et_step_description.text.toString()
        val yt = mYotubeUrl.text.toString()

        //get image path from uri
        val imageUri = mImageUri.toString()
        val imagePath: String? = if (imageUri.isNotEmpty()) imageUri else null

        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference


        //validate input fields
        if (recipe_name.isEmpty()) {
            et_recipe.error = "Please enter the recipe name"
            return
        }
        if (ingr.isEmpty()) {
            et_ingredients.error = "Please enter the ingredients"
            return
        }
        if (descr.isEmpty()) {
            et_description.error = "Please enter the description"
            return
        }
        if (cooking_time.isEmpty()) {
            et_cookingTime.error = "Please enter the cooking time"
            return
        }
        if (cat.isEmpty()) {
            et_category.error = "Please enter the category for the recipe"
            return
        }
        if (step.isEmpty()) {
            et_step_description.error = "Please enter the steps description"
            return
        }

        // create a reference to the location in Firebase Storage where the image will be stored
        val imageRef = storageRef.child("images/${mImageUri.lastPathSegment}")

        // upload the image to Firebase Storage
        val uploadTask = imageRef.putFile(mImageUri)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let { throw it }
            }
            // Continue with the task to get the download URL
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUrl = task.result.toString()

                //create new recipe instance with image download URL
                val recipeId = dbRef.push().key!! //null check !!
                val recipe =
                    Recipe(recipeId, recipe_name, descr, cooking_time, ingr, yt, downloadUrl, step)

                //save recipe data to Firebase
                dbRef.child(recipeId).setValue(recipe).addOnCompleteListener {
                    Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                    //clear input fields
                    et_recipe.text.clear()
                    et_ingredients.text.clear()
                    et_description.text.clear()
                    et_cookingTime.text.clear()
                    et_category.text.clear()
                    et_step_description.text.clear()
                    mYotubeUrl.text.clear()

                }.addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }

            } else {
                // Handle failures
                Toast.makeText(this, "Error uploading image", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
            && data != null
        ) {
            val uri = data.data
            if (uri != null) {
                mImageUri = uri
                Picasso.get().load(mImageUri).into(mImageView)
            }
        }
    }
}