package uk.matvey.app.index

import io.ktor.server.auth.principal
import io.ktor.server.html.respondHtml
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import uk.matvey.app.auth.AccountPrincipal
import uk.matvey.app.auth.AuthJwt.Optional.authJwtOptional
import uk.matvey.app.index.HomeHtml.homePage
import uk.matvey.app.index.IndexHtml.page

fun Route.indexRouting() {
    route("/") {
        authJwtOptional {
            get {
                val principal = call.principal<AccountPrincipal>()
                call.respondHtml { page(principal, MenuHtml.MenuTab.HOME) { homePage() } }
            }
        }
    }
}
