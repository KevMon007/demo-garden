package com.example.smartgardendemo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartgardendemo.data.model.HistorialEntry
import com.example.smartgardendemo.data.repository.HistorialRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

class HistorialViewModel(
    private val historialRepository: HistorialRepository = HistorialRepository()
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    val historial: StateFlow<List<HistorialEntry>> = historialRepository.observarHistorial()
        .onEach { if (_isLoading.value) _isLoading.value = false }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}
