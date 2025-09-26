package items

class Folder(name: String, parent: Folder?): Item(name, parent) {
    private val content = mutableListOf<Item>()

    fun addFolder(folderName: String): Boolean {
        if (content.any { it.name == folderName }) return false
        content.add(Folder(folderName, this))
        return true
    }

    fun addFile(fileName: String, file: Any? = null): Boolean {
        if (content.any { it.name == fileName }) return false
        content.add(File(fileName, this))
        return true
    }

    fun deleteFolder(folderName: String): Boolean {
        return content.removeIf { (it.name == folderName) and (it is Folder)}
    }

    fun deleteFile(fileName: String): Boolean {
        return content.removeIf { (it.name == fileName) and (it is File)}
    }

    fun findChild(childName: String): Item? {
        return content.find { it.name == childName }
    }

    fun buildBranch(start: String): String{
        var result = start + name + '\n'
        for (i in 0..<content.size) {
            val child = content.elementAt(i)
            var childStart = start
                .replace("├─", "│  ")
                .replace("└─", "      ")
            childStart += (if (i + 1 == content.size) "└─" else "├─")
            result += if (child is Folder) child.buildBranch(childStart)
            else childStart + name
        }
        return result
    }

    override fun buildContent(): String {
        var result = name + "\n"
        if (content.size > 0) {
            for (i in 0..<content.size) {
                val child = content.elementAt(i)
                val isFinal = i + 1 == content.size
                result += (if (isFinal) "└─" else "├─") + child.name
            }
            return result
        }
        return "$result└─<EMPTY>"
    }
}