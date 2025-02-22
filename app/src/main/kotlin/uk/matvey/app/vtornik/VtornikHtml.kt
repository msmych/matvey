package uk.matvey.app.vtornik

import kotlinx.html.ButtonType
import kotlinx.html.HtmlBlockTag
import kotlinx.html.a
import kotlinx.html.b
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h3
import kotlinx.html.i
import kotlinx.html.id
import kotlinx.html.img
import kotlinx.html.label
import kotlinx.html.radioInput
import kotlinx.html.style
import kotlinx.html.textInput
import uk.matvey.app.auth.AccountPrincipal
import uk.matvey.app.director.Director
import uk.matvey.app.html.CommonHtml.col
import uk.matvey.app.html.CommonHtml.row
import uk.matvey.app.movie.AccountMovie
import uk.matvey.app.tmdb.TmdbHtml.toggleToWatch
import uk.matvey.app.tmdb.TmdbHtml.toggleWatched
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
                            required = true
                            placeholder = "Search by title"
                        }
                    }
                    button {
                        type = ButtonType.submit
                        +"Search"
                    }
                }
            }
            div {
                id = "search-result"
            }
        }
    }

    fun HtmlBlockTag.movieSearchResults(
        movies: List<AccountMovie>,
        directors: Map<Int, Director>,
        principal: AccountPrincipal?,
    ) {
        col(16) {
            h3 {
                +"Search results:"
            }
            col(16) {
                movies.forEach { movie ->
                    row(8) {
                        img {
                            width = "44px"
                            height = "66px"
                            src = movie.movie.posterPath?.let {
                                "https://image.tmdb.org/t/p/w440_and_h660_face$it"
                            } ?: ""
                            alt = "Poster for ${movie.movie.title}"
                        }
                        div {
                            style = "flex: 1 1 0"
                            a {
                                href = "/vtornik/movies/${movie.movie.id}"
                                hxGet("/vtornik/movies/${movie.movie.id}")
                                hxTarget("body")
                                hxPushUrl()
                                b {
                                    +movie.movie.title
                                }
                                movie.movie.releaseDate?.year?.let { year -> +" ($year)" }
                            }
                            movie.movie.originalTitle?.takeUnless { it == movie.movie.title }?.let { originalTitle ->
                                div {
                                    i {
                                        +originalTitle
                                    }
                                }
                            }
                            div {
                                if (movie.movie.directorsIds.isNotEmpty() && movie.movie.directorsIds.all { it in directors }) {
                                    +"by ${movie.movie.directorsIds.joinToString { directors.getValue(it).name }}"
                                } else {
                                    hxGet("/vtornik/movies/${movie.movie.id}/directors")
                                    hxTrigger("load")
                                }
                            }
                        }
                        principal?.let {
                            col {
                                toggleToWatch(movie)
                                toggleWatched(movie)
                            }
                        }
                    }
                }
            }
        }
    }
}