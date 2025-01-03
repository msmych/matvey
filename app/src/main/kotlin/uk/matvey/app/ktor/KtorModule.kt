package uk.matvey.app.ktor

import io.ktor.server.application.Application
import io.ktor.server.http.content.staticResources
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import uk.matvey.app.Services
import uk.matvey.app.auth.AuthResource
import uk.matvey.app.falafel.falafelRouting
import uk.matvey.app.index.indexRouting
import uk.matvey.app.settings.SettingsResource
import uk.matvey.app.styles.stylesRouting

fun Application.ktorModule(services: Services) {
    val resources = listOf(
        AuthResource(services.accountService),
        SettingsResource(services.accountService, services.pool),
    )
    routing {
        get("/health") {
            call.respondText("OK")
        }
        staticResources("/assets", "/assets")
        indexRouting()
        stylesRouting()
        resources.forEach { with(it) { routing() } }
        falafelRouting()
    }
}