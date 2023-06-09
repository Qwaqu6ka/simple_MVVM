package com.example.simplemvvm.views.currentcolor

import com.example.foundation.models.PendingResult
import com.example.foundation.models.SuccessResult
import com.example.foundation.navigator.Navigator
import com.example.foundation.uiactions.UiActions
import com.example.foundation.views.BaseViewModel
import com.example.foundation.views.LiveResult
import com.example.foundation.views.MutableLiveResult
import com.example.simplemvvm.R
import com.example.simplemvvm.model.colors.ColorsRepository
import com.example.simplemvvm.model.colors.NamedColor
import com.example.simplemvvm.views.changecolor.ChangeColorFragment
import kotlinx.coroutines.launch

class CurrentColorViewModel(
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val colorsRepository: ColorsRepository
) : BaseViewModel() {

    private val _currentColor = MutableLiveResult<NamedColor>(PendingResult())
    val currentColor: LiveResult<NamedColor> = _currentColor

    init {
        viewModelScope.launch {
            colorsRepository.listenCurrentColor().collect {
                _currentColor.postValue(SuccessResult(it))
            }
        }
        load()
    }

    override fun onResult(result: Any) {
        super.onResult(result)
        if (result is NamedColor) {
            val message = uiActions.getString(R.string.changed_color, result.name)
            uiActions.toast(message)
        }
    }

    fun changeColor() {
        val currentColor = _currentColor.value?.takeSuccess() ?: return
        val screen = ChangeColorFragment.Screen(currentColor.id)
        navigator.launch(screen)
    }

    fun onTryAgain() {
        load()
    }

    private fun load() = into(_currentColor) { colorsRepository.getCurrentColor() }
}