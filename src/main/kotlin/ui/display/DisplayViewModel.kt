package ui.display

import androidx.compose.runtime.mutableStateOf

object DisplayViewModel {
    val scrollFlag = mutableStateOf(true)
    val typingIndex = mutableStateOf(0)
}