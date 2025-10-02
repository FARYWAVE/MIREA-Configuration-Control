package ui.console

import Input
import ui.MainViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import ui.display.Display
import ui.theme.Colors

@Composable
fun Console(modifier: Modifier = Modifier) {
    SelectionContainer {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Colors.Background)
                .onKeyEvent { keyEvent ->
                    return@onKeyEvent MainViewModel.onKeyPressed(keyEvent)
                }
        ) {
            Display(modifier = Modifier.weight(1f))
            Input(modifier = Modifier.fillMaxWidth())
        }
    }
}