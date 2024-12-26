package uk.matvey.app.auth

import kotlinx.html.ButtonType
import kotlinx.html.HTML
import kotlinx.html.InputType
import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.form
import kotlinx.html.input
import uk.matvey.pauk.ktor.KtorHtmx.hxPost
import uk.matvey.app.auth.AuthResource.Companion.TARGET_URL
import uk.matvey.app.html.CommonHtml.inputGroup
import uk.matvey.app.html.CommonHtml.t1
import uk.matvey.app.html.CommonHtml.vertical

object AuthHtml {

    fun HTML.auth(targetUrl: String?) = body {
        form(classes = "card") {
            hxPost(path = "/auth", swap = "none")
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