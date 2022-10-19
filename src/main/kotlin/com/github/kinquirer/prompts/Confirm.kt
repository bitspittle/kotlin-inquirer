package com.github.kinquirer.prompts

import com.github.kinquirer.KInquirer
import com.github.kinquirer.kotter.confirmedText
import com.github.kinquirer.kotter.questionMark
import com.varabyte.kotter.foundation.input.Keys
import com.varabyte.kotter.foundation.input.onKeyPressed
import com.varabyte.kotter.foundation.liveVarOf
import com.varabyte.kotter.foundation.runUntilSignal
import com.varabyte.kotter.foundation.text.*

public fun KInquirer.promptConfirm(
    message: String,
    default: Boolean = false
): Boolean {
    return runKotterBlock {
        var interacting by liveVarOf(true)
        var confirmed by liveVarOf(default)

        section {
            questionMark()
            text(' ')
            bold { text(message) }
            text(' ')

            when {
                interacting && confirmed -> text("[Yes] No ")
                interacting && !confirmed -> text(" Yes [No]")
                !interacting && confirmed -> confirmedText("Yes")
                else -> confirmedText("No")
            }
        }.runUntilSignal {
            onKeyPressed {
                when (key) {
                    Keys.LEFT -> confirmed = true
                    Keys.RIGHT -> confirmed = false
                    Keys.Y, Keys.Y_UPPER -> confirmed = true
                    Keys.N, Keys.N_UPPER -> confirmed = false
                    Keys.ENTER -> { interacting = false; signal() }
                }
            }
        }

        confirmed
    }
}
