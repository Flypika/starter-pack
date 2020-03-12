package com.flypika.pack.presentation.ext

fun Boolean.then(block: () -> Unit): Boolean? {
    if (this) {
        block()
        return this
    }

    return null
}

fun <T> Boolean.thenTake(block: () -> T): T? {
    if (this) {
        return block()
    }

    return null
}
