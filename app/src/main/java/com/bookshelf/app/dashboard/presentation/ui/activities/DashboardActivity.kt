package com.bookshelf.app.dashboard.presentation.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import com.bookshelf.app.R
import com.bookshelf.app.core.baseui.BaseActivity
import com.bookshelf.app.core.utils.collectLatestLifecycleFlow
import com.bookshelf.app.core.utils.setDoubleClickListener
import com.bookshelf.app.core.utils.showToast
import com.bookshelf.app.dashboard.data.models.BooksDataResponseItem
import com.bookshelf.app.dashboard.data.models.Year
import com.bookshelf.app.dashboard.presentation.ui.adapters.BooksAdapter
import com.bookshelf.app.dashboard.presentation.ui.adapters.YearAdapter
import com.bookshelf.app.dashboard.presentation.viewmodels.DashboardViewModel
import com.bookshelf.app.databinding.ActivityDashboardBinding
import com.bookshelf.app.registration.data.models.SessionResult
import com.bookshelf.app.registration.presentation.ui.activities.SignInActivity
import com.bookshelf.app.registration.presentation.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardActivity : BaseActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private lateinit var sessionManager: SessionManager
    private var lastSelectedItemId = R.id.tab_home
    private var searchJob: Job? = null

    private val yearAdapter by lazy {
        YearAdapter(onItemSelected = ::onYearItemSelected)
    }

    private val booksAdapter by lazy {
        BooksAdapter(
            onItemSelected = ::onBookItemSelected,
            onFavouriteSelected = ::onBookMarkedFavourite
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        apiObservers()
    }

    private fun initView() {
        sessionManager = SessionManager(dashboardViewModel.useCase.sessionRepo)
        binding.rvYear.adapter = yearAdapter
        binding.rvBooks.adapter = booksAdapter
        booksAdapter.setFragmentManager(supportFragmentManager)
    }

    private fun apiObservers() {
        collectLatestLifecycleFlow(dashboardViewModel.sortedYears) {
            yearAdapter.setData(it)
        }

        collectLatestLifecycleFlow(dashboardViewModel.sortedBooks) {
            booksAdapter.setData(it)
        }

        collectLatestLifecycleFlow(dashboardViewModel.apiError) {
            it?.message?.let { it1 -> showToast(it1) }
        }

        collectLatestLifecycleFlow(dashboardViewModel.isLoading) {
            showProgress(it)
        }

        collectLatestLifecycleFlow(sessionManager.sessionObserver()) {
            when (it) {
                is SessionResult.Active -> {

                }

                is SessionResult.NotActive -> {
                    startActivity(Intent(this, SignInActivity::class.java))
                    finishAffinity()
                }
            }
        }
    }


    override fun setOnclickListener() {
        binding.navBar.setOnItemSelectedListener { menuItem ->
            if (menuItem.itemId != lastSelectedItemId) {
                when (menuItem.itemId) {
                    R.id.tab_home -> {
                        booksAdapter.isFavouriteTabActive(false)
                        dashboardViewModel.getBooksFromDb()
                    }

                    R.id.tab_fav -> {
                        booksAdapter.isFavouriteTabActive(true)
                        dashboardViewModel.getFavouriteBooks()
                    }

                    R.id.tab_logout -> {
                        booksAdapter.isFavouriteTabActive(false)
                        showToast(getString(R.string.double_click_to_logout))
                    }
                }
                lastSelectedItemId = menuItem.itemId
            }
            true
        }

        binding.navBar.menu.findItem(R.id.tab_logout).setDoubleClickListener {
            sessionManager.clearSession()
        }

        binding.etSearchBar.doAfterTextChanged {
            val inputText = it.toString().trim()
            searchJob?.cancel()
            if (inputText.isNotEmpty()) {
                searchJob = CoroutineScope(Dispatchers.Main).launch {
                    delay(500)
                    dashboardViewModel.runSearchQuery(inputText)
                }
            } else {
                dashboardViewModel.getBooksFromDb()
            }
        }
    }

    private fun onBookItemSelected(position: Int, booksDataResponseItem: BooksDataResponseItem) {

    }

    private fun onYearItemSelected(position: Int, year: Year) {
        year.year?.let { dashboardViewModel.filterBooksByYear(it) }
    }

    private fun onBookMarkedFavourite(booksDataResponseItem: BooksDataResponseItem) {
        if (booksDataResponseItem.isFavourite == 1) {
            dashboardViewModel.markBookAsFavourite(booksDataResponseItem.id)
        } else {
            dashboardViewModel.unmarkFavouriteBook(booksDataResponseItem.id)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        searchJob?.cancel()
    }
}