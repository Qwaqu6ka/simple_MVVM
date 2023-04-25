package com.example.simplemvvm.model.colors

import com.example.foundation.models.Repository

typealias ColorListener = (NamedColor) -> Unit

interface ColorsRepository : Repository {
    var currentColor: NamedColor

    fun getAvailableColors(): List<NamedColor>

    fun getById(id: Long): NamedColor

    fun addListener(listener: ColorListener)

    fun removeListener(listener: ColorListener)
}