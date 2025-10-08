package domain.system

import kotlin.system.exitProcess

class LazerSystem {
    val version = "0.1.7"
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
        if (input == "\$HOME") return System.getProperty("user.dir")
        val regex = "\\$[A-Za-z_][A-Za-z0-9_]*".toRegex()
        return regex.replace(input) { match ->
            val varName = match.value.substring(1)
            System.getenv(varName) ?: throw LazerSystemException(
                LazerSystemException.Reason.NO_SUCH_VARIABLE,
                "CANNOT FIND ENVIRONMENT VARIABLE NAMED $input"
                )
        }
    }

    fun exit() {
        exitProcess(0)
    }

}