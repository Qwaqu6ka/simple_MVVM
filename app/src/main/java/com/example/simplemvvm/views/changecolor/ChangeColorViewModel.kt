package com.example.simplemvvm.views.changecolor

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import com.example.foundation.models.EmptyProgress
import com.example.foundation.models.PendingResult
import com.example.foundation.models.PercentageProgress
import com.example.foundation.models.Progress
import com.example.foundation.models.Result
import com.example.foundation.models.SuccessResult
import com.example.foundation.models.getPercentage
import com.example.foundation.models.isInProgress
import com.example.foundation.navigator.Navigator
import com.example.foundation.uiactions.UiActions
import com.example.foundation.utils.finiteShareIn
import com.example.foundation.views.BaseViewModel
import com.example.foundation.views.ResultFlow
import com.example.foundation.views.ResultMutableStateFlow
import com.example.simplemvvm.R
import com.example.simplemvvm.model.colors.ColorsRepository
import com.example.simplemvvm.model.colors.NamedColor
import com.example.simplemvvm.views.changecolor.ChangeColorFragment.Screen
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.launch

const val KEY_CURRENT_COLOR_ID = "current_color_id"

class ChangeColorViewModel(
    screen: Screen,
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val colorsRepository: ColorsRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(), ColorsAdapter.Listener {

    private val _availableColors: ResultMutableStateFlow<List<NamedColor>> =
        MutableStateFlow(PendingResult())
    private val _currentColorId =
        savedStateHandle.getMutableStateFlow(KEY_CURRENT_COLOR_ID, screen.currentColorId)
    private val _instantSaveInProgress =
        MutableStateFlow<Progress>(EmptyProgress)  // for progressBar
    private val _sampledSaveInProgress =
        MutableStateFlow<Progress>(EmptyProgress)  // for progressTextView

    val viewState: ResultFlow<ViewState> = combine(
        _availableColors,
        _currentColorId,
        _instantSaveInProgress,
        _sampledSaveInProgress,
        ::mergeSources
    )

    val screenTitle: LiveData<String> = viewState
        .map { result ->
            return@map if (result is SuccessResult) {
                val currentColor = result.data.colorsList.first { it.selected }
                uiActions.getString(
                    R.string.change_color_screen_title,
                    currentColor.namedColor.name
                )
            } else {
                uiActions.getString(R.string.change_color_screen_title_simple)
            }
        }
        .asLiveData()

    init {
        load()
    }

    override fun onColorChosen(namedColor: NamedColor) {
        if (_instantSaveInProgress.value.isInProgress()) return
        _currentColorId.value = namedColor.id
    }

    @OptIn(FlowPreview::class)
    fun onSavePressed() = viewModelScope.launch {
        try {
            _instantSaveInProgress.value = PercentageProgress.START
            _sampledSaveInProgress.value = PercentageProgress.START

            val currentColorId = _currentColorId.value
            val currentColor = colorsRepository.getById(currentColorId)

            val flow = colorsRepository.setCurrentColor(currentColor).finiteShareIn(this)

            val instantJob = async {
                flow
                    .collect { percentage ->
                    _instantSaveInProgress.value = PercentageProgress(percentage)
                }
            }

            val sampleJob = async {
                flow.sample(200)
                    .collect { percentage ->
                        _sampledSaveInProgress.value = PercentageProgress(percentage)
                    }
            }

            instantJob.await()
            sampleJob.await()

            navigator.goBack(currentColor)
        } catch (e: Exception) {
            if (e !is CancellationException) uiActions.toast(uiActions.getString(R.string.cant_save_color))
        } finally {
            _instantSaveInProgress.value = EmptyProgress
            _sampledSaveInProgress.value = EmptyProgress
        }
    }

    fun onCancelPressed() {
        navigator.goBack()
    }

    fun onTryAgain() {
        load()
    }

    private fun mergeSources(
        colors: Result<List<NamedColor>>,
        currentColorId: Long,
        instantSaveInProgress: Progress,
        sampledSaveInProgress: Progress
    ): Result<ViewState> {

        return colors.map { colorsList ->
            ViewState(
                colorsList = colorsList.map { NamedColorListItem(it, it.id == currentColorId) },
                showSaveProgressBar = instantSaveInProgress.isInProgress(),
                showSaveButton = !instantSaveInProgress.isInProgress(),
                showCancelButton = !instantSaveInProgress.isInProgress(),

                saveProgressPercentage = instantSaveInProgress.getPercentage(),
                saveProgressPercentageMessage = uiActions.getString(
                    R.string.percentage_value,
                    sampledSaveInProgress.getPercentage()
                )
            )
        }
    }

    private fun load() = into(_availableColors) { colorsRepository.getAvailableColors() }

    data class ViewState(
        val colorsList: List<NamedColorListItem>,
        val showSaveProgressBar: Boolean,
        val showSaveButton: Boolean,
        val showCancelButton: Boolean,

        val saveProgressPercentage: Int,
        val saveProgressPercentageMessage: String
    )
}
