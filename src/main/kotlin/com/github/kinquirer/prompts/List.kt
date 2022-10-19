package com.github.kinquirer.prompts

import com.github.kinquirer.KInquirer
import com.github.kinquirer.core.Choice
import com.github.kinquirer.kotter.confirmedTextLine
import com.github.kinquirer.kotter.hintText
import com.github.kinquirer.kotter.questionMark
import com.varabyte.kotter.foundation.input.Keys
import com.varabyte.kotter.foundation.input.onKeyPressed
import com.varabyte.kotter.foundation.input.runUntilKeyPressed
import com.varabyte.kotter.foundation.liveVarOf
import com.varabyte.kotter.foundation.text.*
import com.varabyte.kotter.runtime.render.RenderScope
import isOldTerminal
import kotlin.math.max
import kotlin.math.min

public data class ListViewOptions(
    val questionMarkPrefix: RenderScope.() -> Unit = { questionMark() },
    val cursor: RenderScope.() -> Unit = { cyan(isBright = true) { text(if (isOldTerminal) " > " else " ❯ ") } },
    val nonCursor: RenderScope.() -> Unit = { text("   ") },
)

public fun KInquirer.promptList(
    message: String,
    choices: List<String> = emptyList(),
    hint: String = "",
    pageSize: Int = Int.MAX_VALUE,
    viewOptions: ListViewOptions = ListViewOptions()
): String {
    return promptListObject(
        message = message,
        hint = hint,
        choices = choices.map { choice -> Choice(choice, choice) },
        pageSize = pageSize,
        viewOptions = viewOptions,
    )
}

public fun <T> KInquirer.promptListObject(
    message: String,
    choices: List<Choice<T>> = emptyList(),
    hint: String = "",
    pageSize: Int = Int.MAX_VALUE,
    viewOptions: ListViewOptions = ListViewOptions()
): T {

    return runKotterBlock {
        var cursorIndex by liveVarOf(0)
        var choiceStartIndex by liveVarOf(0)
        var interacting by liveVarOf(true)
        fun choiceEndIndex() = choiceStartIndex + pageSize

        section {
            // Question mark character
            viewOptions.questionMarkPrefix(this)
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
                            confirmedTextLine(choice.displayName);
                        } else {
                            viewOptions.nonCursor(this)
                            textLine(choice.displayName)
                        }
                    }
                }

                // Info message
                if (pageSize < choices.size) {
                    textLine("(move up and down to reveal more choices)")
                }
            } else {
                confirmedTextLine(choices[cursorIndex].displayName)
            }
        }.runUntilKeyPressed(Keys.ENTER) {
            onKeyPressed {
                when (key) {
                    Keys.UP -> {
                        cursorIndex = max(0 , cursorIndex - 1)
                        if (cursorIndex < choiceStartIndex) {
                            choiceStartIndex = max(0, choiceStartIndex - 1)
                        }
                    }
                    Keys.DOWN -> {
                        cursorIndex = min(choices.size - 1, cursorIndex + 1)
                        if (cursorIndex > choiceEndIndex() - 1) {
                            choiceStartIndex = min(choices.size - 1, choiceStartIndex + 1)
                        }
                    }
                    Keys.ENTER -> interacting = false
                }
            }
        }

        choices[cursorIndex].data
    }
}
