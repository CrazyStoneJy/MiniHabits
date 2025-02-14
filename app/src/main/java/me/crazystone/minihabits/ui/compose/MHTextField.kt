package me.crazystone.minihabits.ui.compose

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import me.crazystone.minihabits.ui.theme.ColorTheme
import me.crazystone.minihabits.ui.theme.Dimensions

@Composable
fun MHTextField(
    value: String,
    modifier: Modifier,
    placeholder: String,
    onValueChange: (value: String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = {
            Text(placeholder, fontSize = Dimensions.TEXT_MID_SIZE.sp, color = Color.LightGray)
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = ColorTheme.transparent,
            unfocusedContainerColor = ColorTheme.transparent,
            focusedIndicatorColor = ColorTheme.primaryColor,
            unfocusedIndicatorColor = ColorTheme.primaryColor
        )
    )
}