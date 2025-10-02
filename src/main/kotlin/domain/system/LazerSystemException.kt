package domain.system

class LazerSystemException(val reason: Reason, override val message: String) : Exception(message) {
    enum class Reason {
        NO_SUCH_VARIABLE,
    }

    override fun toString(): String {
        return "ERROR: $reason: $message"
    }
}