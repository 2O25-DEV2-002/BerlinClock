package com.anonymous.berlinclock.presentation.berlinclock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anonymous.berlinclock.domain.usecase.GetBerlinClockData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class BerlinClockViewModel @Inject constructor(
    private val getBerlinClockDataUseCase: GetBerlinClockData
) : ViewModel() {

    private val _clockState = MutableStateFlow(ClockState())
    val clockState = _clockState.asStateFlow()

    fun onEvent(event: ClockEvent) {
        when (event) {
            is ClockEvent.StartAutomaticClock -> startAutomaticClock()
            is ClockEvent.UpdateClock -> updateBerlinClockTime(time = event.time)
        }
    }

    private fun startAutomaticClock() {
        viewModelScope.launch {
            getBerlinClockDataUseCase()
                .onEach { result ->
                    result.let {
                        _clockState.value = _clockState.value.copy(
                            secondLamp = it.secondLamp,
                            topHourLamps = it.topHourLamps,
                            bottomHourLamps = it.bottomHourLamps,
                            topMinuteLamps = it.topMinuteLamps,
                            bottomMinuteLamps = it.bottomMinuteLamps,
                            normalTime = it.normalTime
                        )
                    }
                }.launchIn(this)
        }
    }

    private fun updateBerlinClockTime(time: String) {
        val result = getBerlinClockDataUseCase(time = time)
        result.let {
            _clockState.value = _clockState.value.copy(
                secondLamp = it.secondLamp,
                topHourLamps = it.topHourLamps,
                bottomHourLamps = it.bottomHourLamps,
                topMinuteLamps = it.topMinuteLamps,
                bottomMinuteLamps = it.bottomMinuteLamps,
                normalTime = it.normalTime
            )
        }

    }
}
