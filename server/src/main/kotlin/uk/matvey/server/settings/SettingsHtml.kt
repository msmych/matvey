package uk.matvey.server.settings

import kotlinx.html.ButtonType
import kotlinx.html.HtmlBlockTag
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.textInput
import uk.matvey.pauk.ktor.KtorHtmx.hxDelete
import uk.matvey.pauk.ktor.KtorHtmx.hxGet
import uk.matvey.pauk.ktor.KtorHtmx.hxPatch
import uk.matvey.server.html.MatveyHtml.col
import uk.matvey.server.html.MatveyHtml.row
import uk.matvey.server.login.AccountPrincipal

object SettingsHtml {

    fun HtmlBlockTag.settings(principal: AccountPrincipal) = col(16) {
        div(classes = "t1") {
            +"Settings"
        }
        username(principal.username)
        button {
            hxDelete("/login")
            +"Logout"
        }
    }

    fun HtmlBlockTag.username(username: String) = row(8) {
        div(classes = "t3") {
            +"Username:"
        }
        div(classes = "t3") {
            +username
        }
        button {
            hxGet(path = "/settings/username-edit", target = "closest .row")
            +"📝"
        }
    }

    fun HtmlBlockTag.usernameEdit() = form(classes = "row gap-8") {
        hxPatch(path = "/settings/username", target = "closest .row")
        div(classes = "t3") {
            +"Username:"
        }
        textInput {
            name = "username"
            required = true
            placeholder = "New username"
        }
        button {
            type = ButtonType.submit
            +"✅"
        }
        button {
            hxGet(path = "/settings/username", target = "closest div.row")
            +"❌"
        }
    }
}