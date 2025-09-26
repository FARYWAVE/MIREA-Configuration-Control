package ui.display

import MainViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import commandAdapter.ResponseType
import domain.command_parser.CommandParserResponse
import kotlinx.coroutines.launch
import ui.responses.*
import ui.theme.Colors

@Composable
fun Display(modifier: Modifier = Modifier) {
    val listState = rememberLazyListState()
    val scrollFlag = remember { mutableStateOf(false) }

    LaunchedEffect(MainViewModel.history.value.size, scrollFlag.value) {
        if (MainViewModel.history.value.isNotEmpty()) {
            withFrameNanos {}
            listState.animateScrollToItem(MainViewModel.history.value.lastIndex)
        }
    }

    LazyColumn(
        modifier = modifier.background(Colors.Background).padding(10.dp),
        state = listState,
    ) {
        items(MainViewModel.history.value) { response ->
            when (response.type) {
                ResponseType.TEXT -> Text(response.attachment as String, scrollFlag)
                ResponseType.ERROR -> Error(response.attachment as String, scrollFlag)
                ResponseType.CRITICAL_ERROR -> CriticalError(response.attachment as String, scrollFlag)
                ResponseType.NOTIFICATION -> Notification(response.attachment as String, scrollFlag)
                ResponseType.LOGO -> Logo(response.attachment as String, scrollFlag)
                ResponseType.COMMAND -> Command(response.attachment as String)
                else -> Unit
            }
        }
    }
}