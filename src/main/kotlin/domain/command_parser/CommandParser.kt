package domain.command_parser

import domain.system.LazerSystemException
import domain.system.LazerSystem

class CommandParser(private val lazerSystem: LazerSystem) {
    init {
        lazerSystem.parser = this
    }
    private data class Command(
        val name: String,
        val invoke: (arguments: List<String>) -> List<CommandParserResponse>
    )

    private val commands = listOf(
        Command("CD") { arguments -> cd(arguments) },
        Command("LS") { arguments -> ls(arguments) },
        Command("CONF-DUMP") { _ -> confDump() },
        Command("RUN") { arguments -> runScript(arguments) },
        Command("EXIT") { _ -> exit() },
        Command("COMMANDS") { _ -> commands() },
        Command("CLEAR") { _ -> clear() }
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
        val result = mutableListOf<CommandParserResponse>()
        try {
            val splitInput = input.split(' ')
            val command = splitInput[0]
            val suggestions = searchCommands(command)
            if (suggestions.isEmpty()) return listOf(CommandParserResponse(ResponseType.ERROR, "COMMAND NOT FOUND"))
            val arguments = splitInput.drop(1).toList().map { lazerSystem.viewEnvironmentVariable(it) }

            if (suggestions.size == 1) {
                result.addAll(commands.find{ it.name === suggestions[0] }!!.invoke(arguments))
            } else {
                result.add(
                    CommandParserResponse(
                        ResponseType.NOTIFICATION,
                        "COMMAND IS UNDEFINED, SUGGESTIONS:\n" +
                                suggestions.joinToString("\n")
                    )
                )
            }
        } catch (e: LazerSystemException) {
            result.add(
                CommandParserResponse(
                    ResponseType.ERROR,
                    "ERROR: ${e.reason} - ${e.message}"
                )
            )
        }
        return result
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

    private fun confDump(): List<CommandParserResponse> {
        return listOf(
            CommandParserResponse(
                ResponseType.TEXT,
                lazerSystem.viewConfiguration()
            )
        )
    }

    private fun runScript(arguments: List<String>): List<CommandParserResponse> {
        if (arguments.isEmpty()) return listOf(CommandParserResponse(ResponseType.ERROR, "NOT ENOUGH ARGUMENTS, PATH REQUIRED"))
        lazerSystem.runScript(arguments[0])
        if (arguments.size == 1) return listOf(CommandParserResponse(ResponseType.EMPTY))
        else return listOf(CommandParserResponse(
            ResponseType.NOTIFICATION,
            "REDUNDANT ARGUMENTS IGNORED: ${arguments.subList(1, arguments.size).joinToString(" ")}"
        ))

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
