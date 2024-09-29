package com.assignment.codingassignment.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.assignment.codingassignment.R
import com.assignment.codingassignment.databinding.FragmentRecipeBinding
import com.assignment.codingassignment.network.RecipeListState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeListFragment : Fragment() {

    lateinit var binding: FragmentRecipeBinding
    private val viewModel: RecipeListViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecipeBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {
        val recipeAdapter = RecipeAdapter(emptyList())
        binding.rvRecipe.apply {
            layoutManager = context?.let { LinearLayoutManager(it) }
            setHasFixedSize(true)
            adapter = recipeAdapter
        }
        viewModel.alRecipeList.observe(viewLifecycleOwner) { recipeState ->
            when (recipeState) {
                is RecipeListState.Loading -> {
                    binding.progressCircular.visibility = View.VISIBLE
                }

                is RecipeListState.RecipeListLoaded -> {
                    val alRecipes = recipeState.response.recipes
                    binding.tvRecipeCount.text =
                        getString(R.string.recipe_count) + recipeState.response.count
                    binding.rvRecipe.visibility = View.VISIBLE
                    binding.progressCircular.visibility = View.GONE
                    recipeAdapter.updateReceipeList(alRecipes)
                }

                is RecipeListState.Error -> {
                    binding.tvRecipeCount.text = recipeState.message
                    binding.rvRecipe.visibility = View.GONE
                    binding.progressCircular.visibility = View.GONE
                }

                else -> {}
            }
        }
    }
}