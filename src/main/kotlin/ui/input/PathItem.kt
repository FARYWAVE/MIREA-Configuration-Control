package ui.input

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.sp
import items.Item
import ui.theme.Colors
import ui.theme.TextStyles

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PathItem(item: Item) {
    var isHovered by remember { mutableStateOf(false) }

    Text(
        text = "/${item.name}",
        color = if (isHovered) Colors.Primary else Colors.Text,
        style = TextStyles.Normal,
        modifier = Modifier
            .pointerMoveFilter(
                onEnter = {
                    isHovered = true
                    false
                },
                onExit = {
                    isHovered = false
                    false
                }
            )
            .clickable { MainViewModel.onDirectoryChanged(item) }
            .wrapContentHeight()
    )
}