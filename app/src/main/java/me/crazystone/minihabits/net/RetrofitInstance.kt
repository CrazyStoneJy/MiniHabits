package me.crazystone.minihabits.net

import android.annotation.SuppressLint
import me.crazystone.minihabits.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration

//import com.jakewharton.retrofit2.kotlin.coroutines.CoroutineCallAdapterFactory


class AuthInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token") // 添加 Header
            .addHeader("Content-Type", "application/json") // 其他固定 Header
            .build()
        return chain.proceed(newRequest)
    }
}


object RetrofitInstance {
    private const val BASE_URL = "https://api.deepseek.com/"
    private const val API_KEY = BuildConfig.API_KEY

    // 创建 OkHttpClient 并添加 Interceptor
    @SuppressLint("NewApi")
    private val client: OkHttpClient = OkHttpClient.Builder()
        .callTimeout(Duration.ofSeconds(60))
        .writeTimeout(Duration.ofSeconds(60))
        .readTimeout(Duration.ofSeconds(60))
        .addInterceptor(AuthInterceptor(API_KEY))
        .build()

    private val retrofit: Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()) // 使用 Gson 解析
            .build()

    val apiService: ChatApiService = retrofit.create(ChatApiService::class.java)

}