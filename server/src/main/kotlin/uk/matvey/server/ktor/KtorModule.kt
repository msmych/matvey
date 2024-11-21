package uk.matvey.server.ktor

import io.ktor.server.application.Application
import io.ktor.server.http.content.staticResources
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import uk.matvey.server.index.indexRouting
import uk.matvey.server.login.LoginResource
import uk.matvey.server.settings.settingsRouting
import uk.matvey.server.styles.stylesRouting

fun Application.ktorModule() {
    routing {
        get("/health") {
            call.respondText("OK")
        }
        staticResources("/assets", "/assets")
        indexRouting()
        stylesRouting()
        with(LoginResource) { routing() }
        settingsRouting()
    }
}