package com.example.simplemvvm.utils

class Event<T> (
    private val value: T
) {
    private var handled = false

    fun getValue(): T? {
        if (handled) return null
        handled = true
        return value
    }
}