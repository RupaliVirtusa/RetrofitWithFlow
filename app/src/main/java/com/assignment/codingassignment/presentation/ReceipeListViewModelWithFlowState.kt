package com.assignment.codingassignment.presentation

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assignment.codingassignment.R
import com.assignment.codingassignment.network.RecipeListState
import com.assignment.codingassignment.repository.RecipeRepository
import com.assignment.codingassignment.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class RecipeListFlowViewModel @Inject constructor(

    private val repository: RecipeRepository,
    @Named("auth_token") private val token: String,
    private val app: Application
) : ViewModel() {

    private val _uiState = MutableStateFlow<RecipeListState>(RecipeListState.Loading)

    val uiState: StateFlow<RecipeListState> = _uiState

    var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    private fun onError(s: String) {
        _uiState.value = RecipeListState.Error(s)
    }

    init {
        getAllRecipesFlow()
    }

    private fun getAllRecipesFlow() {
        if (NetworkHelper.isNetworkAvailable(app)) {
            job = viewModelScope.launch(exceptionHandler) {
                repository.searchWithFlow(token = token, page = 1, query = "Chicken")
                    .collect {
                        if (it != null) {
                            _uiState.value = it
                        }
                    }
            }
        } else {
            _uiState.value = RecipeListState.Error(app.getString(R.string.no_internet))
        }

    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}