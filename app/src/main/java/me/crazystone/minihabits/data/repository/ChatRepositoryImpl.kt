package me.crazystone.minihabits.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.crazystone.minihabits.net.ChatBody
import me.crazystone.minihabits.net.ChatCompletionResponse
import me.crazystone.minihabits.net.NetResultState
import me.crazystone.minihabits.net.RetrofitInstance.apiService

class ChatRepositoryImpl : ChatRepository {
    override suspend fun getChatCompletion(chatBody: ChatBody): Flow<NetResultState<ChatCompletionResponse>> =
        flow {
            try {
                emit(NetResultState.Loading())
                val response = apiService.getChatCompletion(chatBody)
                emit(NetResultState.Success(response))
            } catch (e: Exception) {
                emit(NetResultState.Error("network error"))
            }
        }
}