package domain.command_parser

enum class ResponseType {
    EMPTY,
    NOTIFICATION,
    TEXT,
    ERROR,
    CRITICAL_ERROR,
    CLEAR,
    LOGO,
    COMMAND,
}