package uk.matvey.server.settings

import kotlinx.html.ButtonType
import kotlinx.html.HtmlBlockTag
import kotlinx.html.InputType
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.style
import kotlinx.html.textInput
import uk.matvey.pauk.ktor.KtorHtmx.hxDelete
import uk.matvey.pauk.ktor.KtorHtmx.hxGet
import uk.matvey.pauk.ktor.KtorHtmx.hxPatch
import uk.matvey.server.html.MatveyHtml.col
import uk.matvey.server.html.MatveyHtml.inputGroup
import uk.matvey.server.html.MatveyHtml.row
import uk.matvey.server.html.MatveyHtml.t1
import uk.matvey.server.html.MatveyHtml.t2
import uk.matvey.server.html.MatveyHtml.t3
import uk.matvey.server.login.AccountPrincipal

object SettingsHtml {

    fun HtmlBlockTag.settings(principal: AccountPrincipal) = col(16) {
        t1("Settings")
        username(principal.username)
        password()
        button {
            hxDelete("/login")
            +"Logout"
        }
    }

    fun HtmlBlockTag.username(username: String) = row(8) {
        style = "align-items: center;"
        t3("Username:")
        t3(username)
        button {
            hxGet(path = "/settings/username-edit", target = "closest .row")
            +"📝"
        }
    }

    fun HtmlBlockTag.usernameEdit() = form(classes = "row gap-8") {
        style = "align-items: center;"
        hxPatch(path = "/settings/username", target = "closest div.row")
        t3("Username:")
        textInput {
            name = "username"
            required = true
            placeholder = "New username"
        }
        div {
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

    fun HtmlBlockTag.password() = row(8) {
        style = "align-items: center;"
        t3("Password:")
        t3("****")
        button {
            hxGet(path = "/settings/password-edit", target = "closest .row", swap = "outerHTML")
            +"📝"
        }
    }

    fun HtmlBlockTag.passwordEdit() = form(classes = "col gap-8") {
        hxPatch(path = "/settings/password", swap = "outerHTML")
        t2("Update password")
        inputGroup(name = "currentPassword", type = InputType.password, required = true, label = "Current password")
        inputGroup(name = "newPassword", type = InputType.password, required = true, label = "New password")
        div {
            button {
                type = ButtonType.submit
                +"✅"
            }
            button {
                hxGet(path = "/settings/password", target = "closest form")
                +"❌"
            }
        }
    }
}