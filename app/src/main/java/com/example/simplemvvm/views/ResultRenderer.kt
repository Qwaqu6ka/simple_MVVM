package com.example.simplemvvm.views

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.children
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.foundation.models.Result
import com.example.foundation.views.BaseFragment
import com.example.simplemvvm.R
import com.example.simplemvvm.databinding.PartResultBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Default [Result] rendering.
 * - if [result] is [PendingResult] -> only progress-bar is displayed
 * - if [result] is [ErrorResult] -> only error container is displayed
 * - if [result] is [SuccessResult] -> error container & progress-bar is hidden, all other views are visible
 */
fun <T> BaseFragment.renderSimpleResult(root: ViewGroup, result: Result<T>, onSuccess: (T) -> Unit) {
    val resultBinding = PartResultBinding.bind(root)
    renderResult(
        root = root,
        result = result,
        onSuccess = { successData ->
            root.children
                .filter { it.id != resultBinding.errorContainer.id && it.id != resultBinding.progressBar.id }
                .forEach { it.visibility = View.VISIBLE }
            onSuccess(successData)
        },
        onError = {
            resultBinding.errorContainer.visibility = View.VISIBLE
        },
        onPending = {
            resultBinding.progressBar.visibility = View.VISIBLE
        }
    )
}

/**
 * Collect items from the specified [Flow] only when fragment is at least in STARTED state.
 */
fun <T> BaseFragment.collectFlow(flow: Flow<T>, onCollect: (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect {
                onCollect(it)
            }
        }
    }
}

/**
 * Assign onClick listener for default try-again button.
 */
fun BaseFragment.onTryAgain(root: ViewGroup, onTryAgain: () -> Unit) {
    val button = root.findViewById<Button>(R.id.tryAgainButton)
    button.setOnClickListener { onTryAgain() }
}