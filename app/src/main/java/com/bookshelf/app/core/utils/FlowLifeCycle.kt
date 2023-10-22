package com.bookshelf.app.core.utils

import android.os.SystemClock
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Collects a Flow within the scope of an AppCompatActivity's lifecycle in the CREATED state.
 * It repeats the collection when the activity is recreated.
 *
 * @param flow The Flow to collect.
 * @param collect A suspend lambda function to handle each emitted item from the Flow.
 */
fun <T> AppCompatActivity.collectLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.CREATED) {
            flow.collectLatest(collect)
        }
    }
}


/**
 * Collects a Flow within the scope of an AppCompatActivity's or Fragment's lifecycle in the specified state (default is CREATED).
 * It repeats the collection when the lifecycle state matches.
 *
 * @param flow The Flow to collect.
 * @param state The desired state for lifecycle observation (default is CREATED).
 * @param collect A suspend lambda function to handle each emitted item from the Flow.
 */
fun <T> AppCompatActivity.collectLatestLifecycleFlow(
    flow: Flow<T>,
    state: Lifecycle.State = Lifecycle.State.CREATED,
    collect: suspend (T) -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(state) {
            flow.collectLatest(collect)
        }
    }
}


/**
 * Collects a Flow within the scope of a Fragment's lifecycle in the specified state (default is CREATED).
 * It repeats the collection when the lifecycle state matches.
 *
 * @param flow The Flow to collect.
 * @param viewLifecycleOwner The lifecycle owner associated with the Fragment's view.
 * @param state The desired state for lifecycle observation (default is CREATED).
 * @param collect A suspend lambda function to handle each emitted item from the Flow.
 */
fun <T> Fragment.collectLatestLifecycleFlow(
    flow: Flow<T>,
    viewLifecycleOwner: LifecycleOwner,
    state: Lifecycle.State = Lifecycle.State.CREATED,
    collect: suspend (T) -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(state) {
            flow.collectLatest(collect)
        }
    }
}


/**
 * Collects a Flow within the scope of a Fragment's lifecycle in the specified state (default is CREATED).
 * It repeats the collection when the lifecycle state matches.
 *
 * @param flow The Flow to collect.
 * @param viewLifecycleOwner The lifecycle owner associated with the Fragment's view.
 * @param state The desired state for lifecycle observation (default is CREATED).
 * @param collect A suspend lambda function to handle each emitted item from the Flow.
 */
fun <T> Fragment.collectLifecycleFlow(
    flow: Flow<T>,
    viewLifecycleOwner: LifecycleOwner,
    state: Lifecycle.State = Lifecycle.State.CREATED,
    collect: suspend (T) -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(state) {
            flow.collectLatest(collect)
        }
    }
}


/**
 * Sets up a back press handler for a Fragment or AppCompatActivity. It invokes the provided action
 * when the back button is pressed.
 *
 * @param owner The lifecycle owner for which the back press callback should be registered.
 * @param invoke The lambda function to execute when the back button is pressed.
 */
fun Fragment.handleBackPress(owner: LifecycleOwner, invoke: () -> Unit) {
    requireActivity().onBackPressedDispatcher.addCallback(
        owner,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                invoke.invoke()
            }
        })
}

/**
 * Attaches a click listener to a View with debounce functionality to prevent rapid clicks.
 *
 * @param debounceTime The time interval in milliseconds to ignore subsequent clicks.
 * @param action The lambda function to execute when the View is clicked.
 */
fun View.clickWithDebounce(debounceTime: Long = 800L, action: () -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0

        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            else action()
            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}


