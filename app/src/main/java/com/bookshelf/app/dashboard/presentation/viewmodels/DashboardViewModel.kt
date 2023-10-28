package com.bookshelf.app.dashboard.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bookshelf.app.core.network.NetworkResponse
import com.bookshelf.app.core.network.exceptionhandlers.ApiFailureException
import com.bookshelf.app.dashboard.data.models.BooksDataResponse
import com.bookshelf.app.dashboard.data.models.Year
import com.bookshelf.app.dashboard.data.tables.BookWithMetadata
import com.bookshelf.app.dashboard.data.tables.BooksEntity
import com.bookshelf.app.dashboard.data.tables.BooksMetaDataEntity
import com.bookshelf.app.dashboard.domain.usecase.DashboardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject

/**
 * ViewModel for the Dashboard feature, responsible for managing book data and user interactions.
 *
 * @param useCase The use case that provides the business logic for the Dashboard feature.
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(val useCase: DashboardUseCase) : ViewModel() {
    private val _isLoading = Channel<Boolean>()
    val isLoading = _isLoading.receiveAsFlow()

    private val _apiError = Channel<ApiFailureException?>()
    val apiError = _apiError.receiveAsFlow()

    private val _sortedYears = Channel<MutableList<Year>>()
    val sortedYears = _sortedYears.receiveAsFlow()

    private val _sortedBooks = Channel<MutableList<BookWithMetadata>>()
    val sortedBooks = _sortedBooks.receiveAsFlow()

    var activeUserEmail: String? = null

    init {
        getBooksList()
    }

    /**
     * Fetches books data. If the local database is empty, it fetches data from the API.
     * Otherwise, it retrieves data from the local database.
     */
    fun getBooksList() {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _isLoading.send(true)
            if (useCase.getBooksRowCount() == 0) {
                getBooksFromApi()
            } else {
                getBooksFromDb()
            }
        }
    }

    /**
     * Retrieves books with metadata from the local database, processes and sorts the data,
     * and sends the results to the UI.
     */
    fun getBooksFromDb() {
        viewModelScope.launch(Dispatchers.IO) {
            val booksEntity = useCase.getAllBooksWithMetadata()
            withContext(Dispatchers.Main.immediate) {
                booksEntity.let {
                    _sortedYears.send(processLocalEntityAndSortYears(it))
                    _sortedBooks.send(processLocalEntityAndSortByYear(it))
                }
                _isLoading.send(false)
            }
        }
    }

    /**
     * Fetches books data from the API, handling different network responses such as errors,
     * loading, and success. Updates the UI accordingly.
     */
    private fun getBooksFromApi() {
        viewModelScope.launch(Dispatchers.IO) {
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
                            _isLoading.send(false)
                            insertBooksDataIntoDb(it.data)
                        }
                    }
                }
            }
        }
    }

    /**
     * Converts API response data into [BooksEntity] objects and inserts them into the local
     * database. Triggers the retrieval of updated data from the database.
     */
    private fun insertBooksDataIntoDb(data: BooksDataResponse) {
        viewModelScope.launch {
            val booksEntity = convertToBooksEntities(data)
            useCase.insertBook(booksEntity)
            runBlocking {
                getBooksFromDb()
            }
        }
    }

    /**
     * Converts API response data into a list of [BooksEntity] objects.
     */
    private fun convertToBooksEntities(dataResponse: BooksDataResponse): MutableList<BooksEntity> {
        return dataResponse.map { item ->
            BooksEntity(
                uid = item.id,
                image = item.image,
                score = item.score,
                popularity = item.popularity,
                title = item.title,
                publishedChapterDate = convertTimestampToYear(item.publishedChapterDate).toLong(),
            )
        }.toMutableList()
    }

    /**
     * Processes locally stored data to extract unique publication years and sorts them
     * in descending order.
     */
    private fun processLocalEntityAndSortYears(booksData: MutableList<BookWithMetadata>): MutableList<Year> {
        val years = booksData.mapNotNull { it.book?.publishedChapterDate }
        val uniqueYears = years.toSet()
        val sortedYears = uniqueYears.sortedDescending()
        val yearList = sortedYears.map { Year(it.toInt()) }
        return yearList.toMutableList()
    }

    /**
     * Sorts local data by publication year in descending order, preparing it for UI presentation.
     */
    private fun processLocalEntityAndSortByYear(booksData: MutableList<BookWithMetadata>): MutableList<BookWithMetadata> {
        return booksData
            .sortedByDescending { it.book?.publishedChapterDate }
            .map { entity ->
                BookWithMetadata(
                    book = BooksEntity(
                        uid = entity.book?.uid,
                        image = entity.book?.image,
                        score = entity.book?.score,
                        popularity = entity.book?.popularity,
                        title = entity.book?.title,
                        publishedChapterDate = entity.book?.publishedChapterDate,
                    ),
                    metadata = BooksMetaDataEntity(
                        uid = entity.metadata?.uid.toString(),
                        userEmailId = entity.metadata?.userEmailId.toString(),
                        isFavourite = entity.metadata?.isFavourite,
                        tags = entity.metadata?.tags
                    )
                )
            }
            .toMutableList()
    }

    /**
     * Executes a search query for books based on a provided query string and updates
     * the UI with the search results.
     */
    fun runSearchQuery(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val searchBooks = useCase.searchBook(query)
            withContext(Dispatchers.Main.immediate) {
                searchBooks.let {
                    _sortedBooks.send(processLocalEntityAndSortByYear(it))
                }
            }
        }
    }

    /**
     * Filters and retrieves books published in a specific year, updating the UI with the filtered results.
     */
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

    /**
     * Unmarks a book as a favorite by updating its metadata. It requires the book's UID for identification.
     */
    fun unmarkFavouriteBook(uid: String?) {
        uid?.let {
            viewModelScope.launch(Dispatchers.Main.immediate) {
                activeUserEmail?.let { email -> useCase.unmarkBookAsFavorite(uid, email) }
            }
        }
    }

    /**
     * Marks a book as a favorite by updating its metadata. It also requires the book's UID for identification.
     */
    fun markBookAsFavourite(bookData: BookWithMetadata?) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            activeUserEmail?.let { email ->
                val bookMetadata =
                    bookData?.book?.uid?.let { uid ->
                        BooksMetaDataEntity(
                            userEmailId = email,
                            uid = uid,
                            isFavourite = 1,
                            tags = bookData.metadata?.tags
                        )
                    }
                if (bookMetadata != null) {
                    useCase.markBookAsFavorite(bookMetadata)
                }
            }
        }
    }

    /**
     * Retrieves and displays books marked as favorites by the user, updating the UI with these favorite books.
     */
    fun getFavouriteBooks() {
        viewModelScope.launch(Dispatchers.IO) {
            activeUserEmail?.let { email ->
                val filteredBooksByFav = useCase.getAllFavoriteBooksForUser(email)
                withContext(Dispatchers.Main.immediate) {
                    filteredBooksByFav?.let {
                        _sortedBooks.send(processLocalEntityAndSortByYear(it))
                    }
                } ?: _apiError.send(ApiFailureException("No data found!", code = 404))
            }
        }
    }

    /**
     * Converts a timestamp to a publication year. This function is used for date-related calculations.
     */
    private fun convertTimestampToYear(timestamp: Long?): Int {
        val calendar = Calendar.getInstance()
        if (timestamp != null) {
            calendar.timeInMillis = timestamp * 1000
        }
        return calendar.get(Calendar.YEAR)
    }

    /**
     * Saves user-added tags for a book in the database. It requires book metadata and user email for identification.
     */
    fun saveTags(metadata: BookWithMetadata) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            val bookMetadata = activeUserEmail?.let { email ->
                metadata.book?.uid?.let { uid ->
                    BooksMetaDataEntity(
                        userEmailId = email,
                        uid = uid,
                        isFavourite = metadata.metadata?.isFavourite,
                        tags = metadata.metadata?.tags
                    )
                }
            }
            if (bookMetadata != null) {
                useCase.updateBookMetaData(bookMetadata)
            }
        }
    }

}
