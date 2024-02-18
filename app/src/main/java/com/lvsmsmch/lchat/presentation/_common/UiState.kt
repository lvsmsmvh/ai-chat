package com.lvsmsmch.lchat.presentation._common

sealed class UiState {
    data object Loading : UiState()
    data object Success : UiState()
    data class Failed(val message: String = "") : UiState()
}