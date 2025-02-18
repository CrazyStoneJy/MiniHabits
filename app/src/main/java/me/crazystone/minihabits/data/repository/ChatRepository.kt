package me.crazystone.minihabits.data.repository

import kotlinx.coroutines.flow.Flow
import me.crazystone.minihabits.net.ChatBody
import me.crazystone.minihabits.net.ChatCompletionResponse
import me.crazystone.minihabits.net.NetResultState

interface ChatRepository {
    suspend fun getChatCompletion(chatBody: ChatBody): Flow<NetResultState<ChatCompletionResponse>>
}