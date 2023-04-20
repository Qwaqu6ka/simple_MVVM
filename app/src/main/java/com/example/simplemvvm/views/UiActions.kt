package com.example.simplemvvm.views

import androidx.annotation.StringRes

interface UiActions {

    fun toast(message: String)

    fun getString(@StringRes messageRes: Int, vararg args: Any): String
}