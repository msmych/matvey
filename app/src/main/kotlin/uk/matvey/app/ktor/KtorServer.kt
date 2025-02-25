package uk.matvey.app.ktor

import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import io.ktor.server.application.install
import io.ktor.server.application.serverConfig
import io.ktor.server.auth.authentication
import io.ktor.server.engine.applicationEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.uri
import io.ktor.server.response.respondText
import uk.matvey.app.Conf
import uk.matvey.app.Services
import uk.matvey.app.account.AuthException
import uk.matvey.app.auth.AuthJwt
import uk.matvey.app.auth.AuthResource.Companion.TARGET_URL
import uk.matvey.pauk.ktor.KtorHtmx.setHxRedirect
import uk.matvey.pauk.ktor.KtorKit.configureSsl
import uk.matvey.tmdb.TmdbClient

fun ktorServer(
    services: Services,
    tmdbClient: TmdbClient,
) = embeddedServer(
    factory = Netty,
    environment = environment(),
    configure = { config() }
) {
    install(StatusPages) {
        exception<AuthException> { call, _ ->
            val path = call.request.uri.let {
                if (it.startsWith('/')) {
                    it.substring(1)
                } else {
                    it
                }
                    .substringBefore('/')
            }
            call.setHxRedirect("/auth?$TARGET_URL=/$path")
            call.respond(Unauthorized, null)
        }
        exception<IllegalArgumentException> { call, e ->
            call.respondText(e.message ?: "BadRequest", status = BadRequest)
        }
    }
    authentication {
        register(AuthJwt.Required)
        register(AuthJwt.Optional)
    }
    install(CallLogging)
    ktorModule(services, tmdbClient)
}

private fun environment() = applicationEnvironment {
    serverConfig {
        watchPaths = when (Conf.profile) {
            Conf.Profile.LOCAL -> listOf("classes")
            else -> listOf()
        }
    }
}

private fun NettyApplicationEngine.Configuration.config() {
    when (Conf.profile) {
        Conf.Profile.PROD -> configureSsl(
            privateKeyPassword = Conf.app.jksPass(),
            keyStoreFilePath = "/certs/keystore.jks",
            keyStorePassword = Conf.app.jksPass(),
            keyAlias = "matvey-p12",
        ) {
            port = Conf.app.port
        }
        else -> connector {
            port = Conf.app.port
        }
    }
}
