package com.lvsmsmch.lchat.utils

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val myJsonObj = Json { ignoreUnknownKeys = true }

inline fun <reified T> T.toJson(): String {
    return myJsonObj.encodeToString(this)
}

inline fun <reified T : Any> String.fromJson(): T {
    return myJsonObj.decodeFromString(this)
}