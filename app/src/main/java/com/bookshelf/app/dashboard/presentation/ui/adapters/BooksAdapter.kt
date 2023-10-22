package com.bookshelf.app.dashboard.presentation.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bookshelf.app.R
import com.bookshelf.app.core.utils.clickWithDebounce
import com.bookshelf.app.dashboard.data.models.BooksDataResponseItem
import com.bookshelf.app.dashboard.presentation.ui.BookInfoBottomSheetDialog
import com.bookshelf.app.databinding.LayoutBookCardBinding

/**
 * `BooksAdapter` is a RecyclerView adapter for displaying a list of books in a list or grid layout.
 *
 * @property onItemSelected A callback function that is invoked when a book item is selected.
 * @property onFavouriteSelected A callback function that is invoked when the favorite icon for a book is clicked.
 */
class BooksAdapter(
    private val onItemSelected: (position: Int, book: BooksDataResponseItem) -> Unit,
    private val onFavouriteSelected: (book: BooksDataResponseItem) -> Unit
) :
    RecyclerView.Adapter<BooksAdapter.BooksViewHolder>() {
    // Private properties
    private var bookList: MutableList<BooksDataResponseItem>? = null
    private var fragmentManager: FragmentManager? = null
    private var position = -1
    private var favouriteTabActive = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksViewHolder {
        // Create and return a new `BooksViewHolder` instance.
        return BooksViewHolder(
            LayoutBookCardBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            parent.context,
            fragmentManager,
            onItemSelected,
            onFavouriteSelected
        )
    }

    override fun onBindViewHolder(holder: BooksViewHolder, position: Int) {
        // Bind data to the `BooksViewHolder` based on the item position.
        this.position = holder.bindingAdapterPosition
        if (favouriteTabActive) {
            bookList?.filter { it.isFavourite == 1 }?.toMutableList()?.get(position)?.let {
                holder.bind(it, this)
            }
        } else {
            bookList?.get(position)?.let {
                holder.bind(it, this)
            }
        }
    }

    override fun getItemCount(): Int {
        // Return the number of items to be displayed in the RecyclerView.
        if (favouriteTabActive) {
            bookList?.filter { it.isFavourite == 1 }?.toMutableList().let {
                return it?.size ?: 0
            }
        } else {
            return bookList?.size ?: 0
        }
    }

    /**
     * `BooksViewHolder` is an inner class for holding and managing views for each book item.
     */
    class BooksViewHolder(
        private val binding: LayoutBookCardBinding,
        private val context: Context,
        private val fragmentManager: FragmentManager?,
        private val onItemSelected: (position: Int, book: BooksDataResponseItem) -> Unit,
        private val onFavouriteSelected: (book: BooksDataResponseItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: BooksDataResponseItem, booksAdapter: BooksAdapter) {
            book.run {
                // Bind book data to views within the `BooksViewHolder`.
                if (isFavourite == 1) {
                    binding.ivFav.setColorFilter(context.getColor(R.color.golden))
                } else {
                    binding.ivFav.setColorFilter(context.getColor(R.color.grey))
                }
                this.image?.let {
                    binding.ivBook.load(it)
                }
                this.score?.let {
                    binding.tvBookRating.text = it.toString()
                }
                this.title?.let {
                    binding.tvBookTitle.text = it
                }
                this.publishedChapterDate?.let {
                    binding.tvBookPublishYear.text =
                        context.getString(R.string.published_in_s, it.toString())
                }
            }

            // Set click listeners for book title and favorite icon.
            binding.tvBookTitle.clickWithDebounce {
                fragmentManager?.let {
                    // Show a bottom sheet dialog when the book title is clicked.
                    BookInfoBottomSheetDialog().show(it, book) { bookData ->
                        onFavouriteSelected.invoke(book)
                    }
                }

                onItemSelected.invoke(bindingAdapterPosition, book)
            }

            binding.ivFav.clickWithDebounce {
                // Toggle the favorite status of the book and update the view.
                booksAdapter.markOrUnmark(book)
            }
        }
    }

    private fun markOrUnmark(book: BooksDataResponseItem) {
        // Handle marking or unmarking a book as a favorite.
        val currentPosition = bookList?.indexOf(book) ?: -1
        if (currentPosition != -1) {
            val currentBook = bookList?.get(currentPosition)
            currentBook?.isFavourite = if (currentBook?.isFavourite == 1) 0 else 1
            if (currentBook?.isFavourite == 0 && favouriteTabActive) {
                bookList?.removeAt(currentPosition)
                notifyItemRemoved(currentPosition)
            } else {
                notifyItemChanged(currentPosition)
            }
        }
        onFavouriteSelected.invoke(book)
    }

    /**
     * Set the `FragmentManager` for handling fragment transactions.
     */
    fun setFragmentManager(fragmentManager: FragmentManager) {
        this.fragmentManager = fragmentManager
    }

    /**
     * Set whether the favorite tab is active or not.
     */
    fun isFavouriteTabActive(state: Boolean) {
        this.favouriteTabActive = state
    }

    /**
     * Set the data for the adapter to display.
     */
    fun setData(dataList: MutableList<BooksDataResponseItem>) {
        this.bookList = dataList
        notifyDataSetChanged()
    }
}
