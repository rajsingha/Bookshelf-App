package com.bookshelf.app.dashboard.presentation.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import coil.load
import com.bookshelf.app.R
import com.bookshelf.app.core.utils.clickWithDebounce
import com.bookshelf.app.dashboard.data.models.BooksDataResponseItem
import com.bookshelf.app.databinding.LayoutBottomsheetBookInfoBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BookInfoBottomSheetDialog : BottomSheetDialogFragment() {
    private lateinit var binding: LayoutBottomsheetBookInfoBinding
    private var clickedFavourite: (bookData: BooksDataResponseItem?) -> Unit = {}
    private var bookData: BooksDataResponseItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.setCancelable(true)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = LayoutBottomsheetBookInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        bookData?.let { data ->
            binding.ivBook.load(data.image)
            binding.tvBookTitle.text = data.title
            binding.tvBookPublishYear.text = data.publishedChapterDate.toString()
            binding.tvBookRating.text = data.score.toString()

            if (data.isFavourite == 1) {
                context?.getColor(R.color.golden)?.let { binding.ivFav.setColorFilter(it) }
            } else {
                context?.getColor(R.color.grey)?.let { binding.ivFav.setColorFilter(it) }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.ivFav.clickWithDebounce {
            if (bookData?.isFavourite == 1) {
                bookData?.isFavourite = 0
                context?.getColor(R.color.grey)?.let { binding.ivFav.setColorFilter(it) }
            } else {
                bookData?.isFavourite = 1
                context?.getColor(R.color.golden)?.let { binding.ivFav.setColorFilter(it) }
            }
            clickedFavourite.invoke(bookData)
        }
    }

    fun show(
        manager: FragmentManager,
        bookData: BooksDataResponseItem,
        clickedFavourite: (bookData: BooksDataResponseItem?) -> Unit
    ) {
        super.show(manager, BookInfoBottomSheetDialog::class.java.simpleName)
        this.clickedFavourite = clickedFavourite
        this.bookData = bookData
    }

    override fun onPause() {
        super.onPause()
        dialog?.dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dialog?.dismiss()
    }
}