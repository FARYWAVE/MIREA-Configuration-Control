package domain.command_parser

import commandAdapter.ResponseType

data class CommandParserResponse(val type: ResponseType, val attachment: Any? = null)