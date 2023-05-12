package com.example.simplemvvm.model.colors

import com.example.foundation.models.Repository
import kotlinx.coroutines.flow.Flow


interface ColorsRepository : Repository {
    suspend fun getAvailableColors(): List<NamedColor>

    suspend fun getById(id: Long): NamedColor

    suspend fun getCurrentColor(): NamedColor

    fun setCurrentColor(color: NamedColor): Flow<Int>

    fun listenCurrentColor(): Flow<NamedColor>
}