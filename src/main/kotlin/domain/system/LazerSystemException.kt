package system

class LazerSystemException(val reason: Reason, override val message: String) : Exception(message) {
    enum class Reason {
        NO_SUCH_ITEM,
        ROOT_DIRECTORY_CALL,
        NAME_IS_TAKEN,
        INCOMPATIBLE_COMMAND,
        NO_SUCH_DIRECTORY,
    }

    override fun toString(): String {
        return "ERROR: $reason: $message"
    }
}