package uk.matvey.app.falafel

import io.ktor.server.html.respondHtml
import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import kotlinx.html.body
import uk.matvey.app.auth.AuthJwt.Required.authJwtRequired
import uk.matvey.app.falafel.FalafelHtml.falafel
import uk.matvey.app.index.getLoad
import uk.matvey.tmdb.TmdbClient

fun Route.falafelRouting(tmdbClient: TmdbClient) {
    authJwtRequired {
        route("/falafel") {
            getLoad("/falafel") {
                call.respondHtml { body { falafel() } }
            }
        }
    }
}