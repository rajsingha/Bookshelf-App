package com.bookshelf.app.dashboard.presentation.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bookshelf.app.R
import com.bookshelf.app.core.utils.clickWithDebounce
import com.bookshelf.app.dashboard.data.models.BooksDataResponseItem
import com.bookshelf.app.databinding.LayoutBookCardBinding

class BooksAdapter(
    private val onItemSelected: (position: Int, book: BooksDataResponseItem) -> Unit,
    private val onFavouriteSelected: (position: Int, book: BooksDataResponseItem) -> Unit
) :
    RecyclerView.Adapter<BooksAdapter.BooksViewHolder>() {
    private var bookList: MutableList<BooksDataResponseItem>? = null
    private var position = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksViewHolder {
        return BooksViewHolder(
            LayoutBookCardBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            parent.context,
            onItemSelected,
            onFavouriteSelected
        )
    }

    override fun onBindViewHolder(holder: BooksViewHolder, position: Int) {
        this.position = holder.bindingAdapterPosition
        bookList?.get(position)?.let {
            holder.bind(it, this)
        }
    }

    override fun getItemCount(): Int {
        return bookList?.size ?: 0
    }

    class BooksViewHolder(
        private val binding: LayoutBookCardBinding,
        private val context: Context,
        private val onItemSelected: (position: Int, book: BooksDataResponseItem) -> Unit,
        private val onFavouriteSelected: (position: Int, book: BooksDataResponseItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: BooksDataResponseItem, booksAdapter: BooksAdapter) {
            book.run {
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

                binding.tvBookTitle.clickWithDebounce {
                    onItemSelected.invoke(bindingAdapterPosition, book)
                }

                binding.ivFav.clickWithDebounce {
                    onFavouriteSelected.invoke(bindingAdapterPosition, book)
                }
            }
        }
    }
}
