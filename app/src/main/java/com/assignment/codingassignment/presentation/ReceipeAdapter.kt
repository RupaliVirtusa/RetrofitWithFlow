package com.assignment.codingassignment.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.assignment.codingassignment.R
import com.assignment.codingassignment.databinding.ListItemBinding
import com.assignment.codingassignment.network.model.RecipeDto
import com.bumptech.glide.Glide

class RecipeAdapter(private var recipeList: List<RecipeDto>) :
    RecyclerView.Adapter<RecipeAdapter.RecipeHolder>() {
    var inflater: LayoutInflater? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeHolder {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.context)
        }
        val itemBinding: ListItemBinding =
            DataBindingUtil.inflate(inflater!!, R.layout.list_item, parent, false)

        return RecipeHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    override fun onBindViewHolder(holder: RecipeHolder, position: Int) {
        holder.bind(recipeList[position])
    }

    fun updateReceipeList(list: List<RecipeDto>) {
        recipeList = list
        notifyDataSetChanged()
    }

    class RecipeHolder(itemView: ListItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var recipeView: ListItemBinding

        init {
            recipeView = itemView
        }

        companion object {
            @JvmStatic
            @BindingAdapter("featuredImage")
            fun loadImage(imageView: ImageView, url: String) {
                Glide.with(imageView.context)
                    .load(url)
                    .into(imageView)
            }
        }

        fun bind(recipe: RecipeDto) {
            recipeView.recipe = recipe
        }
    }
}