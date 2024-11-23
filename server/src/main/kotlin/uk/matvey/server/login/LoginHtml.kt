package uk.matvey.server.login

import kotlinx.html.ButtonType
import kotlinx.html.HTML
import kotlinx.html.InputType
import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.form
import kotlinx.html.input
import uk.matvey.pauk.ktor.KtorHtmx.hxPost
import uk.matvey.server.html.CommonHtml.inputGroup
import uk.matvey.server.html.CommonHtml.t1
import uk.matvey.server.html.CommonHtml.vertical
import uk.matvey.server.login.LoginResource.TARGET_URL

object LoginHtml {

    fun HTML.login(targetUrl: String?) = body {
        form(classes = "card") {
            hxPost(path = "/login", swap = "none")
            vertical(16) {
                t1("Login")
                vertical(24) {
                    vertical(8) {
                        inputGroup(name = "username", required = true)
                        inputGroup(name = "password", type = InputType.password, required = true)
                        targetUrl?.let {
                            input {
                                name = TARGET_URL
                                type = InputType.hidden
                                value = it
                            }
                        }
                    }
                    button(classes = "primary") {
                        type = ButtonType.submit
                        +"Login"
                    }
                }
            }
        }
    }
}