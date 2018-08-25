package io.xol.chunkstories.api.util.kotlin

import java.util.*

fun ClosedRange<Int>.random() =
        Random().nextInt((endInclusive + 1) - start) +  start