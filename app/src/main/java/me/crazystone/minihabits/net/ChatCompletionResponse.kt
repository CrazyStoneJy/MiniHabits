package me.crazystone.minihabits.net

import com.google.gson.annotations.SerializedName

data class ChatCompletionResponse(
    val id: String,
    @SerializedName("object") val objectType: String, // "object" 是 Kotlin 关键字，避免冲突
    val created: Long,
    val model: String,
    val choices: List<Choice>,
    val usage: Usage,
    val system_fingerprint: String
)

data class Choice(
    val index: Int,
    val message: Message,
    val logprobs: Any?,
    val finish_reason: String
)

data class Usage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int,
    val prompt_tokens_details: PromptTokensDetails,
    val prompt_cache_hit_tokens: Int,
    val prompt_cache_miss_tokens: Int
)

data class PromptTokensDetails(
    val cached_tokens: Int
)
