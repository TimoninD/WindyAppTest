package com.windy.windyapptest.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.windy.windyapptest.presentation.view.MainState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    fun onTextChanged(text: String) {
        updateState(text = text)
    }

    fun onStartClicked() {

    }

    private fun updateState(
        text: String = state.value.textValue,
        sums: List<Int> = state.value.sums
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.emit(MainState(textValue = text, sums = sums))
        }
    }
}