package uk.matvey.app.vtornik

import kotlinx.html.HtmlBlockTag
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.id
import kotlinx.html.label
import kotlinx.html.radioInput
import kotlinx.html.textInput
import uk.matvey.app.auth.AccountPrincipal
import uk.matvey.app.html.CommonHtml.col
import uk.matvey.app.html.CommonHtml.row
import uk.matvey.pauk.ktor.KtorHtmx.hxGet
import uk.matvey.pauk.ktor.KtorHtmx.hxPushUrl
import uk.matvey.pauk.ktor.KtorHtmx.hxTarget
import uk.matvey.pauk.ktor.KtorHtmx.hxTrigger

object VtornikHtml {

    fun HtmlBlockTag.vtornik(principal: AccountPrincipal?) {
        col(16) {
            form {
                hxGet("/vtornik/movies/search")
                hxPushUrl()
                hxTarget("#search-result")
                principal?.let {
                    hxTrigger("submit, load, change")
                } ?: hxTrigger("submit, change")
                row(8) {
                    principal?.let {
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