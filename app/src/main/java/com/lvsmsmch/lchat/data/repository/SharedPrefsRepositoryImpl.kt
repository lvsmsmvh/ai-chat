package com.lvsmsmch.lchat.data.repository

import android.content.Context
import com.lvsmsmch.lchat.domain.repositories.SharedPrefsRepository

class SharedPrefsRepositoryImpl(
    private val context: Context
) : SharedPrefsRepository {

    companion object {
        private const val KEY_PREFERENCES = "my_private_prefs"
    }

    private val prefs = context.getSharedPreferences(
        KEY_PREFERENCES, Context.MODE_PRIVATE
    )
}