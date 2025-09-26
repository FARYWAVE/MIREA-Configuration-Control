package ui.console

import Input
import MainViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp
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
            Spacer(Modifier.height(30.dp))
            Input(modifier = Modifier.fillMaxWidth())
        }
    }
}