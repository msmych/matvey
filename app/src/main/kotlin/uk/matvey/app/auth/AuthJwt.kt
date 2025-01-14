package uk.matvey.app.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.JWTVerificationException
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.AuthenticationContext
import io.ktor.server.auth.AuthenticationProvider
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.routing.Route
import io.ktor.util.date.GMTDate
import io.ktor.util.date.plus
import io.netty.handler.codec.http.cookie.CookieHeaderNames.SAMESITE
import io.netty.handler.codec.http.cookie.CookieHeaderNames.SameSite
import uk.matvey.app.Conf
import uk.matvey.app.Conf.APP_NAME
import uk.matvey.pauk.exception.AuthException
import kotlin.time.Duration.Companion.hours

object AuthJwt {

    const val TOKEN = "token"

    private val algorithm = Algorithm.HMAC256(Conf.app.jwtSecret)

    object Required : AuthenticationProvider(Config) {

        const val REQUIRED_JWT = "required-jwt"

        object Config : AuthenticationProvider.Config(REQUIRED_JWT)

        fun Route.authJwtRequired(build: Route.() -> Unit) = authenticate(REQUIRED_JWT) { build() }

        override suspend fun onAuthenticate(context: AuthenticationContext) {
            processAuth(context, required = true)
        }

        fun ApplicationCall.accountPrincipal() = requireNotNull(principal<AccountPrincipal>()) {
            "Account principal is missing"
        }
    }

    object Optional : AuthenticationProvider(Config) {

        const val OPTIONAL_JWT = "optional-jwt"

        object Config : AuthenticationProvider.Config(OPTIONAL_JWT)

        override suspend fun onAuthenticate(context: AuthenticationContext) {
            processAuth(context, required = false)
        }

        fun Route.authJwtOptional(build: Route.() -> Unit) = authenticate(OPTIONAL_JWT) { build() }
    }

    fun processAuth(context: AuthenticationContext, required: Boolean) {
        context.call.request.cookies[TOKEN]?.let {
            try {
                JWT.decode(it)
            } catch (e: JWTDecodeException) {
                context.call.response.cookies.append(TOKEN, "", expires = GMTDate.START)
                throw AuthException(cause = e)
            }
        }?.let {
            try {
                JWT.require(algorithm)
                    .withIssuer(APP_NAME)
                    .build()
                    .verify(it)
            } catch (e: JWTVerificationException) {
                context.call.response.cookies.append(TOKEN, "", expires = GMTDate.START)
                throw AuthException(cause = e)
            }
        }?.let {
            try {
                context.principal(AccountPrincipal(it.subject.toInt(), it.getClaim("username").asString()))
            } catch (e: Exception) {
                context.call.response.cookies.append(TOKEN, "", expires = GMTDate.START)
                throw AuthException(cause = e)
            }
        } ?: if (required) {
            throw AuthException()
        } else {
        }
    }

    fun ApplicationCall.setTokenCookie(id: Int, username: String) {
        response.cookies.append(
            name = TOKEN,
            value = JWT.create()
                .withIssuer(APP_NAME)
                .withSubject(id.toString())
                .withClaim("username", username)
                .sign(algorithm),
            path = "/",
            expires = GMTDate() + 24.hours,
            secure = Conf.profile == Conf.Profile.PROD,
            httpOnly = true,
            extensions = mapOf(SAMESITE to SameSite.Lax.name),
        )
    }
}
