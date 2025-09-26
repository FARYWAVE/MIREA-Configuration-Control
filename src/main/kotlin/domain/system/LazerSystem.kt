package system

import items.Folder
import items.Item
import kotlin.system.exitProcess

class LazerSystem {
    private val root = Folder("ROOT", null)
    var currentDirectrory: Item = root
    val version = "0.1.1"
    val logo = "██▓    ▄▄▄      ▒███████▒▓█████  ██▀███  ▓█████▄  ██▓ ███▄ ▄███▓ ▒█████    ██████ \n" +
            "▓██▒   ▒████▄    ▒ ▒ ▒ ▄▀░▓█   ▀ ▓██ ▒ ██▒▒██▀ ██▌▓██▒▓██▒▀█▀ ██▒▒██▒  ██▒▒██    ▒ \n" +
            "▒██░   ▒██  ▀█▄  ░ ▒ ▄▀▒░ ▒███   ▓██ ░▄█ ▒░██   █▌▒██▒▓██    ▓██░▒██░  ██▒░ ▓██▄   \n" +
            "▒██░   ░██▄▄▄▄██   ▄▀▒   ░▒▓█  ▄ ▒██▀▀█▄  ░▓█▄   ▌░██░▒██    ▒██ ▒██   ██░  ▒   ██▒\n" +
            "░██████▒▓█   ▓██▒▒███████▒░▒████▒░██▓ ▒██▒░▒████▓ ░██░▒██▒   ░██▒░ ████▓▒░▒██████▒▒\n" +
            "░ ▒░▓  ░▒▒   ▓▒█░░▒▒ ▓░▒░▒░░ ▒░ ░░ ▒▓ ░▒▓░ ▒▒▓  ▒ ░▓  ░ ▒░   ░  ░░ ▒░▒░▒░ ▒ ▒▓▒ ▒ ░\n" +
            "░ ░ ▒  ░ ▒   ▒▒ ░░░▒ ▒ ░ ▒ ░ ░  ░  ░▒ ░ ▒░ ░ ▒  ▒  ▒ ░░  ░      ░  ░ ▒ ▒░ ░ ░▒  ░ ░\n" +
            "  ░ ░    ░   ▒   ░ ░ ░ ░ ░   ░     ░░   ░  ░ ░  ░  ▒ ░░      ░   ░ ░ ░ ▒  ░  ░  ░  \n" +
            "    ░  ░     ░  ░  ░ ░       ░  ░   ░        ░     ░         ░       ░ ░        ░  \n" +
            "                 ░                         ░\n" +
            "   __          _______   _____  ___      _____ _   ______\n" +
            "  / /  __ __  / __/ _ | / _ \\ \\/ / | /| / / _ | | / / __/\n" +
            " / _ \\/ // / / _// __ |/ , _/\\  /| |/ |/ / __ | |/ / _/  \n" +
            "/_.__/\\_, / /_/ /_/ |_/_/|_| /_/ |__/|__/_/ |_|___/___/  \n" +
            "     /___/"

    fun stepBack() {
        if (currentDirectrory.parent != null) currentDirectrory = currentDirectrory.parent!!
        else throw LazerSystemException(
            LazerSystemException.Reason.ROOT_DIRECTORY_CALL,
            "YOU'RE CURRENTLY IN ROOT"
        )
    }

    fun jumpToRoot(): Boolean {
        if (currentDirectrory == root) {
            return true
        }
        currentDirectrory = root
        return false
    }

    fun setDirectory(path: String) {
        val error = LazerSystemException(
            LazerSystemException.Reason.NO_SUCH_DIRECTORY,
            "CANNOT FIND \"$path\""
        )
        var currentItem: Item = if (path.startsWith("ROOT")) root
        else if (currentDirectrory is Folder) {
            if (path.split('/').size < 2) throw error
            (currentDirectrory as Folder).findChild(path.split('/')[1]) ?: throw error
        } else throw error


        var cutPath = path.substringAfter('/')
        while (cutPath.contains('/')) {
            cutPath = cutPath.substringAfter('/')
            if (currentItem !is Folder) throw error
            currentItem = currentItem
                .findChild(cutPath.substringBefore('/')) ?: throw error
        }

        if (currentItem.name == cutPath) currentDirectrory = currentItem;
        else throw error
    }

