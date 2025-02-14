package me.crazystone.minihabits.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import me.crazystone.minihabits.ui.theme.ColorTheme

@Composable
fun RepeatSwitch(isRepeat: Boolean, onCheckedChange: ((Boolean) -> Unit)?) {

    Row(modifier = Modifier.padding(2.dp),
        horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        Text(text = "重复")
        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
        // 可以根据不同的type来设置相应的重复时间， 目前只设置一个开关
        Switch(modifier = Modifier.scale(0.8F),
            checked = isRepeat, onCheckedChange = onCheckedChange, colors = SwitchDefaults.colors(checkedTrackColor = ColorTheme.primaryColor, checkedBorderColor = ColorTheme.primaryColor400) )
    }

}