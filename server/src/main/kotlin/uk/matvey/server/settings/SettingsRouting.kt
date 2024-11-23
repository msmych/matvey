package uk.matvey.server.settings

import io.ktor.server.html.respondHtml
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.route
import kotlinx.html.body
import uk.matvey.pauk.ktor.KtorKit.receiveParamsMap
import uk.matvey.server.index.IndexHtml.settingsMenuItem
import uk.matvey.server.index.getLoad
import uk.matvey.server.login.AuthJwt.Required.accountPrincipal
import uk.matvey.server.login.AuthJwt.Required.authJwtRequired
import uk.matvey.server.settings.SettingsHtml.password
import uk.matvey.server.settings.SettingsHtml.passwordEdit
import uk.matvey.server.settings.SettingsHtml.settings
import uk.matvey.server.settings.SettingsHtml.username
import uk.matvey.server.settings.SettingsHtml.usernameEdit

fun Route.settingsRouting() {
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
                    val params = call.receiveParamsMap()
                    val username = params.getValue("username")
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