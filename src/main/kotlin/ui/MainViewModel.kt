package ui

import androidx.compose.runtime.*
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import domain.command_parser.CommandParser
import domain.command_parser.ResponseType
import domain.command_parser.CommandParserResponse
import ui.display.DisplayViewModel

object MainViewModel {
    var commandParser: CommandParser? = null
    val history = mutableStateOf(listOf<CommandParserResponse>())
    private val commandHistory = mutableListOf<String>()
    private var currentHistoryIndex = -1
    val inputText = mutableStateOf("")
    val isFocused = mutableStateOf(false)
    private var runnedStartup = false

    fun onKeyPressed(keyEvent: KeyEvent): Boolean {
        when (keyEvent.key) {
            Key.Enter -> onEnterPressed()
        }
        return false
    }

    fun init() {
        if (commandParser != null) {
            if (!runnedStartup) {
                history.value = commandParser!!.getOnBoot()
                runnedStartup = true
                commandParser!!.runStartup()
            }
        }
    }

    fun onKeyUpPressed() {
        if (currentHistoryIndex < commandHistory.lastIndex)
            currentHistoryIndex++
        inputText.value = commandHistory[currentHistoryIndex]
    }

    fun onKeyDownPressed() {
        if (currentHistoryIndex > 0) {
            currentHistoryIndex--
            inputText.value = commandHistory[currentHistoryIndex]
        } else {
            currentHistoryIndex = -1
            inputText.value = ""
        }
    }

    private fun onEnterPressed() {
        if (inputText.value.isNotEmpty() && commandParser != null) {
            processCommand(inputText.value)
        } else {
            skipAnimations();
        }
    }

    fun processCommand(command: String) {
        println(command)
        try {
            commandHistory.add(0, command)
            currentHistoryIndex = -1
            val path = ":/${commandParser!!.getPath()}>"
            history.value = history.value + CommandParserResponse(ResponseType.COMMAND, path + command)
            val response = commandParser!!.call(command)
            if (response[0].type == ResponseType.CLEAR) {
                history.value = emptyList()
                DisplayViewModel.typingIndex.value = 0
            }
            else history.value = history.value + response
        } catch (e: Exception) {
            history.value += CommandParserResponse(ResponseType.CRITICAL_ERROR, e.message)
        } finally {
            inputText.value = ""
        }
    }

    private fun skipAnimations() {
        DisplayViewModel.typingIndex.value = history.value.size
        DisplayViewModel.scrollFlag.value = !DisplayViewModel.scrollFlag.value
    }
}