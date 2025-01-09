package uk.matvey.app.tmdb

import kotlinx.html.HtmlBlockTag
import kotlinx.html.b
import kotlinx.html.div
import kotlinx.html.i
import uk.matvey.app.html.CommonHtml.t3
import uk.matvey.app.html.CommonHtml.vertical
import uk.matvey.pauk.ktor.KtorHtmx.hxGet
import uk.matvey.pauk.ktor.KtorHtmx.hxTrigger
import uk.matvey.tmdb.SearchMovieResponse

object TmdbHtml {

    fun HtmlBlockTag.tmdbSearchResult(movies: List<SearchMovieResponse.Movie>) {
        vertical(16) {
            t3("Search results:")
            vertical(16) {
                movies.forEach { movie ->
                    div {
                        b {
                            +movie.title
                        }
                        movie.releaseYear()?.let { year -> +" ($year)" }
                        movie.originalTitle?.takeUnless { it == movie.title }?.let { originalTitle ->
                            div {
                                i {
                                    +originalTitle
                                }
                            }
                        }
                        div {
                            hxGet("/falafel/tmdb/movies/${movie.id}/details")
                            hxTrigger("load")
                        }
                    }
                }
            }
        }
    }
}