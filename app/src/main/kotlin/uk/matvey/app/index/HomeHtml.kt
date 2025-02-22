package uk.matvey.app.index

import kotlinx.html.HtmlBlockTag
import kotlinx.html.h3
import kotlinx.html.p
import uk.matvey.app.html.CommonHtml.col

object HomeHtml {

    fun HtmlBlockTag.homePage() = col(gap = 8) {
        p {
            +"Hi there"
        }
        p {
            +"My name is Matvey. On this website I'll be sharing posts and projects I'm working on"
        }
        h3 {
            +"Vtornik"
        }
        p {
            +"Vtornik is a simple movie library"
        }
    }
}