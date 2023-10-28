package com.bookshelf.app.dashboard.presentation.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import coil.load
import com.bookshelf.app.R
import com.bookshelf.app.core.utils.clickWithDebounce
import com.bookshelf.app.dashboard.data.tables.BookWithMetadata
import com.bookshelf.app.dashboard.data.tables.BooksMetaDataEntity
import com.bookshelf.app.databinding.LayoutAddTagsBinding
import com.bookshelf.app.databinding.LayoutBottomsheetBookInfoBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BookInfoBottomSheetDialog : BottomSheetDialogFragment() {
    private lateinit var binding: LayoutBottomsheetBookInfoBinding
    private lateinit var tagBinding: LayoutAddTagsBinding
    private var clickedFavourite: (bookData: BookWithMetadata?) -> Unit = {}
    private var saveTags: (bookData: BookWithMetadata) -> Unit = {}
    private var bookData: BookWithMetadata? = null
    private var tags: String = ""

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
            binding.ivBook.load(data.book?.image)
            binding.tvBookTitle.text = data.book?.title
            binding.tvBookPublishYear.text = data.book?.publishedChapterDate.toString()
            binding.tvBookRating.text = data.book?.score.toString()

            if (data.metadata?.isFavourite == 1) {
                context?.getColor(R.color.golden)?.let { binding.ivFav.setColorFilter(it) }
            } else {
                context?.getColor(R.color.grey)?.let { binding.ivFav.setColorFilter(it) }
            }

            processTags(data.metadata?.tags)
        }
    }

    private fun processTags(tags: String? = null) {
        tags?.let {
            val tagArray = tags.split(", ").toTypedArray() // Split the tags
            val stringBuilder = SpannableStringBuilder()

            for ((index, tag) in tagArray.withIndex()) {
                // Append the tag with a line break (or space)
                stringBuilder.append(tag)

                // Set a different color for the tag
                val startIndex = stringBuilder.length - tag.length
                val endIndex = stringBuilder.length
                val backgroundColorSpan = BackgroundColorSpan(resources.getColor(R.color.golden))
                stringBuilder.setSpan(
                    backgroundColorSpan,
                    startIndex,
                    endIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                // Append a line break (or space) if not the last tag
                if (index < tagArray.size - 1) {
                    stringBuilder.append("\n") // You can use "\n" for a line break or " " for a space
                }
            }
            binding.tvTags.text = stringBuilder
        }
    }

    override fun onResume() {
        super.onResume()
        binding.ivFav.clickWithDebounce {
            if (bookData?.metadata?.isFavourite == 1) {
                bookData?.metadata?.isFavourite = 0
                context?.getColor(R.color.grey)?.let { binding.ivFav.setColorFilter(it) }
            } else {
                bookData?.metadata?.isFavourite = 1
                context?.getColor(R.color.golden)?.let { binding.ivFav.setColorFilter(it) }
            }
            clickedFavourite.invoke(bookData)
        }

        binding.tvAddTag.clickWithDebounce {
            showAddTagsDialog()
        }
    }

    private fun showAddTagsDialog() {
        tagBinding = LayoutAddTagsBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.add_tags))
            .setView(tagBinding.root)
            .setPositiveButton(getString(R.string.save)) { dialog, which ->
                val enteredTags = tagBinding.etTags.text.toString()
                if (enteredTags.isNotEmpty()) {
                    processTags(enteredTags)
                    saveTags.invoke(
                        BookWithMetadata(
                            book = bookData?.book,
                            metadata = bookData?.metadata?.userEmailId?.let { email ->
                                bookData?.metadata?.uid?.let { uid ->
                                    BooksMetaDataEntity(
                                        userEmailId = email,
                                        uid = uid,
                                        tags = enteredTags,
                                        isFavourite = bookData?.metadata?.isFavourite
                                    )
                                }
                            }
                        )
                    )
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .create()

        dialog.show()
    }

    fun show(
        manager: FragmentManager,
        bookData: BookWithMetadata?,
        clickedFavourite: (bookData: BookWithMetadata?) -> Unit,
        saveTags: (bookData: BookWithMetadata) -> Unit,
    ) {
        Handler(Looper.getMainLooper()).post {
            if (manager.isStateSaved.not()) {
                super.show(manager, BookInfoBottomSheetDialog::class.java.simpleName)
            }
        }
        this.clickedFavourite = clickedFavourite
        this.saveTags = saveTags
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