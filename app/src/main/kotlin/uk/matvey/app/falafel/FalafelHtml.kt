package uk.matvey.app.falafel

import kotlinx.html.HtmlBlockTag
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.id
import kotlinx.html.label
import kotlinx.html.radioInput
import kotlinx.html.textInput
import uk.matvey.app.html.CommonHtml.col
import uk.matvey.app.html.CommonHtml.row
import uk.matvey.pauk.ktor.KtorHtmx.hxGet
import uk.matvey.pauk.ktor.KtorHtmx.hxTarget
import uk.matvey.pauk.ktor.KtorHtmx.hxTrigger

object FalafelHtml {

    fun HtmlBlockTag.falafel() {
        col(16) {
            form {
                hxGet("/falafel/movies/search")
                hxTarget("#search-result")
                hxTrigger("submit, load, change")
                row(8) {
                    label {
                        radioInput {
                            name = "filter"
                            value = "TO_WATCH"
                            checked = true
                        }
                        +" to watch"
                    }
                    label {
                        radioInput {
                            name = "filter"
                            value = "WATCHED"
                        }
                        +" watched"
                    }
                    radioInput {
                        id = "filter-none"
                        name = "filter"
                        value = "NONE"
                    }
                    label {
                        htmlFor = "filter-none"
                        +" search"
                    }
                    label {
                        textInput {
                            name = "q"
                            placeholder = "Search by title"
                        }
                    }
                }
            }
            div {
                id = "search-result"
            }
        }
    }
}