package com.lvsmsmch.lchat.presentation.pages.characters

import androidx.lifecycle.ViewModel
import com.lvsmsmch.lchat.domain.repositories.CharactersRepository
import com.lvsmsmch.lchat.presentation._common.UiState
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class CharactersViewModel(
    private val charactersRepository: CharactersRepository,
) : ContainerHost<CharactersState, CharactersSideEffect>, ViewModel() {


    override val container = container<CharactersState, CharactersSideEffect>(
        CharactersState()
    )

    init {
        intent {
            val characters = charactersRepository.getAll()
            if (characters.isSuccess) {
                reduce {
                    state.copy(
                        status = UiState.Success,
                        characters = characters.getOrThrow(),
                    )
                }
            } else {
                val message = characters.exceptionOrNull()?.message.toString()
                reduce {
                    state.copy(
                        status = UiState.Failed(message),
                        characters = null,
                    )
                }
            }
        }
    }

    fun showChat(characterId: Int) {
        intent {
            postSideEffect(CharactersSideEffect.ShowChat(characterId))
        }
    }

}