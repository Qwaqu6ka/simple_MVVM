package com.example.simplemvvm

import android.app.Application
import com.example.foundation.BaseApplication
import com.example.foundation.models.Repository
import com.example.simplemvvm.model.colors.InMemoryColorsRepository

class App : Application(), BaseApplication {

    override val repositories = listOf<Repository>(
        InMemoryColorsRepository()
    )
}