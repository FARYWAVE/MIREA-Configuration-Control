package ui.responses

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import ui.theme.Colors
import ui.theme.TextStyles


@Composable
fun AnimatedText(text: String, color: Color, style: TextStyle, scrollFlag: MutableState<Boolean>, speed: Long = 10, byChar: Boolean = true) {
    var displayed by rememberSaveable(text) { mutableStateOf("") }
    var finished: Boolean by rememberSaveable(text) { mutableStateOf(false) }

    LaunchedEffect(text) {
        if (finished) return@LaunchedEffect
        displayed = ""
        val sb = StringBuilder()
        if (byChar) {
            for (ch in text) {
                if (ch == '\n') scrollFlag.value = !scrollFlag.value
                sb.append(ch)
                displayed = sb.toString()
                delay(speed)
            }
        } else {
            for (line in text.split('\n')) {
                sb.append(line + '\n')
                displayed = sb.toString()
                delay(speed)
                scrollFlag.value = !scrollFlag.value
            }
        }
        finished = true
    }

    Text(
        modifier = Modifier.wrapContentHeight(),
        text = displayed,
        color = color,
        style = style
    )
}

@Composable
fun PlainText(text: String, color: Color, scrollFlag: MutableState<Boolean>) {
    AnimatedText(text, color, TextStyles.Normal, scrollFlag)
}

@Composable
fun Text(text: String, scrollFlag: MutableState<Boolean>) {
    PlainText(text, Colors.Text, scrollFlag)
}

@Composable
fun Error(text: String, scrollFlag: MutableState<Boolean>) {
    PlainText(text, Colors.Error, scrollFlag)
}

@Composable
fun CriticalError(text: String, scrollFlag: MutableState<Boolean>) {
    PlainText(text, Colors.CriticalError, scrollFlag)
}

@Composable
fun Notification(text: String, scrollFlag: MutableState<Boolean>) {
    PlainText(text, Colors.Notification, scrollFlag)
}

@Composable
fun Logo(text: String, scrollFlag: MutableState<Boolean>) {
    AnimatedText(text, Colors.Text, TextStyles.Logo, scrollFlag, 10, false)
}

@Composable
fun Command(text: String) {
    Text(text, color = Colors.Primary, style = TextStyles.Normal)
}