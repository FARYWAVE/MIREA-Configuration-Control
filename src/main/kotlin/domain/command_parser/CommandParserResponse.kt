package domain.command_parser

data class CommandParserResponse(val type: ResponseType, val attachment: Any? = null)