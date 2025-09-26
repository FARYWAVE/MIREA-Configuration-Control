import androidx.compose.runtime.*
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import commandAdapter.CommandParser
import commandAdapter.ResponseType
import domain.command_parser.CommandParserResponse
import items.Item

object MainViewModel {
    var commandParser: CommandParser? = null

    val history = mutableStateOf(listOf<CommandParserResponse>())
    val inputText = mutableStateOf("")
    val isFocused = mutableStateOf(false)
    val currentDirectory = mutableStateOf(listOf<Item>())

    fun onKeyPressed(keyEvent: KeyEvent): Boolean {
        when (keyEvent.key) {
            Key.Enter -> onEnterPressed()
        }
        return false
    }

    fun init(directory: Item) {
        if (commandParser != null) history.value = commandParser!!.getOnBoot()
        currentDirectory.value = listOf(directory)
    }


    private fun onEnterPressed() {
        if (inputText.value.isNotEmpty() && commandParser != null) {
            try {
                val path = ":/${commandParser!!.getPath().joinToString("/") {it.name}}>"
                history.value = history.value + CommandParserResponse(ResponseType.COMMAND, path + inputText.value)
                val response = commandParser!!.call(inputText.value)
                if (response[0].type == ResponseType.CLEAR) history.value = emptyList()
                else history.value = history.value + response
            } catch (e: Exception) {
                history.value += CommandParserResponse(ResponseType.CRITICAL_ERROR, e.message)
            } finally {
                inputText.value = ""
                currentDirectory.value = commandParser!!.getPath()
            }
        }
    }

    fun onDirectoryChanged(directory: Item) {
        if (commandParser != null) commandParser!!.setDirectory(directory)
        currentDirectory.value = commandParser!!.getPath()
    }
}