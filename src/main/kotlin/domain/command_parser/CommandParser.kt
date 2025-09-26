package commandAdapter

import domain.command_parser.CommandParserResponse
import items.Item
import system.LazerSystemException
import system.LazerSystem

class CommandParser(private val lazerSystem: LazerSystem) {
    private data class Command(
        val name: String,
        val argCount: Int,
        val invoke: (arguments: List<String>) -> List<CommandParserResponse>
    )

    private val commands = listOf(
        Command("CHANGE_DIRECTORY", 1) { arguments -> changeDirectory(arguments[0]) },
        Command("STEP_BACK", 0) { _ -> stepBack() },
        Command("JUMP_TO_ROOT", 0) { _ -> jumpToRoot() },
        Command("ADD_FOLDER", 1) { arguments -> addFolder(arguments[0]) },
        Command("ADD_FILE", 2) { arguments -> addFile(arguments[0], arguments[1]) },
        Command("DELETE_FOLDER", 1) { arguments -> deleteFolder(arguments[0]) },
        Command("DELETE_FILE", 1) { arguments -> deleteFile(arguments[0]) },
        Command("VIEW_CONTENT", -1) { arguments -> viewContent(arguments) },
        Command("VIEW_TREE", 0) { _ -> viewTree() },
        Command("VIEW_BRANCH", 0) { _ -> viewBranch() },
        Command("READ_ENVIRONMENT_VARIABLE", 1) { arguments -> readEnvironmentVariable(arguments[0]) },
        Command("VIEW_ENVIRONMENT_VARIABLES", 0) { _ -> viewEnvironmentVariables()},
        Command("EXIT", 0) { _ -> exit() },
        Command("COMMANDS", 0) { _ -> commands() },
        Command("CLEAR", 0) { _ -> clear() }

    )

    private fun searchCommands(command: String): List<String> {
        val suggestions = mutableListOf<String>()
        val commandNames = commands.map { it.name }
        commandNames.forEach { it ->
            if (command.uppercase().replace("_", "") in it.replace("_", "") ||
                it.split('_').joinToString(""){it[0].toString()} == command.uppercase()
            ) suggestions.add(it)
        }
        return suggestions.toList()
    }

    fun call(input: String): List<CommandParserResponse> {
        val splitInput = input.split(' ')
        val command = splitInput[0]
        val arguments = splitInput.drop(1).toList()

        val suggestions = searchCommands(command)
        if (suggestions.isEmpty()) return listOf(CommandParserResponse(ResponseType.ERROR, "COMMAND NOT FOUND"))
        else if (suggestions.size == 1) {
            val foundCommand = commands.find { it.name == suggestions[0] }!!
            try {
                if (arguments.size == foundCommand.argCount || foundCommand.argCount == -1)
                    return foundCommand.invoke(arguments)
                else if (arguments.size < foundCommand.argCount)
                    return listOf(
                        CommandParserResponse(
                            ResponseType.ERROR,
                            "NOT ENOUGH ARGUMENTS GIVEN, ${foundCommand.argCount} REQUIRED"
                        )
                    )
                else {
                    val result = foundCommand.invoke(arguments).toMutableList()
                    result.add(
                        CommandParserResponse(
                            ResponseType.NOTIFICATION,
                            "REDUNDANT ARGUMENTS IGNORED, ONLY ${foundCommand.argCount} REQUIRED"
                        )
                    )
                    return result.toList()
                }
            } catch (e: LazerSystemException) {
                return listOf(
                    CommandParserResponse(
                        ResponseType.ERROR,
                        "ERROR: ${e.reason} - ${e.message}"
                    )
                )
            }
        } else {
            return listOf(
                CommandParserResponse(
                    ResponseType.NOTIFICATION,
                    "COMMAND IS UNDEFINED, SUGGESTIONS:\n" +
                            suggestions.joinToString("\n")
                )
            )
        }
    }

    fun getPath() = lazerSystem.viewPath()

