package com.example.simplemvvm

import android.app.Application
import com.example.foundation.BaseApplication
import com.example.foundation.models.coroutines.IoDispatcher
import com.example.foundation.models.coroutines.WorkerDispatcher
import com.example.simplemvvm.model.colors.InMemoryColorsRepository
import kotlinx.coroutines.Dispatchers

class App : Application(), BaseApplication {

    private val ioDispatcher = IoDispatcher(Dispatchers.IO)
    private val workerDispatcher = WorkerDispatcher(Dispatchers.Default)

    override val singletonScopeDependencies: List<Any> = listOf(
        InMemoryColorsRepository(ioDispatcher)
    )
}