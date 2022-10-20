package com.github.kinquirer.kotter

import com.varabyte.kotter.foundation.text.*
import com.varabyte.kotter.runtime.render.RenderScope

public fun RenderScope.questionMark() {
    green { bold { text('?') } }
}

public fun RenderScope.hintText(text: String) {
    black(isBright = true) { text(text) }
}

public fun RenderScope.hintTextLine(text: String) {
    hintText(text); textLine()
}

public fun RenderScope.errorText(text: String) {
    red { bold { text(text) } }
}

public fun RenderScope.errorTextLine(text: String) {
    errorText(text); textLine()
}

public fun RenderScope.confirmedText(text: String) {
    cyan { bold { text(text) } }
}

public fun RenderScope.confirmedTextLine(text: String) {
    confirmedText(text); textLine()
}