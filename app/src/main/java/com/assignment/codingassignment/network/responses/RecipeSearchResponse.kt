package com.assignment.codingassignment.network.responses

import com.assignment.codingassignment.network.model.RecipeDto
import com.google.gson.annotations.SerializedName

data class RecipeSearchResponse(
    @SerializedName("count")
    var count: Int,

    @SerializedName("results")
    var recipes: List<RecipeDto>
)

