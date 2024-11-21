package uk.matvey.server.login

import io.ktor.http.HttpHeaders.Referrer
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.html.respondHtml
import io.ktor.server.request.header
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.util.date.GMTDate
import uk.matvey.pauk.ktor.KtorHtmx.setHxRedirect
import uk.matvey.pauk.ktor.KtorKit.receiveParamsMap
import uk.matvey.pauk.ktor.Resource
import uk.matvey.server.index.getLoad
import uk.matvey.server.login.AuthJwt.TOKEN
import uk.matvey.server.login.AuthJwt.setTokenCookie
import uk.matvey.server.login.LoginHtml.login
import java.net.URI

object LoginResource : Resource {

    const val TARGET_URL = "targetUrl"

    override fun Route.routing() {
        route("/login") {
            getLoad("/login") {
                call.respondHtml {
                    val targetUrl = call.request.header(Referrer)
                        ?.let(::URI)
                        ?.query
                        ?.split('&')
                        ?.find { it.startsWith("$TARGET_URL=") }
                        ?.substringAfter('=')
                    login(targetUrl)
                }
            }
            post {
                val params = call.receiveParamsMap()
                call.setTokenCookie(params.getValue("username"))
                call.setHxRedirect(params[TARGET_URL] ?: "/")
                call.respond(OK, null)
            }
            delete {
                call.response.cookies.append(TOKEN, "", expires = GMTDate.START)
                call.setHxRedirect("/")
                call.respond(OK, null)
            }
        }
    }
}
