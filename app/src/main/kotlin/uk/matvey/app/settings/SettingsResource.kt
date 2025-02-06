package uk.matvey.app.settings

import com.github.jasync.sql.db.pool.ConnectionPool
import com.github.jasync.sql.db.postgresql.PostgreSQLConnection
import io.ktor.server.html.respondHtml
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.route
import kotlinx.html.body
import uk.matvey.app.account.AccountService
import uk.matvey.app.account.AccountSql.updateUsername
import uk.matvey.app.auth.AuthJwt.Required.accountPrincipal
import uk.matvey.app.auth.AuthJwt.Required.authJwtRequired
import uk.matvey.app.auth.AuthJwt.setTokenCookie
import uk.matvey.app.index.IndexHtml.page
import uk.matvey.app.index.MenuHtml.MenuTab
import uk.matvey.app.index.MenuHtml.settingsTab
import uk.matvey.app.settings.SettingsHtml.password
import uk.matvey.app.settings.SettingsHtml.passwordEdit
import uk.matvey.app.settings.SettingsHtml.settings
import uk.matvey.app.settings.SettingsHtml.username
import uk.matvey.app.settings.SettingsHtml.usernameEdit
import uk.matvey.pauk.ktor.KtorKit.receiveParamsMap
import uk.matvey.pauk.ktor.Resource

class SettingsResource(
    private val accountService: AccountService,
    private val pool: ConnectionPool<PostgreSQLConnection>
) : Resource {

    override fun Route.routing() {
        authJwtRequired {
            route("/settings") {
                getSettingsPage()
                route("/username") {
                    getUsername()
                    updateUsername()
                }
                route("/username-edit") {
                    getUsernameEdit()
                }
                route("/password") {
                    getPassword()
                    updatePassword()
                }
                route("/password-edit") {
                    getPasswordEdit()
                }
            }
        }
    }

    private fun Route.getPasswordEdit() {
        get {
            call.respondHtml {
                body {
                    passwordEdit()
                }
            }
        }
    }

    private fun Route.updatePassword() {
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

    private fun Route.getPassword() {
        get {
            call.respondHtml {
                body {
                    password()
                }
            }
        }
    }

    private fun Route.getUsernameEdit() {
        get {
            call.respondHtml {
                body {
                    usernameEdit()
                }
            }
        }
    }

    private fun Route.updateUsername() {
        patch {
            val principal = call.accountPrincipal()
            val params = call.receiveParamsMap()
            val username = params.getValue("username")
            pool.updateUsername(principal.id, username)
            call.setTokenCookie(principal.id, username)
            call.respondHtml {
                body {
                    username(username)
                    settingsTab(username, true, true)
                }
            }
        }
    }

    private fun Route.getUsername() {
        get {
            val principal = call.accountPrincipal()
            call.respondHtml {
                body {
                    username(principal.username)
                }
            }
        }
    }

    private fun Route.getSettingsPage() {
        get {
            val principal = call.accountPrincipal()
            call.respondHtml {
                page(principal, MenuTab.SETTINGS) {
                    settings(principal)
                }
            }
        }
    }
}