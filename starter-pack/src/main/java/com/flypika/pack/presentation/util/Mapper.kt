package com.flypika.pack.presentation.util

interface Mapper<SRC, DST> {
    fun transform(data: SRC): DST
}