package com.example.simplemvvm.views.changecolor

import com.example.simplemvvm.model.colors.NamedColor

data class NamedColorListItem(
    val namedColor: NamedColor,
    val selected: Boolean
)
