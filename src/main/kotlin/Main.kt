import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import commandAdapter.CommandParser
import org.jetbrains.skiko.GraphicsApi
import system.LazerSystem
import ui.console.Console

@Composable
@Preview
fun App() {
    Console(modifier = Modifier.fillMaxSize())
}

fun main() = application {
    System.setProperty("skiko.renderApi", "SOFTWARE_COMPAT")
    val system = LazerSystem()
    val commandParser = CommandParser(system)
    MainViewModel.commandParser = commandParser
    MainViewModel.init(system.currentDirectrory)
    //GraphicsApi.

    Window(
        onCloseRequest = ::exitApplication,
        title = "LazerDimOS700",
        icon = painterResource("images/sybau.jpg"),
    ) {
        this.window.addWindowFocusListener(object : java.awt.event.WindowFocusListener {
            override fun windowGainedFocus(e: java.awt.event.WindowEvent?) {
                MainViewModel.isFocused.value = true
            }

            override fun windowLostFocus(e: java.awt.event.WindowEvent?) {
                MainViewModel.isFocused.value = false
            }
        })
        App()
    }
}
