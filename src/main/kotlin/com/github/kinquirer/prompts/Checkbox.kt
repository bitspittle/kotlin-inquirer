package com.github.kinquirer.prompts

import com.github.kinquirer.KInquirer
import com.github.kinquirer.core.Choice
import com.github.kinquirer.kotter.confirmedTextLine
import com.github.kinquirer.kotter.errorTextLine
import com.github.kinquirer.kotter.hintText
import com.github.kinquirer.kotter.questionMark
import com.varabyte.kotter.foundation.collections.liveSetOf
import com.varabyte.kotter.foundation.input.Keys
import com.varabyte.kotter.foundation.input.onKeyPressed
import com.varabyte.kotter.foundation.liveVarOf
import com.varabyte.kotter.foundation.runUntilSignal
import com.varabyte.kotter.foundation.text.*
import com.varabyte.kotter.runtime.render.RenderScope
import isOldTerminal
import kotlin.math.max
import kotlin.math.min

public data class CheckboxViewOptions(
    val questionMarkPrefix: RenderScope.() -> Unit = { questionMark() },
    val cursor: RenderScope.() -> Unit = { cyan { text(if (isOldTerminal) " > " else " ❯ ") } },
    val nonCursor: RenderScope.() -> Unit = { text("   ") },
    val checked: RenderScope.() -> Unit = { green { text(if (isOldTerminal) "(*) " else "◉ ") } },
    val unchecked: RenderScope.() -> Unit = { text(if (isOldTerminal) "( ) " else "◯ ") },
)

public fun KInquirer.promptCheckbox(
    message: String,
    choices: List<String>,
    hint: String = "",
    maxNumOfSelection: Int = Int.MAX_VALUE,
    minNumOfSelection: Int = 0,
    pageSize: Int = 10,
    viewOptions: CheckboxViewOptions = CheckboxViewOptions()
): List<String> {
    return promptCheckboxObject(
        message = message,
        choices = choices.map { Choice(it, it) },
        hint = hint,
        maxNumOfSelection = maxNumOfSelection,
        minNumOfSelection = minNumOfSelection,
        pageSize = pageSize,
        viewOptions = viewOptions,
    )
}

public fun <T> KInquirer.promptCheckboxObject(
    message: String,
    choices: List<Choice<T>>,
    hint: String = "",
    maxNumOfSelection: Int = Int.MAX_VALUE,
    minNumOfSelection: Int = 0,
    pageSize: Int = 10,
    viewOptions: CheckboxViewOptions = CheckboxViewOptions(),
): List<T> {
    return runKotterBlock {
        val selectedIndices = liveSetOf<Int>()
        var cursorIndex by liveVarOf(0)
        var choiceStartIndex by liveVarOf(0)
        var interacting by liveVarOf(true)
        var errorMessage by liveVarOf("")
        fun choiceEndIndex() = choiceStartIndex + pageSize

        section {
            // Question mark character
            viewOptions.questionMarkPrefix
            text(' ')

            // Message
            bold { text(message) }
            text(' ')

            if (interacting) {
                // Hint
                if (hint.isNotBlank()) {
                    hintText(hint)
                }

                // Choices
                textLine()
                choices.forEachIndexed { index, choice ->
                    if (index in choiceStartIndex until choiceEndIndex()) {
                        if (index == cursorIndex) {
                            viewOptions.cursor(this)
                        } else {
                            viewOptions.nonCursor(this)
                        }

                        if (index in selectedIndices) {
                            viewOptions.checked(this)
                        } else {
                            viewOptions.unchecked(this)
                        }

                        if (index == cursorIndex) {
                            confirmedTextLine(choice.displayName)
                        } else {
                            textLine(choice.displayName)
                        }
                    }
                }

                // Info message
                if (pageSize < choices.size) {
                    textLine("(move up and down to reveal more choices)")
                }
                // Error message
                if (errorMessage.isNotBlank()) {
                    errorTextLine(errorMessage)
                }
            } else {
                confirmedTextLine(
                    choices
                        .filterIndexed { index, _ -> index in selectedIndices }
                        .joinToString(", ") { choice -> choice.displayName }
                )
            }
        }.runUntilSignal {
            onKeyPressed {
                errorMessage = ""

                when (key) {
                    Keys.UP -> {
                        cursorIndex = max(0, cursorIndex - 1)
                        if (cursorIndex < choiceStartIndex) {
                            choiceStartIndex = Integer.max(0, choiceStartIndex - 1)
                        }
                    }

                    Keys.DOWN -> {
                        cursorIndex = min(choices.size - 1, cursorIndex + 1)
                        if (cursorIndex > choiceEndIndex() - 1) {
                            choiceStartIndex = min(choices.size - 1, choiceStartIndex + 1)
                        }
                    }

                    Keys.SPACE -> {
                        when {
                            cursorIndex in selectedIndices -> {
                                selectedIndices.remove(cursorIndex)
                            }
                            selectedIndices.size + 1 <= maxNumOfSelection -> {
                                selectedIndices.add(cursorIndex)
                            }
                            selectedIndices.size + 1 > maxNumOfSelection -> {
                                errorMessage = "max selection: $maxNumOfSelection"
                            }
                        }
                    }

                    Keys.ENTER -> {
                        if (selectedIndices.size < minNumOfSelection) {
                            errorMessage = "min selection: $minNumOfSelection"
                        } else {
                            interacting = false
                            signal()
                        }
                    }
                }
            }
        }

        choices
            .filterIndexed { index, _ -> index in selectedIndices }
            .map { choice -> choice.data }
    }
}
