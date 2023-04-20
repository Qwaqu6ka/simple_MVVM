package com.example.simplemvvm.views.changecolor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.simplemvvm.R
import com.example.simplemvvm.model.colors.ColorsRepository
import com.example.simplemvvm.model.colors.NamedColor
import com.example.simplemvvm.views.Navigator
import com.example.simplemvvm.views.UiActions
import com.example.simplemvvm.views.changecolor.ChangeColorFragment.Screen
import com.example.simplemvvm.views.base.BaseViewModel

const val KEY_CURRENT_COLOR_ID = "current_color_id"
class ChangeColorViewModel(
    screen: Screen,
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val colorsRepository: ColorsRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(), ColorsAdapter.Listener {

    // init
    private val _currentColorId = savedStateHandle.getLiveData(KEY_CURRENT_COLOR_ID, screen.currentColorId)
    private val _availableColors = MutableLiveData<List<NamedColor>>()

    private val _colorsList = MediatorLiveData<List<NamedColorListItem>>()
    val colorsList: LiveData<List<NamedColorListItem>> = _colorsList

    private val _screenTitle = MutableLiveData<String>()
    val screenTitle: LiveData<String> = _screenTitle

    init {
        _availableColors.value = colorsRepository.getAvailableColors()

        _colorsList.addSource(_currentColorId) { mergeSources() }
        _colorsList.addSource(_availableColors) { mergeSources() }
    }

    override fun onColorChosen(namedColor: NamedColor) {
        _currentColorId.value = namedColor.id
    }

    fun onSavePressed() {
        val currentColorId = _currentColorId.value ?: return
        val currentColor = colorsRepository.getById(currentColorId)
        colorsRepository.currentColor = currentColor
        navigator.goBack(result = currentColor)
    }

    fun onCancelPressed() {
        navigator.goBack()
    }

    private fun mergeSources() {
        val colors = _availableColors.value ?: return
        val currentColorId = _currentColorId.value ?: return
        val currentColor = colors.first { it.id == currentColorId }
        _colorsList.value = colors.map { NamedColorListItem(it, it.id == currentColorId) }
        _screenTitle.value = uiActions.getString(R.string.change_color_screen_title, currentColor.name)
    }
}