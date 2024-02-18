package com.lvsmsmch.lchat.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lvsmsmch.lchat.presentation.pages.characters.CharactersPage
import com.lvsmsmch.lchat.presentation.pages.characters.CharactersSideEffect
import com.lvsmsmch.lchat.presentation.pages.characters.CharactersViewModel
import com.lvsmsmch.lchat.presentation.pages.chat.ChatPage
import com.lvsmsmch.lchat.presentation.pages.chat.ChatSideEffect
import com.lvsmsmch.lchat.presentation.pages.chat.ChatViewModel
import com.lvsmsmch.lchat.presentation.screen.Screen
import com.lvsmsmch.lchat.presentation.theme.ChatTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ChatTheme {
                // A surface container using the 'background' color from the theme
                window.statusBarColor = MaterialTheme.colorScheme.primaryContainer.toArgb()
                Box(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = Screen.Characters.route) {
                        addCharacters(navController = navController)
                        addChat(navController = navController)
                    }
                }
            }
        }
    }
}

private fun NavGraphBuilder.addCharacters(navController: NavController) {
    composable(route = Screen.Characters.route) {
        val viewModel = koinViewModel<CharactersViewModel>()
        val state by viewModel.collectAsState()
        viewModel.collectSideEffect {
            when (it) {
                is CharactersSideEffect.ShowChat -> {
                    navController.navigate(route = Screen.Chat.createRoute(it.characterId))
                }

                is CharactersSideEffect.Back -> {
                    navController.popBackStack()
                }
            }
        }
        CharactersPage(
            state = state,
            onCharacterClick = { characterId -> viewModel.showChat(characterId) },
            onBack = { navController.popBackStack() }
        )
    }
}


private fun NavGraphBuilder.addChat(navController: NavController) {
    composable(route = Screen.Chat.route) {
        val viewModel = koinViewModel<ChatViewModel>(
            parameters = {
                parametersOf(Screen.Chat.getArgumentId(it))
            }
        )

        val state by viewModel.collectAsState()
        viewModel.collectSideEffect {
            when (it) {
                is ChatSideEffect.Back -> {
                    navController.popBackStack()
                }
            }
        }

        ChatPage(
            state = state,
            onSendMessage = { text -> viewModel.sendMessageFromUser(text) },
            onSelected = { chatItem -> viewModel.selectMessage(chatItem) },
            onUnselected = { chatItem -> viewModel.unselectMessage(chatItem) },
            onSelectAll = { viewModel.selectAllMessages() },
            onUnselectAll = { viewModel.unselectAllMessages() },
            onDelete = { viewModel.deleteMessages() },
            onReload = { chatItem -> viewModel.reloadFailedMessage(chatItem) },
        )
    }
}
