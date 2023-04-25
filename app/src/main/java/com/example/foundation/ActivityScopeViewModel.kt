package com.example.foundation

import androidx.lifecycle.ViewModel
import com.example.foundation.navigator.IntermediateNavigator
import com.example.foundation.navigator.Navigator
import com.example.foundation.uiactions.UiActions


const val ARG_SCREEN = "arg_screen"

class ActivityScopeViewModel(
    val uiActions: UiActions,
    val navigator: IntermediateNavigator
) : ViewModel(), UiActions by uiActions, Navigator by navigator {

    override fun onCleared() {
        super.onCleared()
        navigator.clear()
    }
}