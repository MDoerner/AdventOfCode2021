package adventOfCode2021.utility

class MutableStack<T> {
    private val elements = ArrayList<T>()

    val isEmpty get() = elements.isEmpty()

    fun push(item: T){
        elements.add(item)
    }

    fun pop(): T?{
        if (isEmpty) return null
        val lastItemIndex = elements.count() - 1
        val item = elements[lastItemIndex]
        elements.removeAt(lastItemIndex)
        return item
    }
}

fun<T> MutableStack<T>.toList() : List<T>{
    val itemsTopToBottom = mutableListOf<T>()
    while (!this.isEmpty){
        val topValue = this.pop()
        if (topValue != null){
            itemsTopToBottom.add(topValue)
        }
    }
    return itemsTopToBottom
}