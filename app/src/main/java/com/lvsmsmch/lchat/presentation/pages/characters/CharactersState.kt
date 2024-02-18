package com.lvsmsmch.lchat.presentation.pages.characters

import com.lvsmsmch.lchat.domain.entities.Character
import com.lvsmsmch.lchat.presentation._common.UiState

data class CharactersState(
    val status: UiState = UiState.Loading,
    val characters: List<Character>? = null,
)