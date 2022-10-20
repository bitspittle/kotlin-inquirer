package com.github.kinquirer.prompts

import com.github.kinquirer.KInquirer
import com.github.kinquirer.kotter.confirmedText
import com.github.kinquirer.kotter.errorText
import com.github.kinquirer.kotter.hintText
import com.github.kinquirer.kotter.questionMark
import com.varabyte.kotter.foundation.input.*
import com.varabyte.kotter.foundation.liveVarOf
import com.varabyte.kotter.foundation.text.bold
import com.varabyte.kotter.foundation.text.text
import com.varabyte.kotter.foundation.text.textLine
import java.math.BigDecimal

public fun KInquirer.promptInputNumber(
    message: String,
    default: String = "",
    hint: String = "",
    transform: (c: Char) -> Char = { it }
): BigDecimal {
    val validation: (s: String) -> Boolean = { it.matches("\\d+.?\\d*".toRegex()) }
    val filter: (s: String) -> Boolean = { it.matches("\\d*\\.?\\d*".toRegex()) }

    return BigDecimal(
        promptInput(
            message,
            default,
            hint,
            validation,
            filter,
            transform
        )
    )
}

public fun KInquirer.promptInputPassword(
    message: String,
    default: String = "",
    hint: String = "",
    mask: Char = '*'
): String {
    val validation: (s: String) -> Boolean = { true }
    val filter: (s: String) -> Boolean = { true }
    val transform: (s: Char) -> Char = { mask }

    return promptInput(
        message,
        default,
        hint,
        validation,
        filter,
        transform
    )
}

public fun KInquirer.promptInput(
    message: String,
    default: String = "",
    hint: String = "",
    validation: (s: String) -> Boolean = { true },
    filter: (s: String) -> Boolean = { true },
    transform: (c: Char) -> Char = { it }
): String {
    return runKotterBlock {
        var value by liveVarOf(default)
        var errorMessage by liveVarOf("")
        var interacting by liveVarOf(true)

        section {
            questionMark()
            text(' ')
            bold { text(message) }
            text(' ')

            when {
                interacting -> {
                    input(viewMap = { transform(ch) })
                    if (value.isBlank() && hint.isNotBlank()) {
                        hintText(hint)
                    }

                    if (errorMessage.isNotBlank()) {
                        textLine()
                        errorText(errorMessage)
                    }
                }

                else -> {
                    confirmedText(value.map(transform).joinToString(""))
                }
            }
        }.runUntilInputEntered {
            onInputChanged {
                if (input.isNotBlank() && !filter(input)) rejectInput()
                value = input
            }
            onInputEntered {
                if (validation(input)) {
                    interacting = false
                } else {
                    rejectInput()
                    errorMessage = "invalid input"
                }
            }
        }

        value
    }
}
