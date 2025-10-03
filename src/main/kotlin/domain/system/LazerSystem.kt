package domain.system

import domain.command_parser.CommandParser
import ui.MainViewModel
import java.io.File
import kotlin.system.exitProcess

class LazerSystem {
    object Configuration {
        var vfsPath: String? = System.getProperty("user.dir")
            set(value) {
                field = value?.let {
                    val file = File(it)
                    file.canonicalPath
                }
            }
        var startupScriptPath: String? = null
        set(value) {
            field = value?.let {
                val file = File(it)
                file.canonicalPath
            }
        }
        var cwd: String = System.getProperty("user.dir")
        var osUser: String = System.getProperty("user.name")

        override fun toString(): String {
            return "vfsPath: $vfsPath\n" +
                    "startupScriptPath: $startupScriptPath\n" +
                    "cwd: $cwd\n" +
                    "osUser: $osUser"
        }
    }

    var parser: CommandParser? = null
    val version = "0.2.7"
    val logo = "██▓    ▄▄▄      ▒███████▒▓█████  ██▀███   ▓█████▄  ██▓ ███▄ ▄███▓  ▒█████    ██████ \n" +
            "▓██▒   ▒████▄    ▒ ▒ ▒ ▄▀░▓█   ▀ ▓██ ▒ ██▒ ▒██▀ ██▌▓██▒▓██▒▀█▀ ██▒ ▒██▒  ██▒▒██    ▒ \n" +
            "▒██░   ▒██  ▀█▄  ░ ▒ ▄▀▒░ ▒███   ▓██ ░▄█ ▒ ░██   █▌▒██▒▓██    ▓██░ ▒██░  ██▒░ ▓██▄   \n" +
            "▒██░   ░██▄▄▄▄██   ▄▀▒   ░▒▓█  ▄ ▒██▀▀█▄   ░▓█▄   ▌░██░▒██    ▒██  ▒██   ██░  ▒   ██▒\n" +
            "░██████▒▓█   ▓██▒▒███████▒░▒████▒░██▓ ▒██▒ ░▒████▓ ░██░▒██▒   ░██▒  ░████▓▒░▒██████▒▒\n" +
            "░ ▒░▓  ░▒▒   ▓▒█░░▒▒ ▓░▒░▒░░ ▒░ ░░ ▒▓ ░▒▓░  ▒▒▓  ▒ ░▓  ░ ▒░   ░  ░░  ▒░▒░▒░ ▒ ▒▓▒ ▒ ░\n" +
            "░ ░ ▒  ░ ▒   ▒▒ ░░░▒ ▒ ░ ▒ ░ ░  ░  ░▒ ░ ▒░  ░ ▒  ▒  ▒ ░░  ░      ░   ░ ▒ ▒░ ░ ░▒  ░ ░\n" +
            "  ░ ░    ░   ▒   ░ ░ ░ ░ ░   ░     ░░   ░   ░ ░  ░  ▒ ░░      ░   ░  ░ ░ ▒  ░  ░  ░  \n" +
            "    ░  ░     ░  ░  ░ ░       ░  ░   ░         ░     ░         ░       ░ ░        ░  \n" +
            "                 ░                          ░\n" +
            "   __          _______   _____  ___      _____ _   ______\n" +
            "  / /  __ __  / __/ _ | / _ \\ \\/ / | /| / / _ | | / / __/\n" +
            " / _ \\/ // / / _// __ |/ , _/\\  /| |/ |/ / __ | |/ / _/  \n" +
            "/_.__/\\_, / /_/ /_/ |_/_/|_| /_/ |__/|__/_/ |_|___/___/  \n" +
            "     /___/"

    fun viewEnvironmentVariable(input: String): String {
        val regex = "\\$[A-Za-z_][A-Za-z0-9_]*".toRegex()
        return regex.replace(input) { match ->
            val varName = match.value.substring(1)
            System.getenv(varName) ?: throw LazerSystemException(
                LazerSystemException.Reason.NO_SUCH_VARIABLE,
                "CANNOT FIND ENVIRONMENT VARIABLE NAMED $input"
            )
        }
    }

    fun runStartupScript() {
        runScript(Configuration.startupScriptPath!!)
    }

    fun runScript(
        scriptPath: String,
    ) {
        val f = File(scriptPath)
        if (!f.exists() || !f.isFile) {
            throw LazerSystemException(
                LazerSystemException.Reason.NO_SUCH_SCRIPT,
                "CANNOT FIND $scriptPath"
            )
        }
        f.forEachLine { rawLine ->
            val line = rawLine.trim()
            if (line.isEmpty() || line.startsWith("#")) return@forEachLine
            MainViewModel.processCommand(line)
        }
    }

    fun viewConfiguration() = Configuration.toString()

    fun exit() {
        exitProcess(0)
    }

}