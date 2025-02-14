package uk.matvey.app.tmdb

import kotlinx.html.HtmlBlockTag
import kotlinx.html.a
import kotlinx.html.b
import kotlinx.html.checkBoxInput
import kotlinx.html.div
import kotlinx.html.h3
import kotlinx.html.i
import kotlinx.html.img
import kotlinx.html.label
import kotlinx.html.style
import kotlinx.serialization.json.put
import uk.matvey.app.director.Director
import uk.matvey.app.html.CommonHtml.col
import uk.matvey.app.html.CommonHtml.row
import uk.matvey.app.movie.AccountMovie
import uk.matvey.pauk.htmx.Htmx.Swap.outerHTML
import uk.matvey.pauk.ktor.KtorHtmx.hxGet
import uk.matvey.pauk.ktor.KtorHtmx.hxPatch
import uk.matvey.pauk.ktor.KtorHtmx.hxPushUrl
import uk.matvey.pauk.ktor.KtorHtmx.hxSwap
import uk.matvey.pauk.ktor.KtorHtmx.hxTarget
import uk.matvey.pauk.ktor.KtorHtmx.hxTrigger
import uk.matvey.pauk.ktor.KtorHtmx.hxVals

object TmdbHtml {

    fun HtmlBlockTag.movieSearchResults(movies: List<AccountMovie>, directors: Map<Int, Director>) {
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
                            alt = "${movie.movie.title} poster"
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
                                    hxGet("/vtornik/tmdb/movies/${movie.movie.id}")
                                    hxTrigger("load")
                                }
                            }
                        }
                        col {
                            toggleToWatch(movie)
                            toggleWatched(movie)
                        }
                    }
                }
            }
        }
    }

    fun HtmlBlockTag.toggleToWatch(movie: AccountMovie) {
        label {
            checkBoxInput {
                hxPatch("/vtornik/movies/${movie.movie.id}")
                hxSwap(outerHTML)
                hxTarget("closest label")
                hxTrigger("change")
                hxVals {
                    put("toWatch", (!movie.toWatch).toString())
                }
                name = "toWatch"
                checked = movie.toWatch
            }
            +" to watch"
        }
    }

    fun HtmlBlockTag.toggleWatched(movie: AccountMovie) {
        label {
            checkBoxInput {
                hxPatch("/vtornik/movies/${movie.movie.id}")
                hxSwap(outerHTML)
                hxTarget("closest label")
                hxTrigger("change")
                hxVals {
                    put("watched", (!movie.watched).toString())
                }
                name = "watched"
                checked = movie.watched
            }
            +" watched"
        }
    }
}