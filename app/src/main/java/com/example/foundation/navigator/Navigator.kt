package com.example.foundation.navigator

import com.example.foundation.views.BaseScreen

interface Navigator {

    fun launch(screen: BaseScreen)

    fun goBack(result: Any? = null)
}