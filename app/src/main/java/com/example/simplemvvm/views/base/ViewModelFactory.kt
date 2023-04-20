package com.example.simplemvvm.views.base

import android.os.Build
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.savedstate.SavedStateRegistryOwner
import com.example.simplemvvm.ARG_SCREEN
import com.example.simplemvvm.App
import com.example.simplemvvm.MainViewModel
import java.lang.reflect.Constructor


inline fun <reified VM: ViewModel> BaseFragment.screenViewModel() = viewModels<VM> {
    val application = requireActivity().application as App
    val screen = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        requireArguments().getSerializable(ARG_SCREEN, BaseScreen::class.java)
    } else {
        requireArguments().getSerializable(ARG_SCREEN) as BaseScreen
    } ?: throw IllegalStateException("Each screen must has his own Screen class in his body")

    // using Providers API directly for getting MainViewModel instance
    val provider = ViewModelProvider(requireActivity(), AndroidViewModelFactory(application))
    val mainViewModel = provider[MainViewModel::class.java]

    // forming the list of available dependencies:
    // - singleton scope dependencies (repositories) -> from App class
    // - activity VM scope dependencies -> from MainViewModel
    // - screen VM scope dependencies -> screen args
    val dependencies = listOf(screen, mainViewModel) + application.models

    ViewModelFactory(dependencies, this)
}

class ViewModelFactory(
    private val dependencies: List<Any>,
    owner: SavedStateRegistryOwner
) : AbstractSavedStateViewModelFactory(owner, null) {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        val constructors = modelClass.constructors
        val constructor = constructors.maxBy { it.typeParameters.size }

        val dependenciesWithSavedState = dependencies + handle
        val arguments = findDependencies(constructor, dependenciesWithSavedState)

        return constructor.newInstance(*arguments.toTypedArray()) as T
    }

    private fun findDependencies(constructor: Constructor<*>, dependencies: List<Any>): List<Any> {
        val arg = mutableListOf<Any>()
        constructor.parameterTypes.forEach { parameterClass ->
            val dependency = dependencies.first { parameterClass.isAssignableFrom(it.javaClass) }
            arg.add(dependency)
        }
        return arg
    }
}