    fun addFolder(name: String) {
        if (currentDirectrory !is Folder) throw LazerSystemException(
            LazerSystemException.Reason.INCOMPATIBLE_COMMAND,
            "CURRENT DIRECTORY IS NOT A FOLDER"
        )
        if (!(currentDirectrory as Folder).addFolder(name)) {
            throw LazerSystemException(
                LazerSystemException.Reason.NAME_IS_TAKEN,
                "CURRENT DIRECTORY ALREADY HAS ITEM NAMED \"${name}\""
            )
        }
    }

    fun addFIle(name: String, content: Any = "") {
        if (currentDirectrory !is Folder) throw LazerSystemException(
            LazerSystemException.Reason.INCOMPATIBLE_COMMAND,
            "CURRENT DIRECTORY IS NOT A FOLDER"
        )
        if (!(currentDirectrory as Folder).addFile(name, content)) {
            throw LazerSystemException(
                LazerSystemException.Reason.NAME_IS_TAKEN,
                "CURRENT DIRECTORY ALREADY HAS ITEM NAMED \"${name}\""
            )
        }
    }

    fun deleteFolder(name: String) {
        if (currentDirectrory !is Folder) throw LazerSystemException(
            LazerSystemException.Reason.INCOMPATIBLE_COMMAND,
            "CURRENT DIRECTORY IS NOT A FOLDER"
        )
        if (!(currentDirectrory as Folder).deleteFolder(name)) {
            throw LazerSystemException(
                LazerSystemException.Reason.NO_SUCH_ITEM,
                "CANNOT DELETE \"$name\""
            )
        }
    }

    fun deleteFile(name: String) {
        if (currentDirectrory !is Folder) throw LazerSystemException(
            LazerSystemException.Reason.INCOMPATIBLE_COMMAND,
            "CURRENT DIRECTORY IS NOT A FOLDER"
        )
        if (!(currentDirectrory as Folder).deleteFile(name)) {
            throw LazerSystemException(
                LazerSystemException.Reason.NO_SUCH_ITEM,
                "CANNOT DELETE \"$name\""
            )
        }
    }

    fun viewPath(): List<Item> {
        return currentDirectrory.buildPath()
    }

    fun viewContent(): String {
        return currentDirectrory.buildContent()
    }

    fun viewItemContent(name: String): String {
        if (currentDirectrory !is Folder) throw LazerSystemException(
            LazerSystemException.Reason.INCOMPATIBLE_COMMAND,
            "CURRENT DIRECTORY IS NOT A FOLDER"
        )
        val item = (currentDirectrory as Folder).findChild(name)
        return item?.buildContent() ?: throw LazerSystemException(
            LazerSystemException.Reason.NO_SUCH_ITEM,
            "CANNOT FIND \"$name\""
        )
    }

    fun viewTree(): String {
        return root.buildBranch("")
    }

    fun viewBranch(): String {
        if (currentDirectrory !is Folder) throw LazerSystemException(
            LazerSystemException.Reason.INCOMPATIBLE_COMMAND,
            "CURRENT DIRECTORY IS NOT A FOLDER"
        )
        return (currentDirectrory as Folder).buildBranch("")
    }

    fun viewEnvironmentVariable(input: String): String {
        val regex = "\\$[A-Za-z_][A-Za-z0-9_]*".toRegex()
        return regex.replace(input) { match ->
            val varName = match.value.substring(1)
            java.lang.System.getenv(varName) ?: match.value
        }
    }

    fun viewEnvironmentVariables(): String {
        return java.lang.System.getenv().map { it.key }.joinToString("\n")
    }

    fun exit() {
        exitProcess(0)
    }

}