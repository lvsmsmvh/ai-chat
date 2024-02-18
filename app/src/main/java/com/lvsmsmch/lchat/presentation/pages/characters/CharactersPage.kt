package com.lvsmsmch.lchat.presentation.pages.characters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.lvsmsmch.lchat.R
import com.lvsmsmch.lchat.domain.entities.Character
import com.lvsmsmch.lchat.presentation._common.UiState
import com.lvsmsmch.lchat.presentation.components.ErrorMessage
import com.lvsmsmch.lchat.presentation.components.LoadingIndicator
import com.lvsmsmch.lchat.presentation.theme.Colors
import com.lvsmsmch.lchat.utils.addEmptyLines

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersPage(
    state: CharactersState,
    onCharacterClick: (characterId: Int) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Colors.Characters.Bg)
            ) {
                when (val status = state.status) {
                    UiState.Loading -> {
                        LoadingIndicator(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(it)
                        )
                    }

                    is UiState.Failed -> {
                        ErrorMessage(
                            message = status.message,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(it)
                        )
                    }

                    UiState.Success -> {
                        val characters = state.characters!!

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(it)
                        ) {
                            CharacterList(
                                characters = characters,
                                onClick = { character -> onCharacterClick(character.id) }
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun CharacterList(
    characters: List<Character>,
    onClick: (Character) -> Unit
) {
    Spacer(modifier = Modifier.height(36.dp))

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 6.dp)
            .padding(vertical = 12.dp)
    ) {
        items(characters.size) { index ->
            val character = characters[index]
            Character(
                character = character,
                onClick = onClick
            )
        }
    }

    Spacer(modifier = Modifier.height(12.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Character(
    character: Character,
    onClick: (Character) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 6.dp)
            .padding(vertical = 6.dp)
    ) {
        Card(
            onClick = {
                onClick(character)
            },
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = Colors.Characters.CardBg,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                AsyncImage(
                    model = character.iconUrl,
                    contentScale = ContentScale.Crop,
                    contentDescription = stringResource(id = R.string.picture_of, character.name),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f / 1f)
                )
                Text(
                    text = character.name,
                    color = Colors.Characters.CardText,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    maxLines = CHARACTER_NAME_LINES,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = 12.dp)
                        .padding(vertical = 12.dp)
                )
            }
        }
    }
}

private const val CHARACTER_NAME_LINES = 2