package me.crazystone.minihabits.domain.usecase

import kotlinx.coroutines.flow.Flow
import me.crazystone.minihabits.data.repository.ChatRepository
import me.crazystone.minihabits.net.ChatBody
import me.crazystone.minihabits.net.ChatCompletionResponse
import me.crazystone.minihabits.net.NetResultState

class RequestChat(private val repository: ChatRepository) {
    suspend operator fun invoke(chatBody: ChatBody): Flow<NetResultState<ChatCompletionResponse>> =
        repository.getChatCompletion(chatBody)
}