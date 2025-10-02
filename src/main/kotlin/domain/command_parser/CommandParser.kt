package domain.command_parser

import domain.system.LazerSystemException
import domain.system.LazerSystem

class CommandParser(private val lazerSystem: LazerSystem) {
    private data class Command(
        val name: String,
        val argCount: Int,
        val invoke: (arguments: List<String>) -> List<CommandParserResponse>
    )

    private val commands = listOf(
        Command("CD", -1) { arguments -> cd(arguments) },
        Command("LS", -1) { arguments -> ls(arguments) },
        Command("EXIT", 0) { _ -> exit() },
        Command("COMMANDS", 0) { _ -> commands() },
        Command("CLEAR", 0) { _ -> clear() },
    )

    private fun searchCommands(command: String): List<String> {
        val suggestions = mutableListOf<String>()
        val commandNames = commands.map { it.name }
        commandNames.forEach { it ->
            if (command.uppercase().replace("_", "") in it.replace("_", "") ||
                it.split('_').joinToString("") { it[0].toString() } == command.uppercase()
            ) suggestions.add(it)
        }
        return suggestions.toList()
    }

    fun call(input: String): List<CommandParserResponse> {
        try {
            val splitInput = input.split(' ')
            val command = splitInput[0]
            val suggestions = searchCommands(command)
            if (suggestions.isEmpty()) return listOf(CommandParserResponse(ResponseType.ERROR, "COMMAND NOT FOUND"))
            val arguments = splitInput.drop(1).toList().map { lazerSystem.viewEnvironmentVariable(it) }

            if (suggestions.size == 1) {
                val foundCommand = commands.find { it.name == suggestions[0] }!!

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

            } else {
                return listOf(
                    CommandParserResponse(
                        ResponseType.NOTIFICATION,
                        "COMMAND IS UNDEFINED, SUGGESTIONS:\n" +
                                suggestions.joinToString("\n")
                    )
                )
            }
        } catch (e: LazerSystemException) {
            return listOf(
                CommandParserResponse(
                    ResponseType.ERROR,
                    "ERROR: ${e.reason} - ${e.message}"
                )
            )
        }
    }

    fun getOnBoot() = listOf(
        CommandParserResponse(
            ResponseType.TEXT,
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

    fun getPath() = "ROOT"

    private fun ls(arguments: List<String>): List<CommandParserResponse> {
        return listOf(
            CommandParserResponse(
                ResponseType.TEXT,
                "LS " + arguments.joinToString(" ")
            )
        )
    }

    private fun cd(arguments: List<String>): List<CommandParserResponse> {
        return listOf(
            CommandParserResponse(
                ResponseType.TEXT,
                "CD " + arguments.joinToString(" ")
            )
        )
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
