package items

class File(
    name: String,
    parent: Folder?,
    private var file: Any? = null
) : Item(name, parent) {
    override fun buildContent(): String {
        return file.toString()
    }

}