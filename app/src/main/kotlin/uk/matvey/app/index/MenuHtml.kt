package uk.matvey.app.index

import kotlinx.html.HtmlBlockTag
import kotlinx.html.b
import kotlinx.html.button
import kotlinx.html.id
import kotlinx.html.style
import uk.matvey.app.Conf
import uk.matvey.app.auth.AccountPrincipal
import uk.matvey.app.html.CommonHtml.row
import uk.matvey.pauk.ktor.KtorHtmx.hxGet
import uk.matvey.pauk.ktor.KtorHtmx.hxPushUrl
import uk.matvey.pauk.ktor.KtorHtmx.hxSwapOob
import uk.matvey.pauk.ktor.KtorHtmx.hxTarget

object MenuHtml {

    fun HtmlBlockTag.menu(principal: AccountPrincipal?) = row(8) {
        style = """ justify-content: space-between; """.trimIndent()
        row(8) {
            homeTab(true)
            if (Conf.profile != Conf.Profile.PROD) {
                falafelTab(false)
            }
        }
        if (Conf.profile != Conf.Profile.PROD) {
            if (principal != null) {
                settingsTab(principal.username, false, false)
            } else {
                loginTab(false)
            }
        }
    }

    fun HtmlBlockTag.homeTab(active: Boolean) = button {
        hxGet("/")
        hxTarget("body")
        hxPushUrl()
        tabLabel(HOME_LABEL, active)
    }

    fun HtmlBlockTag.falafelTab(active: Boolean) = button {
        hxGet("/falafel")
        hxTarget("#content")
        hxPushUrl()
        tabLabel(FALAFEL_LABEL, active)
    }

    fun HtmlBlockTag.settingsTab(username: String, active: Boolean, oob: Boolean) = button {
        id = "menu-item-settings"
        hxGet("/settings")
        hxTarget("#content")
        hxPushUrl()
        if (oob) {
            hxSwapOob()
        }
        tabLabel("👤 $username", active)
    }

    private fun HtmlBlockTag.loginTab(active: Boolean) = button {
        hxGet("/auth")
        hxTarget("#content")
        hxPushUrl()
        tabLabel("👤 Login", active)
    }

    private fun HtmlBlockTag.tabLabel(label: String, active: Boolean) {
        if (active) {
            b {
                +label
            }
        } else {
            +label
        }
    }

    private const val HOME_LABEL = "🏠 Home"
    private const val FALAFEL_LABEL = "🍿 Falafel"
}