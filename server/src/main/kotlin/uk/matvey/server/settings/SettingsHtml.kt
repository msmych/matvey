package uk.matvey.server.settings

import kotlinx.html.ButtonType
import kotlinx.html.HtmlBlockTag
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.passwordInput
import kotlinx.html.style
import kotlinx.html.textInput
import uk.matvey.pauk.htmx.Htmx.Swap.outerHTML
import uk.matvey.pauk.ktor.KtorHtmx.hxConfirm
import uk.matvey.pauk.ktor.KtorHtmx.hxDelete
import uk.matvey.pauk.ktor.KtorHtmx.hxGet
import uk.matvey.pauk.ktor.KtorHtmx.hxPatch
import uk.matvey.server.auth.AccountPrincipal
import uk.matvey.server.html.CommonHtml.horizontal
import uk.matvey.server.html.CommonHtml.t1
import uk.matvey.server.html.CommonHtml.t3
import uk.matvey.server.html.CommonHtml.vertical

object SettingsHtml {

    fun HtmlBlockTag.settings(principal: AccountPrincipal) = vertical(16) {
        t1("Settings")
        username(principal.username)
        password()
        button {
            style = "color: red;"
            hxDelete("/auth")
            hxConfirm("Are you sure want to logout?")
            +"Logout"
        }
    }

    fun HtmlBlockTag.username(username: String) = horizontal(gap = 8, classes = "center") {
        t3("Username:")
        t3(username)
        button {
            hxGet(path = "/settings/username-edit", target = "closest div.horizontal")
            +"📝"
        }
    }

    fun HtmlBlockTag.usernameEdit() = form(classes = "horizontal gap-8 center") {
        hxPatch(path = "/settings/username", target = "closest div.horizontal")
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
                hxGet(path = "/settings/username", target = "closest div.horizontal")
                +"❌"
            }
        }
    }

    fun HtmlBlockTag.password() = horizontal(gap = 8, classes = "center") {
        t3("Password:")
        t3("****")
        button {
            hxGet(path = "/settings/password-edit", target = "closest .horizontal", swap = outerHTML)
            +"📝"
        }
    }

    fun HtmlBlockTag.passwordEdit() = form(classes = "horizontal gap-8 center") {
        hxPatch(path = "/settings/password", swap = outerHTML)
        t3("Password:")
        horizontal(gap = 8, classes = "wrap") {
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
            horizontal {
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