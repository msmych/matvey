package uk.matvey.server.ktor

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
import uk.matvey.pauk.exception.AuthException
import uk.matvey.pauk.ktor.KtorHtmx.setHxRedirect
import uk.matvey.pauk.ktor.KtorKit.configureSsl
import uk.matvey.server.Conf
import uk.matvey.server.login.AuthJwt
import uk.matvey.server.login.LoginResource.TARGET_URL

fun ktorServer() = embeddedServer(
    factory = Netty,
    environment = environment(),
    configure = { config() }
) {
    install(StatusPages) {
        exception<AuthException> { call, _ ->
            call.setHxRedirect("/login?$TARGET_URL=${call.request.uri}")
            call.respond(Unauthorized, null)
        }
    }
    authentication {
        register(AuthJwt.Required)
        register(AuthJwt.Optional)
    }
    install(CallLogging)
    ktorModule()
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
            privateKeyPassword = Conf.server.jksPass(),
            keyStoreFilePath = "/certs/keystore.jks",
            keyStorePassword = Conf.server.jksPass(),
            keyAlias = "matvey-p12",
        ) {
            port = Conf.server.port
        }
        else -> connector {
            port = Conf.server.port
        }
    }
}
