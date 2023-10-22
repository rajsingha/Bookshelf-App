package com.bookshelf.app.dashboard.presentation.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.bookshelf.app.R
import com.bookshelf.app.core.baseui.BaseActivity
import com.bookshelf.app.core.utils.collectLatestLifecycleFlow
import com.bookshelf.app.core.utils.showToast
import com.bookshelf.app.dashboard.data.models.Year
import com.bookshelf.app.dashboard.presentation.ui.adapters.YearAdapter
import com.bookshelf.app.dashboard.presentation.viewmodels.DashboardViewModel
import com.bookshelf.app.databinding.ActivityDashboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : BaseActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private var lastSelectedItemId = R.id.tab_home

    private val yearAdapter by lazy {
        YearAdapter(onItemSelected = ::onYearItemSelected)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        apiObservers()
    }

    private fun initView() {
        dashboardViewModel.getBooksList()
        binding.rvYear.adapter = yearAdapter
    }

    private fun apiObservers() {
        collectLatestLifecycleFlow(dashboardViewModel.sortedYears) {
            yearAdapter.setData(it)
        }
        collectLatestLifecycleFlow(dashboardViewModel.apiError) {
            Log.e("TAG", it.toString())
        }
    }


    override fun setOnclickListener() {
        binding.navBar.setOnItemSelectedListener { menuItem ->
            if (menuItem.itemId != lastSelectedItemId) {
                when (menuItem.itemId) {
                    R.id.tab_home -> {
                        showToast("Home")
                        dashboardViewModel.getBooksFromApi()
                    }

                    R.id.tab_fav -> {
                        showToast("Fav")
                        dashboardViewModel.getBooksFromApi()
                    }

                    R.id.tab_logout -> {
                        showToast("Logout")
                        dashboardViewModel.getBooksFromApi()
                    }
                }
                lastSelectedItemId = menuItem.itemId
            }
            true
        }
    }

    private fun onYearItemSelected(position: Int, year: Year) {

    }
}