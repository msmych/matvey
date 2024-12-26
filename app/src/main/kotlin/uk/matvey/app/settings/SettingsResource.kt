package uk.matvey.app.settings

import com.github.jasync.sql.db.pool.ConnectionPool
import com.github.jasync.sql.db.postgresql.PostgreSQLConnection
import io.ktor.server.html.respondHtml
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.route
import kotlinx.html.body
import uk.matvey.pauk.ktor.KtorKit.receiveParamsMap
import uk.matvey.pauk.ktor.Resource
import uk.matvey.app.account.AccountService
import uk.matvey.app.account.AccountSql.updateUsername
import uk.matvey.app.auth.AuthJwt.Required.accountPrincipal
import uk.matvey.app.auth.AuthJwt.Required.authJwtRequired
import uk.matvey.app.auth.AuthJwt.setTokenCookie
import uk.matvey.app.index.IndexHtml.settingsMenuItem
import uk.matvey.app.index.getLoad
import uk.matvey.app.settings.SettingsHtml.password
import uk.matvey.app.settings.SettingsHtml.passwordEdit
import uk.matvey.app.settings.SettingsHtml.settings
import uk.matvey.app.settings.SettingsHtml.username
import uk.matvey.app.settings.SettingsHtml.usernameEdit

class SettingsResource(
    private val accountService: AccountService,
    private val pool: ConnectionPool<PostgreSQLConnection>
) : Resource {

    override fun Route.routing() {
        authJwtRequired {
            route("/settings") {
                getLoad("/settings") {
                    val principal = call.accountPrincipal()
                    call.respondHtml {
                        body {
                            settings(principal)
                        }
                    }
                }
                route("/username") {
                    get {
                        val principal = call.accountPrincipal()
                        call.respondHtml {
                            body {
                                username(principal.username)
                            }
                        }
                    }
                    patch {
                        val principal = call.accountPrincipal()
                        val params = call.receiveParamsMap()
                        val username = params.getValue("username")
                        pool.updateUsername(principal.id, username)
                        call.setTokenCookie(principal.id, username)
                        call.respondHtml {
                            body {
                                username(username)
                                settingsMenuItem(username, oob = true)
                            }
                        }
                    }
                }
                route("/username-edit") {
                    get {
                        call.respondHtml {
                            body {
                                usernameEdit()
                            }
                        }
                    }
                }
                route("/password") {
                    get {
                        call.respondHtml {
                            body {
                                password()
                            }
                        }
                    }
                    patch {
                        val principal = call.accountPrincipal()
                        val params = call.receiveParamsMap()
                        accountService.updatePassword(
                            principal.id,
                            params.getValue("currentPassword"),
                            params.getValue("newPassword")
                        )
                        call.respondHtml {
                            body {
                                password()
                            }
                        }
                    }
                }
                route("/password-edit") {
                    get {
                        call.respondHtml {
                            body {
                                passwordEdit()
                            }
                        }
                    }
                }
            }
        }
    }
}