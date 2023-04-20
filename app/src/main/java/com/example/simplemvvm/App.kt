package com.example.simplemvvm

import android.app.Application
import com.example.simplemvvm.model.colors.InMemoryColorsRepository

class App : Application() {
    val models = listOf<Any>(
        InMemoryColorsRepository()
    )
}