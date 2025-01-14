package uk.matvey.app.settings

import kotlinx.html.ButtonType
import kotlinx.html.HtmlBlockTag
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h1
import kotlinx.html.h3
import kotlinx.html.passwordInput
import kotlinx.html.style
import kotlinx.html.textInput
import uk.matvey.app.auth.AccountPrincipal
import uk.matvey.app.html.CommonHtml.col
import uk.matvey.app.html.CommonHtml.row
import uk.matvey.pauk.htmx.Htmx.Swap.outerHTML
import uk.matvey.pauk.ktor.KtorHtmx.hxConfirm
import uk.matvey.pauk.ktor.KtorHtmx.hxDelete
import uk.matvey.pauk.ktor.KtorHtmx.hxGet
import uk.matvey.pauk.ktor.KtorHtmx.hxPatch

object SettingsHtml {

    fun HtmlBlockTag.settings(principal: AccountPrincipal) = col(16) {
        h1 { +"Settings" }
        username(principal.username)
        password()
        button {
            style = "color: red;"
            hxDelete("/auth")
            hxConfirm("Are you sure want to logout?")
            +"Logout"
        }
    }

    fun HtmlBlockTag.username(username: String) = row(gap = 8, classes = "center") {
        h3 { +"Username:" }
        h3 { +username }
        button {
            hxGet(path = "/settings/username-edit", target = "closest div.row")
            +"📝"
        }
    }

    fun HtmlBlockTag.usernameEdit() = form(classes = "row gap-8 center") {
        hxPatch(path = "/settings/username", target = "closest div.row")
        h3 { +"Username:" }
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

    fun HtmlBlockTag.password() = row(gap = 8, classes = "center") {
        h3 { +"Password:" }
        h3 { +"****" }
        button {
            hxGet(path = "/settings/password-edit", target = "closest .row", swap = outerHTML)
            +"📝"
        }
    }

    fun HtmlBlockTag.passwordEdit() = form(classes = "row gap-8 center") {
        hxPatch(path = "/settings/password", swap = outerHTML)
        h3 { +"Password:" }
        row(gap = 8, classes = "wrap") {
            passwordInput {
                name = "currentPassword"
                required = true
                placeholder = "Current password"
            }
            passwordInput {
                name = "newPassword"
                required = true
                placeholder = "New password"
            }
            row {
                button {
                    type = ButtonType.submit
                    +"✅"
                }
                button {
                    hxGet(path = "/settings/password", target = "closest form", swap = outerHTML)
                    +"❌"
                }
            }
        }
    }
}