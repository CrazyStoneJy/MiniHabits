package me.crazystone.minihabits.utils

object Logs {
    const val tag = "wtf"
    fun <T> d(t: T) {
        println("$tag $t")
    }

}