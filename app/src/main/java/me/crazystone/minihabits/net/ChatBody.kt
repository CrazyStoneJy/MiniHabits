package me.crazystone.minihabits.net

import com.google.gson.annotations.SerializedName

data class ChatBody(
    @SerializedName("messages") val messages: List<Message>,
    @SerializedName("model") val model: String,
    @SerializedName("frequency_penalty") val frequencyPenalty: Int,
    @SerializedName("max_tokens") val maxTokens: Int,
    @SerializedName("presence_penalty") val presencePenalty: Int,
    @SerializedName("response_format") val responseFormat: ResponseFormat,
    @SerializedName("stop") val stop: Any?,
    @SerializedName("stream") val stream: Boolean,
    @SerializedName("stream_options") val streamOptions: Any?,
    @SerializedName("temperature") val temperature: Int,
    @SerializedName("top_p") val topP: Int,
    @SerializedName("tools") val tools: Any?,
    @SerializedName("tool_choice") val toolChoice: String,
    @SerializedName("logprobs") val logprobs: Boolean,
    @SerializedName("top_logprobs") val topLogprobs: Any?
)

data class Message(
    @SerializedName("content") val content: String,
    @SerializedName("role") val role: String
)

data class ResponseFormat(
    @SerializedName("type") val type: String
)
