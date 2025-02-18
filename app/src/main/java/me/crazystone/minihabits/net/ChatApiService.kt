package me.crazystone.minihabits.net

import retrofit2.http.Body
import retrofit2.http.POST

interface ChatApiService {
    @POST("chat/completions")
    suspend fun getChatCompletion(@Body chatBody: ChatBody): ChatCompletionResponse
}
