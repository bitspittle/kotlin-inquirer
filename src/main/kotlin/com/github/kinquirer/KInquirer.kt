package com.github.kinquirer

import com.varabyte.kotter.foundation.session
import com.varabyte.kotter.foundation.text.textLine
import com.varabyte.kotter.runtime.Session

public class KInquirer internal constructor(
    block: KInquirer.() -> Unit,
) {
    private lateinit var session: Session
    internal fun <T> runKotterBlock(block: Session.() -> T): T {
        return session.block()
    }

    init {
        session {
            session = this
            block()
        }
    }

    public fun println(message: Any?) {
        session.section {
            textLine(message.toString())
        }.run()
    }
}

public fun kinquirer(block: KInquirer.() -> Unit) {
    KInquirer(block)
}