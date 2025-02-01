package uk.matvey.app.index

import kotlinx.html.HtmlBlockTag
import kotlinx.html.a
import kotlinx.html.p
import uk.matvey.app.html.CommonHtml.col

object HomeHtml {

    fun HtmlBlockTag.homePage() = col(gap = 8) {
        p {
            +"Hi, my name is Matvey"
        }
        p {
            +"I live in London and work as a "
            a(href = "https://www.linkedin.com/in/matvey-smychkov-743b21175/") {
                +"software engineer"
            }
        }
        p {
            +"I do some coding in my spare time as well. Currently, I'm working on several open-source Kotlin libraries:"
        }
        p {
            +"* "
            a(href = "https://github.com/msmych/telek") {
                +"Telek"
            }
            +": Kotlin Telegram Bot API client"
        }
        p {
            +"* "
            a(href = "https://github.com/msmych/kit") {
                +"Kit"
            }
            +": misc Kotlin utilities I find useful as an extension of standard library"
        }
        p {
            +"* "
            a(href = "https://github.com/msmych/pauk") {
                +"Pauk"
            }
            +": Kotlin HTTP utilities"
        }
        p {
            +"More stuff is on the way. in the meantime, you can check out my "
            a(href = "https://x.com/matvey_uk") {
                +"X account"
            }
        }
    }
}