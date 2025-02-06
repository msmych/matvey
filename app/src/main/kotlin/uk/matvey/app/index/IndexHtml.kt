package uk.matvey.app.index

import kotlinx.html.HTML
import kotlinx.html.HtmlBlockTag
import kotlinx.html.body
import kotlinx.html.head
import kotlinx.html.header
import kotlinx.html.img
import kotlinx.html.lang
import kotlinx.html.link
import kotlinx.html.script
import kotlinx.html.style
import kotlinx.html.title
import uk.matvey.app.Conf
import uk.matvey.app.auth.AccountPrincipal
import uk.matvey.app.html.CommonHtml.col
import uk.matvey.app.index.MenuHtml.MenuTab
import uk.matvey.app.index.MenuHtml.menu
import uk.matvey.pauk.html.HtmlKit.Viewport.Companion.viewport
import uk.matvey.pauk.html.HtmlKit.stylesheet
import uk.matvey.pauk.ktor.KtorHtmx.htmxScript

object IndexHtml {

    fun HTML.page(principal: AccountPrincipal?, activeTab: MenuTab, block: HtmlBlockTag.() -> Unit) {
        lang = "en"
        pageHead()
        pageBody(principal, activeTab) {
            block()
        }
    }

    private fun HTML.pageHead() = head {
        title("Matvey")
        viewport()
        script {
            src = "/assets/script.js"
        }
        htmxScript()
        stylesheet("/styles.css")
        link {
            rel = "apple-touch-icon"
            sizes = "180x180"
            href = "${Conf.app.assetsUrl}/favicons/apple-touch-icon.png"
        }
        link {
            rel = "icon"
            type = "image/png"
            sizes = "32x32"
            href = "${Conf.app.assetsUrl}/favicons/favicon-32x32.png"
        }
        link {
            rel = "icon"
            type = "image/png"
            sizes = "16x16"
            href = "${Conf.app.assetsUrl}/favicons/favicon-16x16.png"
        }
        link {
            rel = "manifest"
            href = "${Conf.app.assetsUrl}/favicons/site.webmanifest"
        }
    }

    fun HTML.pageBody(
        principal: AccountPrincipal?,
        activeTab: MenuTab,
        block: HtmlBlockTag.() -> Unit
    ) = body {
        col(16) {
            pageHeader()
            col(32) {
                style = """
                        margin: 0 auto 0;
                        width: 80%;
                        min-width: 320px;
                        """.trimIndent()
                menu(principal, activeTab)
                block()
            }
        }
    }

    private fun HtmlBlockTag.pageHeader() = header {
        img {
            width = "100%"
            src = "https://live.staticflickr.com/961/41778488722_4612755202_k.jpg"
        }
    }
}