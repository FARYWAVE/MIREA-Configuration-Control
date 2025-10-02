package ui.responses

import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import kotlinx.coroutines.delay
import ui.display.DisplayViewModel
import ui.theme.Colors
import ui.theme.TextStyles
import utils.highlightHistoryCommand


@Composable
fun AnimatedText(text: String, color: Color, style: TextStyle, index: Int, speed: Long = 3, byChar: Boolean = true) {
    var displayed by rememberSaveable(text) { mutableStateOf("") }

    LaunchedEffect(text, DisplayViewModel.typingIndex.value) {
        if (DisplayViewModel.typingIndex.value > index) {
            displayed = text
            return@LaunchedEffect
        }
        if (DisplayViewModel.typingIndex.value < index) {
            displayed = ""
            return@LaunchedEffect
        }
        displayed = ""
        val sb = StringBuilder()
        if (byChar) {
            for (ch in text) {
                sb.append(ch)
                if (ch == '\n') DisplayViewModel.scrollFlag.value = !DisplayViewModel.scrollFlag.value
                displayed = sb.toString()
                delay(speed)
            }
        } else {
            for (line in text.split('\n')) {
                sb.append(line + '\n')
                displayed = sb.toString()
                DisplayViewModel.scrollFlag.value = !DisplayViewModel.scrollFlag.value
                delay(speed)
            }
        }
        DisplayViewModel.typingIndex.value = DisplayViewModel.typingIndex.value + 1
    }

    Text(
        modifier = Modifier.wrapContentHeight(),
        text = displayed,
        color = color,
        style = style
    )
}

@Composable
fun PlainText(text: String, color: Color, index: Int) {
    AnimatedText(text, color, TextStyles.Normal, index)
}

@Composable
fun Text(text: String, index: Int) {
    PlainText(text, Colors.Text, index)
}

@Composable
fun Error(text: String, index: Int) {
    PlainText(text, Colors.Error, index)
}

@Composable
fun CriticalError(text: String, index: Int) {
    PlainText(text, Colors.CriticalError, index)
}

@Composable
fun Notification(text: String, index: Int) {
    PlainText(text, Colors.Notification, index)
}

@Composable
fun Logo(text: String, index: Int) {
    AnimatedText(text, Colors.Text, TextStyles.Logo, index,10, false)
}

@Composable
fun Command(text: String, index: Int) {
    if (DisplayViewModel.typingIndex.value == index) DisplayViewModel.typingIndex.value++
    Text(text = highlightHistoryCommand(text), color = Colors.Primary, style = TextStyles.Normal)
}