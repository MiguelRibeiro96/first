package com.example.cookit

data class RecipeModel(
    var recipeId:String? = null,
    var recipeName:String? = null,
    var ingredients:String? = null,
    var description:String? = null,
    var recipeUrl: String? = null,
    var cookingTime: String? = null,
    var category:String? = null
)
