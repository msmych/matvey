package uk.matvey.app.html

import kotlinx.html.DIV
import kotlinx.html.HtmlBlockTag
import kotlinx.html.INPUT
import kotlinx.html.InputType
import kotlinx.html.div
import kotlinx.html.input
import kotlinx.html.label
import uk.matvey.kit.string.StringKit.capitalize

object CommonHtml {

    fun HtmlBlockTag.col(gap: Int? = null, classes: String? = null, block: DIV.() -> Unit) {
        val allClasses = listOfNotNull("col", gap?.let { "gap-$it" }, classes).joinToString(" ")
        div(allClasses, block)
    }

    fun HtmlBlockTag.row(
        gap: Int? = null,
        classes: String? = null,
        block: DIV.() -> Unit
    ) {
        val allClasses = listOfNotNull("row", gap?.let { "gap-$it" }, classes).joinToString(" ")
        div(allClasses, block)
    }

    fun HtmlBlockTag.inputGroup(
        name: String,
        type: InputType = InputType.text,
        required: Boolean = false,
        label: String? = capitalize(name),
        placeholder: String? = label,
        inputBlock: INPUT.() -> Unit = {},
    ) = col(gap = 2, classes = "input-group") {
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