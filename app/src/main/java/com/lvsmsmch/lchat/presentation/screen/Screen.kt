package com.lvsmsmch.lchat.presentation.screen

import androidx.navigation.NavBackStackEntry

sealed class Screen(val route: String) {
    data object Characters : Screen(route = "characters")
    data object Chat : Screen(route = "chat/{characterId}") {
        fun createRoute(characterId: Int) = "chat/$characterId"
        fun getArgumentId(entry: NavBackStackEntry): Int {
            return entry.arguments?.getString("characterId")?.toInt() ?: 0
        }
    }
}
