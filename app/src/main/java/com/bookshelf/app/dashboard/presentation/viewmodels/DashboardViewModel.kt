package com.bookshelf.app.dashboard.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bookshelf.app.core.network.NetworkResponse
import com.bookshelf.app.core.network.exceptionhandlers.ApiFailureException
import com.bookshelf.app.dashboard.data.models.BooksDataResponse
import com.bookshelf.app.dashboard.data.models.BooksDataResponseItem
import com.bookshelf.app.dashboard.data.models.Year
import com.bookshelf.app.dashboard.data.tables.BooksEntity
import com.bookshelf.app.dashboard.domain.usecase.DashboardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(val useCase: DashboardUseCase) : ViewModel() {
    private val _isLoading = Channel<Boolean>()
    val isLoading = _isLoading.receiveAsFlow()

    private val _apiError = Channel<ApiFailureException?>()
    val apiError = _apiError.receiveAsFlow()

    private val _sortedYears = Channel<MutableList<Year>>()
    val sortedYears = _sortedYears.receiveAsFlow()

    private val _sortedBooks = Channel<MutableList<BooksDataResponseItem>>()
    val sortedBooks = _sortedBooks.receiveAsFlow()

    init {
        getBooksList()
    }

    private fun getBooksList() {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            if (useCase.getBooksRowCount() == 0) {
                getBooksFromApi()
            } else {
                getBooksFromDb()
            }
        }
    }

    fun getBooksFromDb() {
        viewModelScope.launch(Dispatchers.IO) {
            val booksEntity = useCase.getAllBooksFromDB()
            withContext(Dispatchers.Main.immediate) {
                booksEntity?.let {
                    _sortedYears.send(processLocalEntityAndSortYears(it))
                    _sortedBooks.send(processLocalEntityAndSortByYear(it))
                } ?: _apiError.send(ApiFailureException("Something went wrong", code = 500))
            }
        }
    }

    private fun getBooksFromApi() {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            useCase.getBooksInfoFromApi().collect {
                withContext(Dispatchers.Main.immediate) {
                    when (it) {
                        is NetworkResponse.Error -> {
                            _apiError.send(it.error)
                        }

                        is NetworkResponse.Loading -> {
                            _isLoading.send(it.isLoading)
                        }

                        is NetworkResponse.Success -> {
                            _sortedYears.send(processResponseAndSortYears(it.data))
                            _sortedBooks.send(processApiResponseAndSortByYear(it.data))
                            insertBooksDataIntoDb(it.data)
                        }
                    }
                }
            }
        }
    }


    private fun insertBooksDataIntoDb(data: BooksDataResponse) {
        viewModelScope.launch {
            val booksEntity = convertToBooksEntities(data)
            useCase.insertBook(booksEntity)
        }
    }

    private fun convertToBooksEntities(dataResponse: BooksDataResponse): MutableList<BooksEntity> {
        return dataResponse.map { item ->
            BooksEntity(
                uid = item.id,
                image = item.image,
                score = item.score,
                popularity = item.popularity,
                title = item.title,
                publishedChapterDate = convertTimestampToYear(item.publishedChapterDate).toLong(),
                isFavourite = item.isFavourite
            )
        }.toMutableList()
    }


    private fun processResponseAndSortYears(booksData: MutableList<BooksDataResponseItem>): MutableList<Year> {
        val years = booksData.mapNotNull {
            it.publishedChapterDate?.let { timestamp ->
                convertTimestampToYear(
                    timestamp
                )
            }
        }
        val uniqueYears = years.toSet()
        val sortedYears = uniqueYears.sortedDescending()
        val yearList = sortedYears.map { Year(it) }
        return yearList.toMutableList()
    }

    private fun processLocalEntityAndSortYears(booksData: MutableList<BooksEntity>): MutableList<Year> {
        val years = booksData.mapNotNull { it.publishedChapterDate }
        val uniqueYears = years.toSet()
        val sortedYears = uniqueYears.sortedDescending()
        val yearList = sortedYears.map { Year(it.toInt()) }
        return yearList.toMutableList()
    }

    private fun processLocalEntityAndSortByYear(booksData: MutableList<BooksEntity>): MutableList<BooksDataResponseItem> {
        return booksData
            .sortedByDescending { it.publishedChapterDate }
            .map { entity ->
                BooksDataResponseItem(
                    id = entity.uid,
                    image = entity.image,
                    score = entity.score,
                    popularity = entity.popularity,
                    title = entity.title,
                    publishedChapterDate = entity.publishedChapterDate,
                    isFavourite = entity.isFavourite
                )
            }
            .toMutableList()
    }


    private fun processApiResponseAndSortByYear(booksData: BooksDataResponse): MutableList<BooksDataResponseItem> {
        return booksData
            .sortedByDescending { convertTimestampToYear(it.publishedChapterDate) }
            .map { entity ->
                BooksDataResponseItem(
                    id = entity.id,
                    image = entity.image,
                    score = entity.score,
                    popularity = entity.popularity,
                    title = entity.title,
                    publishedChapterDate = convertTimestampToYear(entity.publishedChapterDate).toLong(),
                    isFavourite = entity.isFavourite
                )
            }
            .toMutableList()
    }

    fun runSearchQuery(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val searchBooks = useCase.searchBook(query)
            withContext(Dispatchers.Main.immediate) {
                searchBooks?.let {
                    _sortedBooks.send(processLocalEntityAndSortByYear(it))
                } ?: _apiError.send(ApiFailureException("No data found!", code = 404))
            }
        }
    }

    fun filterBooksByYear(year: Int) {
        if (year != 0) {
            viewModelScope.launch(Dispatchers.IO) {
                val filteredBooksByYear = useCase.filterBooksByYear(year.toLong())
                withContext(Dispatchers.Main.immediate) {
                    filteredBooksByYear?.let {
                        _sortedBooks.send(processLocalEntityAndSortByYear(it))
                    }
                } ?: _apiError.send(ApiFailureException("No data found!", code = 404))
            }
        }
    }

    fun unmarkFavouriteBook(uid: String?) {
        uid?.let {
            viewModelScope.launch(Dispatchers.Main.immediate) {
                useCase.unMarkFavouriteBook(it)
            }
        }
    }

    fun markBookAsFavourite(uid: String?) {
        uid?.let {
            viewModelScope.launch(Dispatchers.Main.immediate) {
                useCase.markBookAsFavourite(it)
            }
        }
    }

    fun getFavouriteBooks() {
        viewModelScope.launch(Dispatchers.IO) {
            val filteredBooksByFav = useCase.getFavouriteBooks()
            withContext(Dispatchers.Main.immediate) {
                filteredBooksByFav?.let {
                    _sortedBooks.send(processLocalEntityAndSortByYear(it))
                }
            } ?: _apiError.send(ApiFailureException("No data found!", code = 404))
        }
    }

    private fun convertTimestampToYear(timestamp: Long?): Int {
        val calendar = Calendar.getInstance()
        if (timestamp != null) {
            calendar.timeInMillis = timestamp * 1000
        }
        return calendar.get(Calendar.YEAR)
    }

}