package com.example.foundation.utils

typealias ResourceAction<T> = (T) -> Unit

class ResourceActions<T> {

    var resource: T? = null
    set(value) {
        field = value
        if (value != null) {
            actions.forEach { it(value) }
            actions.clear()
        }
    }

    private val actions = mutableListOf<ResourceAction<T>>()

    operator fun invoke(action: ResourceAction<T>) {
        val resource = this.resource
        if (resource == null) {
            actions += action
        }
        else {
            action(resource)
        }
    }

    fun clear() {
        actions.clear()
    }
}