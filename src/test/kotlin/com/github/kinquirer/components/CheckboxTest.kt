package com.github.kinquirer.components

import com.github.kinquirer.KInquirer
import com.github.kinquirer.kotter.confirmedTextLine
import com.github.kinquirer.kotter.hintTextLine
import com.github.kinquirer.kotter.questionMark
import com.github.kinquirer.prompts.promptCheckbox
import com.github.kinquirer.testKInquirer
import com.varabyte.kotter.foundation.text.bold
import com.varabyte.kotter.foundation.text.cyan
import com.varabyte.kotter.foundation.text.text
import com.varabyte.kotter.foundation.text.textLine
import com.varabyte.kotter.runtime.internal.ansi.Ansi
import com.varabyte.kotterx.test.terminal.sendCode
import org.junit.jupiter.api.Test

internal class CheckboxTest {

    private fun KInquirer.promptTestCheckbox() {
        promptCheckbox(
            message = "hello?",
            hint = "please select something",
            choices = listOf("A", "B", "C", "D"),
            maxNumOfSelection = 2,
            minNumOfSelection = 1,
            pageSize = 2,
        )
    }

    @Test
    fun `test checkbox scrolling down 2 steps`() = testKInquirer(
        block = { promptTestCheckbox() },
        process = {
            sendCode(Ansi.Csi.Codes.Keys.DOWN)
            sendCode(Ansi.Csi.Codes.Keys.DOWN)
        },
        expected = {
            questionMark()
            text(' ')
            bold { text("hello?") }
            text(' ')
            hintTextLine("please select something")
            textLine("   ◯ B")
            cyan(isBright = true) { text(" ❯ ") }; text("◯ "); confirmedTextLine("C")
            hintTextLine("(move up and down to reveal more choices)")
        }
    )

//    @Test
//    fun `test checkbox scrolling down till the end`() {
//        checkbox.onEventSequence {
//            add(KeyPressDown)
//            add(KeyPressDown)
//            add(KeyPressDown)
//            add(KeyPressDown)
//            add(KeyPressDown)
//            add(KeyPressDown)
//        }
//        val expected = buildString {
//            append("?".toAnsi { bold(); fgGreen() })
//            append(" ")
//            append("hello?".toAnsi { bold() })
//            append(" ")
//            appendLine("please select something".toAnsi { fgBrightBlack() })
//            appendLine("   ◯ C")
//            appendLine(" ❯ ".toAnsiStr { fgBrightCyan() } + "◯ D")
//            appendLine("(move up and down to reveal more choices)".toAnsi { fgBrightBlack() })
//        }
//        assertEquals(expected, checkbox.render())
//    }
//
//    @Test
//    fun `test checkbox scrolling down till the end and back up`() {
//        checkbox.onEventSequence {
//            add(KeyPressDown)
//            add(KeyPressDown)
//            add(KeyPressDown)
//            add(KeyPressDown)
//            add(KeyPressUp)
//            add(KeyPressUp)
//            add(KeyPressUp)
//            add(KeyPressUp)
//            add(KeyPressUp)
//        }
//        val expected = buildString {
//            append("?".toAnsi { bold(); fgGreen() })
//            append(" ")
//            append("hello?".toAnsi { bold() })
//            append(" ")
//            appendLine("please select something".toAnsi { fgBrightBlack() })
//            appendLine(" ❯ ".toAnsiStr { fgBrightCyan() } + "◯ A")
//            appendLine("   ◯ B")
//            appendLine("(move up and down to reveal more choices)".toAnsi { fgBrightBlack() })
//        }
//        assertEquals(expected, checkbox.render())
//    }
//
//    @Test
//    fun `test checkbox selection max limitation`() {
//        checkbox.onEventSequence {
//            add(KeyPressSpace)
//            add(KeyPressDown)
//            add(KeyPressSpace)
//            add(KeyPressDown)
//            add(KeyPressSpace)
//        }
//        val expected = buildString {
//            append("?".toAnsi { bold(); fgGreen() })
//            append(" ")
//            append("hello?".toAnsi { bold() })
//            append(" ")
//            appendLine("please select something".toAnsi { fgBrightBlack() })
//            appendLine("   " + "◉ ".toAnsiStr { fgGreen() } + "B".toAnsiStr { fgCyan(); bold() })
//            appendLine(" ❯ ".toAnsiStr { fgBrightCyan() } + "◯ C")
//            appendLine("(move up and down to reveal more choices)".toAnsi { fgBrightBlack() })
//            appendLine("max selection: 2".toAnsi { bold(); fgRed() })
//        }
//        assertEquals(expected, checkbox.render())
//    }
//
//    @Test
//    fun `test checkbox selection min limitation`() {
//        checkbox.onEventSequence {
//            add(KeyPressEnter)
//        }
//        val expected = buildString {
//            append("?".toAnsi { bold(); fgGreen() })
//            append(" ")
//            append("hello?".toAnsi { bold() })
//            append(" ")
//            appendLine("please select something".toAnsi { fgBrightBlack() })
//            appendLine(" ❯ ".toAnsiStr { fgBrightCyan() } + "◯ A")
//            appendLine("   ◯ B")
//            appendLine("(move up and down to reveal more choices)".toAnsi { fgBrightBlack() })
//            appendLine("min selection: 1".toAnsi { bold(); fgRed() })
//        }
//        assertEquals(expected, checkbox.render())
//    }
//
//    @Test
//    fun `test checkbox value`() {
//        checkbox.onEventSequence {
//            add(KeyPressSpace)
//            add(KeyPressDown)
//            add(KeyPressSpace)
//            add(KeyPressEnter)
//        }
//        assertEquals(listOf("1", "2"), checkbox.value())
//    }
//
//    @Test
//    fun `test checkbox view options`() {
//        val checkbox = CheckboxComponent(
//            message = "hello?",
//            hint = "please select something",
//            choices = listOf(
//                Choice("A", "1"),
//                Choice("B", "2"),
//            ),
//            viewOptions = CheckboxViewOptions(
//                questionMarkPrefix = "❓",
//                cursor = "👉",
//                nonCursor = " ",
//                checked = " ✅ ",
//                unchecked = " ⏹ ",
//            )
//        )
//
//        checkbox.onEventSequence {
//            add(KeyPressSpace)
//            add(KeyPressDown)
//        }
//
//        val expected = buildString {
//            append("❓")
//            append(" ")
//            append("hello?".toAnsi { bold() })
//            append(" ")
//            appendLine("please select something".toAnsi { fgBrightBlack() })
//            appendLine("  ✅ " + "A".toAnsiStr { fgCyan(); bold() })
//            appendLine("👉 ⏹ B")
//        }
//
//        assertEquals(expected, checkbox.render())
//
//    }

}
