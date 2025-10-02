import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import ui.MainViewModel
import ui.theme.Colors
import ui.theme.TextStyles
import utils.highlightCommand

@Composable
fun Input(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(Colors.Tertiary)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(":", color = Colors.Text, style = TextStyles.Normal)
        LazyRow(Modifier.wrapContentSize()) {
            item { Text("ROOT", color = Colors.Text, style = TextStyles.Normal) }
        }
        Text(">", color = Colors.Text, style = TextStyles.Normal)
        val scrollState = rememberScrollState()
        BasicTextField(
            value = MainViewModel.inputText.value,
            onValueChange = { MainViewModel.inputText.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Colors.Transparent)
                .horizontalScroll(scrollState)
                .onPreviewKeyEvent { event ->
                    if (event.type == KeyEventType.KeyDown) {
                        when (event.key) {
                            Key.DirectionUp -> {
                                MainViewModel.onKeyUpPressed()
                                true
                            }

                            Key.DirectionDown -> {
                                MainViewModel.onKeyDownPressed()
                                true
                            }
                            else -> false
                        }
                    }
                    false
                },
            singleLine = true,
            textStyle = TextStyles.Normal.copy(color = Colors.Primary),
            cursorBrush = SolidColor(Colors.Primary),
            decorationBox = { innerTextField ->
                innerTextField()
                Text(
                    text = highlightCommand(MainViewModel.inputText.value),
                    style = TextStyles.Normal
                )
            }
        )
    }
}
