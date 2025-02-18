package me.crazystone.minihabits.net

sealed class NetResultState<T> {

    data class Success<T>(val data: T) : NetResultState<T>()
    data class Error<T>(val message: String) : NetResultState<T>()
    class Loading<T> : NetResultState<T>()

}