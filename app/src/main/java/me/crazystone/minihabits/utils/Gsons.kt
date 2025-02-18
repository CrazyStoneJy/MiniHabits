package me.crazystone.minihabits.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object Gsons {

    val instance: Gson by lazy {
        GsonBuilder()
            .setPrettyPrinting() // 可选：格式化 JSON
            .disableHtmlEscaping() // 可选：避免转义 HTML
            .create()
    }

    fun fromJsonToStringArray(rawJson: String): List<String> {
        val cleanJson =
            rawJson.trimIndent().replace(Regex("```[a-zA-Z]*"), "").replace("```", "").trim()
        return instance.fromJson(cleanJson, Array<String>::class.java).toList()
    }

}