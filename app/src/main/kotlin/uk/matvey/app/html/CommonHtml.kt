package uk.matvey.app.html

import kotlinx.html.DIV
import kotlinx.html.HtmlBlockTag
import kotlinx.html.INPUT
import kotlinx.html.InputType
import kotlinx.html.div
import kotlinx.html.input
import kotlinx.html.label
import uk.matvey.kit.string.StringKit.capital

object CommonHtml {

    fun HtmlBlockTag.vertical(gap: Int? = null, classes: String? = null, block: DIV.() -> Unit) {
        val allClasses = listOfNotNull("vertical", gap?.let { "gap-$it" }, classes).joinToString(" ")
        div(allClasses, block)
    }

    fun HtmlBlockTag.horizontal(
        gap: Int? = null,
        classes: String? = null,
        block: DIV.() -> Unit
    ) {
        val allClasses = listOfNotNull("horizontal", gap?.let { "gap-$it" }, classes).joinToString(" ")
        div(allClasses, block)
    }

    fun HtmlBlockTag.t1(text: String) = div(classes = "t1") {
        +text
    }

    fun HtmlBlockTag.t2(text: String) = div(classes = "t2") {
        +text
    }

    fun HtmlBlockTag.t3(text: String) = div(classes = "t3") {
        +text
    }

    fun HtmlBlockTag.inputGroup(
        name: String,
        type: InputType = InputType.text,
        required: Boolean = false,
        label: String? = capital(name),
        placeholder: String? = label,
        inputBlock: INPUT.() -> Unit = {},
    ) = vertical(gap = 2, classes = "input-group") {
        label?.let {
            label {
                htmlFor = name
                +it
                if (required) {
                    +"*"
                }
            }
        }
        input {
            this.type = type
            this.name = name
            this.required = required
            placeholder?.let { this.placeholder = it }
            inputBlock()
        }
    }
}