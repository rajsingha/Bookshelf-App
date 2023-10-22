package com.bookshelf.app.dashboard.presentation.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bookshelf.app.R
import com.bookshelf.app.core.utils.clickWithDebounce
import com.bookshelf.app.dashboard.data.models.Year
import com.bookshelf.app.databinding.LayoutYearItemBinding

/**
 * `YearAdapter` is a RecyclerView adapter for displaying a list of years.
 *
 * @property onItemSelected A callback function that is invoked when a year item is selected.
 */
class YearAdapter(private val onItemSelected: (position: Int, year: Year) -> Unit) :
    RecyclerView.Adapter<YearAdapter.YearViewHolder>() {
    // Private properties
    private var years: MutableList<Year>? = null
    private var position = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YearViewHolder {
        // Create and return a new `YearViewHolder` instance.
        return YearViewHolder(
            LayoutYearItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            parent.context,
            onItemSelected
        )
    }

    override fun onBindViewHolder(holder: YearViewHolder, position: Int) {
        // Bind data to the `YearViewHolder` based on the item position.
        this.position = holder.bindingAdapterPosition
        years?.get(position)?.let {
            holder.bind(it, this)
        }
    }

    override fun getItemCount(): Int {
        // Return the number of items to be displayed in the RecyclerView.
        return years?.size ?: 0
    }

    /**
     * `YearViewHolder` is an inner class for holding and managing views for each year item.
     */
    class YearViewHolder(
        private val binding: LayoutYearItemBinding,
        private val context: Context,
        private val onItemSelected: (position: Int, year: Year) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(year: Year, adapter: YearAdapter) {
            if (year.isActive) {
                binding.tvYear.setBackgroundColor(context.getColor(R.color.grey))
            } else {
                binding.tvYear.setBackgroundColor(context.getColor(R.color.white))
            }
            binding.tvYear.text = year.year.toString()
            binding.tvYear.clickWithDebounce {
                // Handle single item selection and invoke the callback.
                adapter.singleSelection(year)
                onItemSelected.invoke(bindingAdapterPosition, year)
            }
        }
    }

    private fun singleSelection(year: Year) {
        // Ensure that only one year item is selected at a time.
        years?.forEach {
            if (it.isActive) {
                it.isActive = false
            }
        }
        year.isActive = true
        notifyDataSetChanged()
    }

    /**
     * Set the data for the adapter to display.
     */
    fun setData(dataList: MutableList<Year>) {
        this.years = dataList
        notifyDataSetChanged()
    }
}
