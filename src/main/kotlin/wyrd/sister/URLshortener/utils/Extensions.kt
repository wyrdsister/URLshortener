package wyrd.sister.URLshortener.utils

import java.net.URI

val charPool : List<Char> = ('a'..'z') + ('A'..'Z')

fun String.isValidURL(): Boolean {
    try {
        URI.create(this).toURL()
        return true
    } catch (e: Exception) {
        return false
    }
}

fun generateString(length: Int): String{
    return List(length){ charPool.random() }.joinToString("")
}