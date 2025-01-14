package uk.matvey.app.falafel

import kotlinx.html.HtmlBlockTag
import kotlinx.html.checkBoxInput
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h1
import kotlinx.html.id
import kotlinx.html.input
import kotlinx.html.label
import uk.matvey.app.html.CommonHtml.col
import uk.matvey.app.html.CommonHtml.row
import uk.matvey.pauk.ktor.KtorHtmx.hxGet
import uk.matvey.pauk.ktor.KtorHtmx.hxTrigger

object FalafelHtml {

    fun HtmlBlockTag.falafel() {
        col(16) {
            h1 {
                +"Falafel"
            }
            row(16) {
                form {
                    hxGet("/falafel/tmdb/movies/search", target = "#tmdb-search-result")
                    input {
                        name = "q"
                        placeholder = "Search by title"
                        required = true
                    }
                }
                col {
                    label {
                        checkBoxInput {
                            name = "toWatch"
                            value = "false"
                        }
                        +" to watch"
                    }
                    label {
                        checkBoxInput {
                            name = "watched"
                            value = "false"
                        }
                        +" watched"
                    }
                }
            }
            col {
                hxGet("/falafel/movies")
                hxTrigger("load")
            }
            div {
                id = "tmdb-search-result"
            }
        }
    }
}