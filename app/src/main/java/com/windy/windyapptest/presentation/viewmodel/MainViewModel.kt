package com.windy.windyapptest.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.windy.windyapptest.domain.MainInteractor
import com.windy.windyapptest.presentation.view.MainState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val interactor = MainInteractor()

    private var sumFlow: MutableSharedFlow<Int>? = null
    private var sumJob: Job? = null

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    fun onTextChanged(text: String) {
        updateState(text = text)
    }

    fun onStartClicked() {
        viewModelScope.launch {
            val count = state.value.textValue.toIntOrNull()
            count?.let {
                updateState(sums = listOf())

                val flows = interactor.getFlowsByCount(it)

                sumFlow = MutableSharedFlow()
                collectFinalSumFlow()
                collectSumFlows(flows)
            }
        }
    }

    private fun collectFinalSumFlow() {
        viewModelScope.launch {
            sumFlow?.collect {
                val oldSums = state.value.sums
                val newSum = oldSums.sum() + it
                updateState(sums = oldSums + newSum)
            }
        }
    }

    private fun collectSumFlows(flows: List<Flow<Int>>) {
        sumJob?.cancel()
        sumJob = viewModelScope.launch(Dispatchers.IO) {
            flows.forEach {
                it.collect {
                    sumFlow?.emit(it)
                }
            }
        }
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