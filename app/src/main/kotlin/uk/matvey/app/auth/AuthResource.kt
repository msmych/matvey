package uk.matvey.app.auth

import io.ktor.http.HttpHeaders.Referrer
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.html.respondHtml
import io.ktor.server.request.header
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.util.date.GMTDate
import uk.matvey.kit.net.NetKit.queryParamOrNull
import uk.matvey.pauk.ktor.KtorHtmx.setHxRedirect
import uk.matvey.pauk.ktor.KtorKit.receiveParamsMap
import uk.matvey.pauk.ktor.Resource
import uk.matvey.app.account.AccountService
import uk.matvey.app.auth.AuthHtml.auth
import uk.matvey.app.auth.AuthJwt.TOKEN
import uk.matvey.app.auth.AuthJwt.setTokenCookie
import uk.matvey.app.index.getLoad
import java.net.URI

class AuthResource(
    private val accountService: AccountService,
) : Resource {

    override fun Route.routing() {
        route("/auth") {
            getLoad("/auth") {
                call.respondHtml {
                    val targetUrl = call.request.header(Referrer)?.let(::URI)?.queryParamOrNull(TARGET_URL)
                    auth(targetUrl)
                }
            }
            post {
                val params = call.receiveParamsMap()
                val username = params.getValue(USERNAME)
                val password = params.getValue(PASSWORD)
                val account = accountService.ensureAccount(username, password)
                call.setTokenCookie(account.id, username)
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

    companion object {
        const val TARGET_URL = "targetUrl"

        const val USERNAME = "username"
        const val PASSWORD = "password"
    }
}
