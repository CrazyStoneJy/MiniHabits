package me.crazystone.minihabits.ui.compose

import androidx.compose.foundation.background
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import me.crazystone.minihabits.ui.theme.ColorTheme
import me.crazystone.minihabits.ui.theme.Dimensions

@Composable
fun MHButton(title: String, onClick: () -> Unit) {
    Button(onClick = onClick,
        colors = ButtonDefaults.buttonColors(
        containerColor = ColorTheme.primaryColor400, // 按钮填充颜色
        contentColor = ColorTheme.blackColor  // 文字颜色
    )) {
        Text(title, fontSize = Dimensions.TEXT_MID_SIZE.sp)
    }
}