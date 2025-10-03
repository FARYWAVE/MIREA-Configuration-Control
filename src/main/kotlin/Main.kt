import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import domain.command_parser.CommandParser
import domain.system.LazerSystem
import ui.MainViewModel
import ui.console.Console

@Composable
@Preview
fun App() {
    Console(modifier = Modifier.fillMaxSize())
}

fun main(args: Array<String>) = application {
    System.setProperty("skiko.renderApi", "SOFTWARE_COMPAT")
    val system = LazerSystem()
    val commandParser = CommandParser(system)
    MainViewModel.commandParser = commandParser
    MainViewModel.init()
    LazerSystem.Configuration.vfsPath = args.getOrNull(0) ?: System.getProperty("user.dir")
    LazerSystem.Configuration.startupScriptPath = args.getOrNull(1) ?: "../startups/startup1.txt"
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
        system.runStartupScript()
    }
}
