package com.bookshelf.app.dashboard.presentation.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bookshelf.app.R
import com.bookshelf.app.core.utils.clickWithDebounce
import com.bookshelf.app.dashboard.data.models.Year
import com.bookshelf.app.databinding.LayoutYearItemBinding

class YearAdapter(private val onItemSelected: (position: Int, year: Year) -> Unit) :
    RecyclerView.Adapter<YearAdapter.YearViewHolder>() {
    private var years: MutableList<Year>? = null
    private var position = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YearViewHolder {
        return YearViewHolder(
            LayoutYearItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            parent.context,
            onItemSelected
        )
    }

    override fun onBindViewHolder(holder: YearViewHolder, position: Int) {
        this.position = holder.bindingAdapterPosition
        years?.get(position)?.let {
            holder.bind(it, this)
        }
    }

    override fun getItemCount(): Int {
        return years?.size ?: 0
    }

    class YearViewHolder(
        private val binding: LayoutYearItemBinding,
        private val context: Context,
        private val onItemSelected: (position: Int, year: Year) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(year: Year, adapter: YearAdapter) {
            if (year.isActive) {
                binding.tvYear.setBackgroundColor(context.getColor(R.color.grey))
            } else {
                binding.tvYear.setBackgroundColor(context.getColor(R.color.white))
            }
            binding.tvYear.text = year.year.toString()
            binding.tvYear.clickWithDebounce {
                adapter.singleSelection(year)
                onItemSelected.invoke(bindingAdapterPosition, year)
            }
        }
    }

    private fun singleSelection(year: Year) {
        years?.forEach {
            if (it.isActive) {
                it.isActive = false
            }
        }
        year.isActive = true
        notifyDataSetChanged()
    }

    fun setData(dataList: MutableList<Year>) {
        this.years = dataList
        notifyDataSetChanged()
    }
}
