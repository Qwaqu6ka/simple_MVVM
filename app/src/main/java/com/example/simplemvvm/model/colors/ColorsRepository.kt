package com.example.simplemvvm.model.colors

import com.example.foundation.models.Repository

typealias ColorListener = (NamedColor) -> Unit

interface ColorsRepository : Repository {
    suspend fun getAvailableColors(): List<NamedColor>

    suspend fun getById(id: Long): NamedColor

    suspend fun getCurrentColor(): NamedColor

    suspend fun setCurrentColor(color: NamedColor)

    fun addListener(listener: ColorListener)

    fun removeListener(listener: ColorListener)
}