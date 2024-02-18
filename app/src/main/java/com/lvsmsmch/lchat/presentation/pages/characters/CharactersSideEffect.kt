package com.lvsmsmch.lchat.presentation.pages.characters

sealed class CharactersSideEffect {
    data class ShowChat(val characterId: Int) : CharactersSideEffect()
    data object Back : CharactersSideEffect()
}