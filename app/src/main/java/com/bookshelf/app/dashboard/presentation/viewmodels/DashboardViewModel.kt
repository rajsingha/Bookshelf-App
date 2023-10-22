package com.bookshelf.app.dashboard.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bookshelf.app.core.network.NetworkResponse
import com.bookshelf.app.core.network.exceptionhandlers.ApiFailureException
import com.bookshelf.app.dashboard.data.models.BooksDataResponseItem
import com.bookshelf.app.dashboard.data.models.Year
import com.bookshelf.app.dashboard.domain.usecase.DashboardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(val useCase: DashboardUseCase) : ViewModel() {
    private val _isLoading = Channel<Boolean>()
    val isLoading = _isLoading.receiveAsFlow()

    private val _apiError = Channel<ApiFailureException?>()
    val apiError = _apiError.receiveAsFlow()

    private val _sortedYears = Channel<MutableList<Year>>()
    val sortedYears = _sortedYears.receiveAsFlow()

    fun getBooksList() {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            if (useCase.getBooksRowCount() == 0) {
                getBooksFromApi()
            } else {
                getBooksFromDb()
            }
        }
    }

    private fun getBooksFromDb() {

    }

    fun getBooksFromApi() {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            useCase.getBooksInfoFromApi().collect {
                withContext(Dispatchers.Main.immediate) {
                    when (it) {
                        is NetworkResponse.Error -> _apiError.send(it.error)
                        is NetworkResponse.Loading -> _isLoading.send(it.isLoading)
                        is NetworkResponse.Success -> {
                            _sortedYears.send(processAndSortYears(it.data))
                            Log.e("TAG", it.data.toString())
                        }
                    }
                }
            }
        }
    }


    private fun processAndSortYears(booksData: MutableList<BooksDataResponseItem>): MutableList<Year> {
        val years = booksData.mapNotNull { it.publishedChapterDate }
        val uniqueYears = years.toSet()
        val sortedYears = uniqueYears.sortedDescending()
        val yearList = sortedYears.map { Year(it) }
        return yearList.toMutableList()
    }

}