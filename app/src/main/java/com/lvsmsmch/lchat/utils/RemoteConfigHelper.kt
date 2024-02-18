package com.lvsmsmch.lchat.utils

import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.remoteconfig.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.lvsmsmch.lchat.R
import com.lvsmsmch.lchat.domain.entities.Character

object RemoteConfigHelper {

    fun setDefaultsAndFetch() {
        Firebase.remoteConfig.apply {
            setConfigSettingsAsync(remoteConfigSettings {
                minimumFetchIntervalInSeconds = 3600
            })
            setDefaultsAsync(defaults)
            fetchAndActivate()
        }
    }

    fun openAiKey(): String {
        return Firebase.remoteConfig[KEY_OPEN_AI_KEY].asString()
    }

    fun characters(): List<Character> {
        val json = Firebase.remoteConfig[KEY_CHARACTERS].asString()
        val jsonFromMemory = Firebase.app.applicationContext.getJsonFromResource(R.raw.example_characters)

        Log.d("tag_char", "lastFetchStatus = ${Firebase.remoteConfig.info.lastFetchStatus }")
        Log.d("tag_char", "json = ${json.take(50)}")
        Log.d("tag_char", "jsonFromMemory = ${jsonFromMemory.take(50)}")

        return jsonToListOfObjects<Character>(
            Firebase.remoteConfig[KEY_CHARACTERS].asString().ifEmpty {
                Firebase.app.applicationContext.getJsonFromResource(R.raw.example_characters)
            }
        )
    }


    private const val KEY_OPEN_AI_KEY = "open_api_key"
    private const val KEY_CHARACTERS = "characters_json"


    private val defaults = mutableMapOf<String, Any>(
        KEY_OPEN_AI_KEY to "",
        KEY_CHARACTERS to Firebase.app.applicationContext.getJsonFromResource(
            R.raw.example_characters
        ),
    )

}