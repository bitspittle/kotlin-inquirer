package com.github.kinquirer

import com.varabyte.kotter.runtime.MainRenderScope
import com.varabyte.kotter.runtime.Session
import com.varabyte.kotterx.test.foundation.testSession
import com.varabyte.kotterx.test.terminal.TestTerminal
import com.varabyte.kotterx.test.terminal.resolveRerenders
import kotlinx.coroutines.*
import java.time.Duration
import kotlin.test.fail

private suspend fun blockUntil(maxWait: Duration = Duration.ofNanos(Long.MAX_VALUE), condition: () -> Boolean): Boolean {
    var maxWait = maxWait
    while (!condition() && maxWait > Duration.ZERO) {
        delay(10)
        maxWait -= Duration.ofMillis(10)
    }

    return maxWait > Duration.ZERO
}

internal fun testKInquirer(
    block: KInquirer.() -> Unit,
    process: suspend TestTerminal.() -> Unit,
    expected: MainRenderScope.() -> Unit
) {
    val expectedOutput = TestTerminal.consoleOutputFor(expected)

    object : KInquirer(block) {
        override fun runSession(block: Session.() -> Unit) {
            testSession { terminal ->
                val session = this
                val renderJob = CoroutineScope(Dispatchers.Default).launch {
                    block()
                }
                val eventsJob = CoroutineScope(Dispatchers.IO).launch {
                    blockUntil { session.activeSection != null }
                    terminal.process()

                    if (!blockUntil(maxWait = Duration.ofSeconds(1)) {
                        terminal.resolveRerenders() == expectedOutput
                    }) {
                        fail(
                            """
Terminal output did not equal expected value.

Output:
${terminal.resolveRerenders().joinToString("\n")}

Expected:
${expectedOutput.joinToString("\n")}
                            """
                        )
                    }
                    session.activeSection?.abort()
                }

                runBlocking {
                    listOf(renderJob, eventsJob).joinAll()
                }

                System.out.println("GOT OUT")
            }
        }
    }
}