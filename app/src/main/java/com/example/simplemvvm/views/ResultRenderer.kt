package com.example.simplemvvm.views

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.children
import com.example.foundation.views.BaseFragment
import com.example.foundation.models.Result
import com.example.simplemvvm.R
import com.example.simplemvvm.databinding.PartResultBinding

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

fun BaseFragment.onTryAgain(root: ViewGroup, onTryAgain: () -> Unit) {
    val button = root.findViewById<Button>(R.id.tryAgainButton)
    button.setOnClickListener { onTryAgain() }
}