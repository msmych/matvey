package uk.matvey.server.ktor

import io.ktor.server.application.Application
import io.ktor.server.http.content.staticResources
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import uk.matvey.server.Services
import uk.matvey.server.auth.AuthResource
import uk.matvey.server.index.indexRouting
import uk.matvey.server.settings.SettingsResource
import uk.matvey.server.styles.stylesRouting

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
    }
}