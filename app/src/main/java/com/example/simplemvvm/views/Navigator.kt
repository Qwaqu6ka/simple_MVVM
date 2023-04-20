package com.example.simplemvvm.views

import com.example.simplemvvm.views.base.BaseScreen

interface Navigator {

    fun launch(screen: BaseScreen)

    fun goBack(result: Any? = null)
}