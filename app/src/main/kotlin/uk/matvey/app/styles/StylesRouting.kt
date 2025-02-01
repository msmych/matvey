package uk.matvey.app.styles

import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlinx.css.Align
import kotlinx.css.Border
import kotlinx.css.BorderStyle
import kotlinx.css.Color
import kotlinx.css.CssBuilder
import kotlinx.css.Cursor
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.FlexWrap
import kotlinx.css.FontWeight
import kotlinx.css.Margin
import kotlinx.css.Overflow
import kotlinx.css.Padding
import kotlinx.css.Position
import kotlinx.css.alignItems
import kotlinx.css.background
import kotlinx.css.backgroundColor
import kotlinx.css.body
import kotlinx.css.border
import kotlinx.css.borderColor
import kotlinx.css.borderRadius
import kotlinx.css.borderStyle
import kotlinx.css.borderWidth
import kotlinx.css.button
import kotlinx.css.color
import kotlinx.css.cursor
import kotlinx.css.display
import kotlinx.css.em
import kotlinx.css.filter
import kotlinx.css.flexDirection
import kotlinx.css.flexWrap
import kotlinx.css.fontFamily
import kotlinx.css.fontSize
import kotlinx.css.fontWeight
import kotlinx.css.gap
import kotlinx.css.h1
import kotlinx.css.h2
import kotlinx.css.h3
import kotlinx.css.header
import kotlinx.css.height
import kotlinx.css.input
import kotlinx.css.margin
import kotlinx.css.marginLeft
import kotlinx.css.maxWidth
import kotlinx.css.overflow
import kotlinx.css.p
import kotlinx.css.padding
import kotlinx.css.position
import kotlinx.css.properties.deg
import kotlinx.css.properties.rotate
import kotlinx.css.properties.transform
import kotlinx.css.px
import kotlinx.css.width
import uk.matvey.pauk.ktor.KtorKit.respondCss

fun Route.stylesRouting() {
    get("/styles.css") {
        call.respondCss { stylesCss() }
    }
}

private fun CssBuilder.stylesCss() {
    body {
        margin = Margin(0.px)
        fontFamily = """"Mona Sans", sans-serif"""
    }
    headerCss()
    markupCss()
    hCss()
    buttonCss()
    inputCss()
    p {
        margin = Margin(0.px)
        borderRadius = 4.px
    }
    rule("input[type=radio]:checked") {
        borderWidth = 6.px
    }
    rule("input[name=q]") {
        display = Display.none
    }
    rule("input[name=filter][value=NONE]:checked ~ label > input[name=q]") {
        display = Display.block
    }
}

private fun CssBuilder.headerCss() {
    header {
        height = 64.px
        overflow = Overflow.hidden
        position = Position.relative
    }
    rule("header > img") {
        position = Position.absolute
        transform {
            rotate(180.deg)
        }
        filter = "invert(0)"
    }
}

private fun CssBuilder.hCss() {
    h1 {
        margin = Margin(0.px)
    }
    h2 {
        margin = Margin(0.px)
    }
    h3 {
        margin = Margin(0.px)
    }
}

private fun CssBuilder.markupCss() {
    rule(".col") {
        display = Display.flex
        flexDirection = FlexDirection.column
    }
    rule(".row") {
        display = Display.flex
        flexDirection = FlexDirection.row
    }
    rule(".wrap") {
        flexWrap = FlexWrap.wrap
    }
    rule(".center") {
        alignItems = Align.center
    }
    (0..64).forEach { i ->
        rule(".gap-$i") {
            gap = i.px
        }
    }
}

private fun CssBuilder.buttonCss() {
    button {
        padding = Padding(8.px, 16.px)
        maxWidth = 256.px
        color = Color.black
        background = "transparent"
        border = Border.none
        borderRadius = 4.px
        fontSize = 1.em
    }
    rule("button:hover") {
        backgroundColor = Color.aliceBlue
        cursor = Cursor.pointer
    }
    rule("button.primary") {
        padding = Padding(16.px, 16.px)
        fontSize = 1.2.em
        fontWeight = FontWeight.bold
        backgroundColor = Color.darkCyan
        borderRadius = 8.px
        color = Color.white
    }
    rule("button.primary:hover") {
        backgroundColor = Color.darkCyan
    }
    rule("button.active") {
        fontWeight = FontWeight.bold
    }
}

private fun CssBuilder.inputCss() {
    input {
        padding = Padding(8.px)
        borderColor = Color.lightGray
        borderStyle = BorderStyle.solid
        borderRadius = 4.px
        fontSize = 1.em
    }
    rule(".input-group") {
        width = 256.px
    }
    rule(".input-group label") {
        marginLeft = 4.px
        fontSize = 0.8.em
        color = Color.gray
    }
}