    fun getOnBoot() = listOf(
        CommandParserResponse(ResponseType.TEXT,
            "LAUNCHING LAZERDIMOS700...\n" +
                    "LAZER SYSTEM: ONLINE\n" +
                    "COMMAND PARSER: ONLINE\n" +
                    "MAIN VIEW MODEL: ONLINE\n" +
                    "UI: ONLINE\n" +
                    "ШРИФТ ИМПАКТ: ПРИСУТСТВУЕТ\n\n" +
                    "WELCOME TO\n"
        ),
        CommandParserResponse(
            ResponseType.LOGO,
            "${lazerSystem.logo}\n\nVERSION: ${lazerSystem.version}"
        )
    )

    fun setDirectory(directory: Item) {
        lazerSystem.currentDirectrory = directory
    }

    private fun changeDirectory(path: String): List<CommandParserResponse> {
        lazerSystem.setDirectory(path)
        return listOf(CommandParserResponse(ResponseType.EMPTY))
    }

    private fun stepBack(): List<CommandParserResponse> {
        lazerSystem.stepBack()
        return listOf(CommandParserResponse(ResponseType.EMPTY))
    }

    private fun jumpToRoot(): List<CommandParserResponse> {
        return listOf(
            if (lazerSystem.jumpToRoot())
                CommandParserResponse(ResponseType.NOTIFICATION, "YOU'RE ALREADY IN ROOT")
            else
                CommandParserResponse(ResponseType.EMPTY)
        )
    }

    private fun addFolder(name: String): List<CommandParserResponse> {
        lazerSystem.addFolder(name)
        return listOf(CommandParserResponse(ResponseType.TEXT, "CREATED FOLDER: $name"))
    }

    private fun addFile(name: String, content: Any): List<CommandParserResponse> {
        lazerSystem.addFIle(name, content)
        return listOf(CommandParserResponse(ResponseType.TEXT, "CREATED FILE: $name"))
    }

    private fun deleteFolder(name: String): List<CommandParserResponse> {
        lazerSystem.deleteFolder(name)
        return listOf(CommandParserResponse(ResponseType.TEXT, "DELETE FOLDER: $name"))
    }

    private fun deleteFile(name: String): List<CommandParserResponse> {
        lazerSystem.deleteFile(name)
        return listOf(CommandParserResponse(ResponseType.TEXT, "DELETED FILE: $name"))
    }

    private fun viewContent(arguments: List<String>): List<CommandParserResponse> {
        return listOfNotNull(
            if (arguments.isEmpty()) CommandParserResponse(ResponseType.TEXT, lazerSystem.viewContent())
            else CommandParserResponse(
                ResponseType.TEXT,
                lazerSystem.viewItemContent(arguments[0])
            ),
            if (arguments.size > 1) CommandParserResponse(ResponseType.NOTIFICATION) else null
        )
    }


    private fun viewTree(): List<CommandParserResponse> {
        return listOf(CommandParserResponse(ResponseType.TEXT, lazerSystem.viewTree()))
    }

    private fun viewBranch(): List<CommandParserResponse> {
        return listOf(CommandParserResponse(ResponseType.TEXT, lazerSystem.viewBranch()))
    }

    private fun readEnvironmentVariable(name: String): List<CommandParserResponse> {
        return listOf(CommandParserResponse(ResponseType.TEXT, lazerSystem.viewEnvironmentVariable(name)))
    }

    private fun viewEnvironmentVariables(): List<CommandParserResponse> {
        return listOf(CommandParserResponse(ResponseType.TEXT, lazerSystem.viewEnvironmentVariables()))
    }

    private fun exit(): List<CommandParserResponse> {
        lazerSystem.exit()
        return listOf(CommandParserResponse(ResponseType.EMPTY))
    }

    private fun commands(): List<CommandParserResponse> {
        return listOf(CommandParserResponse(ResponseType.TEXT, commands.joinToString("\n") { it.name }))
    }

    private fun clear(): List<CommandParserResponse> {
        return listOf(CommandParserResponse(ResponseType.CLEAR))
    }

}
