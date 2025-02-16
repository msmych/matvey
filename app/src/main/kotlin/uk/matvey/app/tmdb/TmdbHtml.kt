package uk.matvey.app.tmdb

import kotlinx.html.HtmlBlockTag
import kotlinx.html.checkBoxInput
import kotlinx.html.label
import kotlinx.serialization.json.put
import uk.matvey.app.movie.AccountMovie
import uk.matvey.pauk.htmx.Htmx.Swap.outerHTML
import uk.matvey.pauk.ktor.KtorHtmx.hxPatch
import uk.matvey.pauk.ktor.KtorHtmx.hxSwap
import uk.matvey.pauk.ktor.KtorHtmx.hxTarget
import uk.matvey.pauk.ktor.KtorHtmx.hxTrigger
import uk.matvey.pauk.ktor.KtorHtmx.hxVals

object TmdbHtml {

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