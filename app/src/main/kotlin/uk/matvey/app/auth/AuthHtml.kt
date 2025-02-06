package uk.matvey.app.auth

import kotlinx.html.ButtonType
import kotlinx.html.HtmlBlockTag
import kotlinx.html.InputType
import kotlinx.html.button
import kotlinx.html.form
import kotlinx.html.h1
import kotlinx.serialization.json.put
import uk.matvey.app.auth.AuthResource.Companion.TARGET_URL
import uk.matvey.app.html.CommonHtml.col
import uk.matvey.app.html.CommonHtml.inputGroup
import uk.matvey.pauk.htmx.Htmx.Swap.none
import uk.matvey.pauk.ktor.KtorHtmx.hxPost
import uk.matvey.pauk.ktor.KtorHtmx.hxSwap
import uk.matvey.pauk.ktor.KtorHtmx.hxVals

object AuthHtml {

    fun HtmlBlockTag.auth(targetUrl: String?) = form {
        hxPost("/auth")
        hxSwap(none)
        targetUrl?.let {
            hxVals {
                put(TARGET_URL, it)
            }
        }
        col(16) {
            h1 { +"Login" }
            col(24) {
                col(8) {
                    inputGroup(name = "username", required = true)
                    inputGroup(name = "password", type = InputType.password, required = true)
                }
                button(classes = "primary") {
                    type = ButtonType.submit
                    +"Login"
                }
            }
        }
    }
}