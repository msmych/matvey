package uk.matvey.app.index

import io.ktor.server.auth.principal
import io.ktor.server.html.respondHtml
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingHandler
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import uk.matvey.pauk.ktor.KtorHtmx.isHxRequest
import uk.matvey.app.index.IndexHtml.index
import uk.matvey.app.auth.AccountPrincipal
import uk.matvey.app.auth.AuthJwt.Optional.authJwtOptional

fun Route.indexRouting() {
    route("/") {
        authJwtOptional {
            get {
                val principal = call.principal<AccountPrincipal>()
                call.respondHtml { index(principal) }
            }
        }
    }
}

fun Route.getLoad(loadContent: String?, body: RoutingHandler) = get {
    if (!call.isHxRequest()) {
        val principal = call.principal<AccountPrincipal>()
        return@get call.respondHtml { index(principal, loadContent) }
    }
    body()
}