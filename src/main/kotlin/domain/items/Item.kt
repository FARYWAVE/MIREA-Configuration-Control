package items

abstract class Item(
    var name: String,
    var parent: Folder?
) {
    abstract fun buildContent(): String

    fun buildPath(): List<Item> {
        val result = mutableListOf(this)
        var parent = parent
        while (parent != null) {
            result.add(0, parent)
            parent = parent.parent
        }
        return result
    }
}