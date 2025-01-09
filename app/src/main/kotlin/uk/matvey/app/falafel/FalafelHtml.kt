package uk.matvey.app.falafel

import kotlinx.html.HtmlBlockTag
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.id
import uk.matvey.app.html.CommonHtml.inputGroup
import uk.matvey.app.html.CommonHtml.t1
import uk.matvey.app.html.CommonHtml.vertical
import uk.matvey.pauk.ktor.KtorHtmx.hxGet

object FalafelHtml {

    fun HtmlBlockTag.falafel() {
        vertical(16) {
            t1("Search TMDb movies")
            form {
                hxGet("/falafel/tmdb/search", target = "#tmdb-search-result")
                inputGroup(
                    name = "q",
                    label = "Query",
                    placeholder = "Search by title",
                    required = true,
                )
            }
            div {
                id = "tmdb-search-result"
            }
        }
    }
}