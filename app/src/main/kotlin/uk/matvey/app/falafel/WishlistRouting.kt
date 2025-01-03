package uk.matvey.app.falafel

import io.ktor.server.html.respondHtml
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import kotlinx.html.body
import uk.matvey.app.falafel.FalafelHtml.falafel

fun Route.falafelRouting() {
    route("/falafel") {
        get {
            call.respondHtml { body { falafel() } }
        }
    }
}