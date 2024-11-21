package uk.matvey.server.login

import kotlinx.html.ButtonType
import kotlinx.html.HTML
import kotlinx.html.InputType
import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.input
import uk.matvey.pauk.ktor.KtorHtmx.hxPost
import uk.matvey.server.html.MatveyHtml.col
import uk.matvey.server.html.MatveyHtml.inputGroup
import uk.matvey.server.login.LoginResource.TARGET_URL

object LoginHtml {

    fun HTML.login(targetUrl: String?) = body {
        form(classes = "card") {
            hxPost(path = "/login", swap = "none")
            col(16) {
                div(classes = "t1") {
                    +"Login"
                }
                col(24) {
                    col(8) {
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