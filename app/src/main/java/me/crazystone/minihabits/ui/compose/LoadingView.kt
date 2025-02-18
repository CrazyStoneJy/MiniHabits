package me.crazystone.minihabits.ui.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LoadingView() {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("lottie_loading.json"))
    val progress by animateLottieCompositionAsState(composition)

    LottieAnimation(
        composition = composition,
//        progress = { progress },
        iterations = LottieConstants.IterateForever,
        modifier = Modifier.fillMaxSize(),
    )

}