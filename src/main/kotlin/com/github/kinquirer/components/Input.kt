//package com.github.kinquirer.components
//
//import com.github.kinquirer.core.Component
//import com.github.kinquirer.KInquirer
//import com.github.kinquirer.core.KInquirerEvent
//import com.github.kinquirer.core.KInquirerEvent.*
//import com.github.kinquirer.core.toAnsi
//import org.fusesource.jansi.Ansi.*
//import java.math.BigDecimal
//
//internal class InputComponent(
//    val message: String,
//    val default: String = "",
//    val hint: String = "",
//    val validation: (s: String) -> Boolean = { true },
//    val filter: (s: String) -> Boolean = { true },
//    val transform: (s: String) -> String = { s -> s },
//) : Component<String> {
//    private var value: String? = null
//    private var interacting = true
//    private var errorMessage = ""
//
//    override fun value(): String = value ?: default
//
//    override fun isInteracting(): Boolean = interacting
//
//    override fun onEvent(event: KInquirerEvent) {
//        errorMessage = ""
//        when (event) {
//            is KeyPressEnter -> {
//                if (validation(value())) {
//                    interacting = false
//                } else {
//                    errorMessage = "invalid input"
//                }
//            }
//            is KeyPressBackspace -> {
//                value = value?.dropLast(1)
//            }
//            is KeyPressSpace -> {
//                if (filter(" ")) {
//                    value = value?.plus(" ") ?: " "
//                }
//            }
//            is Character -> {
//                val tempVal = value.orEmpty() + event.c
//                if (filter(tempVal)) {
//                    value = tempVal
//                }
//            }
//            else -> {}
//        }
//    }
//
//    override fun render(): String = buildString {
//        // Question mark character
//        append("?".toAnsi { fgGreen(); bold() })
//        append(" ")
//
//        // Message
//        append(message.toAnsi { bold() })
//        append(" ")
//
//        when {
//            interacting && value.isNullOrEmpty() && hint.isNotBlank() -> {
//                // Hint
//                append("  ")
//                append(hint.toAnsi { fgBrightBlack() })
//                append(ansi().cursorLeft(hint.length + 2))
//            }
//            interacting -> {
//                // User Input
//                append(transform(value()))
//                // Error message
//                if (errorMessage.isNotBlank()) {
//                    append("  ")
//                    append(errorMessage.toAnsi { bold(); fgRed() })
//                    append(ansi().cursorLeft(errorMessage.length + 2))
//                }
//            }
//            else -> {
//                // User Input with new line
//                appendLine(transform(value()).toAnsi { fgCyan(); bold(); })
//            }
//        }
//    }
//}
//
//
//public fun KInquirer.promptInput(
//    message: String,
//    default: String = "",
//    hint: String = "",
//    validation: (s: String) -> Boolean = { true },
//    filter: (s: String) -> Boolean = { true },
//    transform: (s: String) -> String = { it }
//): String {
//    return prompt(InputComponent(message, default, hint, validation, filter, transform))
//}
//
//public fun KInquirer.promptInputPassword(
//    message: String,
//    default: String = "",
//    hint: String = "",
//    mask: String = "*"
//): String {
//    val validation: (s: String) -> Boolean = { true }
//    val filter: (s: String) -> Boolean = { true }
//    val transform: (s: String) -> String = { it.map { mask }.joinToString("") }
//
//    return prompt(InputComponent(message, default, hint, validation, filter, transform))
//}
//
//public fun KInquirer.promptInputNumber(
//    message: String,
//    default: String = "",
//    hint: String = "",
//    transform: (s: String) -> String = { it }
//): BigDecimal {
//    val validation: (s: String) -> Boolean = { it.matches("\\d+.?\\d*".toRegex()) }
//    val filter: (s: String) -> Boolean = { it.matches("\\d*\\.?\\d*".toRegex()) }
//
//    return BigDecimal(prompt(InputComponent(message, default, hint, validation, filter, transform)))
//}
