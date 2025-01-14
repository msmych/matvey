package uk.matvey.app.auth

import kotlinx.html.ButtonType
import kotlinx.html.HTML
import kotlinx.html.InputType
import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.form
import kotlinx.html.h1
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import uk.matvey.app.auth.AuthResource.Companion.TARGET_URL
import uk.matvey.app.html.CommonHtml.col
import uk.matvey.app.html.CommonHtml.inputGroup
import uk.matvey.kit.json.JsonKit.JSON
import uk.matvey.pauk.ktor.KtorHtmx.hxPost

object AuthHtml {

    fun HTML.auth(targetUrl: String?) = body {
        form(classes = "card") {
            hxPost(path = "/auth", swap = "none")
            targetUrl?.let {
                attributes["hx-vals"] = JSON.encodeToString(buildJsonObject {
                    put(TARGET_URL, it)
                })
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
}