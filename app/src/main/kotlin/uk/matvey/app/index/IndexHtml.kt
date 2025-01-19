package uk.matvey.app.index

import kotlinx.html.HTML
import kotlinx.html.HtmlBlockTag
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.head
import kotlinx.html.id
import kotlinx.html.img
import kotlinx.html.lang
import kotlinx.html.link
import kotlinx.html.meta
import kotlinx.html.script
import kotlinx.html.style
import kotlinx.html.title
import uk.matvey.app.Conf
import uk.matvey.app.auth.AccountPrincipal
import uk.matvey.app.html.CommonHtml.col
import uk.matvey.app.index.MenuHtml.menu
import uk.matvey.pauk.html.HtmlKit.stylesheet
import uk.matvey.pauk.ktor.KtorHtmx.htmxScript
import uk.matvey.pauk.ktor.KtorHtmx.hxGet
import uk.matvey.pauk.ktor.KtorHtmx.hxTrigger

object IndexHtml {

    fun HTML.index(principal: AccountPrincipal?, loadPage: String? = null) {
        lang = "en"
        head()
        body(principal, loadPage)
    }

    private fun HTML.head() = head {
        title("Matvey")
        meta {
            name = "viewport"
            content = "width=device-width, initial-scale=1"
        }
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

    private fun HTML.body(principal: AccountPrincipal?, loadPage: String? = null) = body {
        col(16) {
            div {
                style = """
                        |height: 64px;
                        |overflow: hidden;
                        |position: relative;
                        |""".trimMargin()
                img {
                    width = "100%"
                    style = """
                            |position: absolute;
                            |transform: rotate(180deg);
                            |filter: invert(0);
                            |""".trimMargin()
                    src = "https://live.staticflickr.com/961/41778488722_4612755202_k.jpg"
                }
            }
            col(32) {
                style = """
                        margin: 0 auto 0;
                        width: 80%;
                        min-width: 320px;
                        """.trimIndent()
                menu(principal)
                div {
                    id = "content"
                    if (loadPage != null) {
                        hxGet(loadPage)
                        hxTrigger("load delay:10ms")
                    } else {
                        home()
                    }
                }
            }
        }
    }

    fun HtmlBlockTag.home() = col(gap = 8, classes = "card") {
        div {
            +"Hi, my name is Matvey"
        }
        div {
            +"I live in London and work as a "
            a(href = "https://www.linkedin.com/in/matvey-smychkov-743b21175/") {
                +"software engineer"
            }
        }
        div {
            +"I do some coding in my spare time as well. Currently, I'm working on several open-source Kotlin libraries:"
        }
        div {
            +"* "
            a(href = "https://github.com/msmych/telek") {
                +"Telek"
            }
            +": Kotlin Telegram Bot API client"
        }
        div {
            +"* "
            a(href = "https://github.com/msmych/kit") {
                +"Kit"
            }
            +": misc Kotlin utilities I find useful as an extension of standard library"
        }
        div {
            +"* "
            a(href = "https://github.com/msmych/pauk") {
                +"Pauk"
            }
            +": Kotlin HTTP utilities"
        }
        div {
            +"More stuff is on the way. in the meantime, you can check out my "
            a(href = "https://x.com/matvey_uk") {
                +"X account"
            }
        }
    }
}