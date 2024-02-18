package com.lvsmsmch.lchat.utils

import android.content.Context
import android.content.res.Resources.getSystem
import android.widget.Toast
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.util.Scanner


fun Int.dpToPx(): Int = (this * getSystem().displayMetrics.density).toInt()
fun Int.pxToDp(): Int = (this / getSystem().displayMetrics.density).toInt()
fun Float.dpToPx(): Int = (this * getSystem().displayMetrics.density).toInt()
fun Float.pxToDp(): Int = (this / getSystem().displayMetrics.density).toInt()



fun showToast(context: Context, str: String) {
    Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
}

fun showToast(context: Context, strRes: Int) {
    showToast(context, context.getString(strRes))
}

fun String.addEmptyLines(lines: Int) = this + "\n".repeat(lines)

fun Context.getJsonFromResource(resourceId: Int): String {
    return Scanner(resources.openRawResource(resourceId)).useDelimiter("\\A").next()
}

inline fun <reified T> jsonToListOfObjects(jsonString: String): List<T> {
    val tagGroupType = object : TypeToken<List<T>>() {}.type
    return Gson().fromJson(jsonString, tagGroupType)
}