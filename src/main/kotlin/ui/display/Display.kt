package ui.display

import ui.MainViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import domain.command_parser.ResponseType
import ui.responses.*
import ui.theme.Colors

@Composable
fun Display(modifier: Modifier = Modifier) {
    val listState = rememberLazyListState()

    LaunchedEffect(MainViewModel.history.value.size, DisplayViewModel.scrollFlag.value) {
        if (MainViewModel.history.value.isNotEmpty()) {
            withFrameNanos {}
            listState.animateScrollToItem(MainViewModel.history.value.size)
        }
    }

    LazyColumn(
        modifier = modifier.background(Colors.Background).padding(10.dp),
        state = listState,
    ) {
        itemsIndexed(MainViewModel.history.value) { index, response ->
            if (DisplayViewModel.typingIndex.value >= index) {
                when (response.type) {
                    ResponseType.TEXT -> Text(response.attachment as String, index)
                    ResponseType.ERROR -> Error(response.attachment as String, index)
                    ResponseType.CRITICAL_ERROR -> CriticalError(response.attachment as String, index)
                    ResponseType.NOTIFICATION -> Notification(response.attachment as String, index)
                    ResponseType.LOGO -> Logo(response.attachment as String, index)
                    ResponseType.COMMAND -> Command(response.attachment as String, index)
                    else -> if (index == DisplayViewModel.typingIndex.value) DisplayViewModel.typingIndex.value++
                }
            } else Unit

        }
        item {
            Spacer(Modifier.height(30.dp))
        }
    }
}