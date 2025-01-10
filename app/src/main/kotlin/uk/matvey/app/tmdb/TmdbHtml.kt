package uk.matvey.app.tmdb

import kotlinx.html.ButtonType
import kotlinx.html.HtmlBlockTag
import kotlinx.html.InputType
import kotlinx.html.b
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.i
import kotlinx.html.img
import kotlinx.html.input
import uk.matvey.app.html.CommonHtml.horizontal
import uk.matvey.app.html.CommonHtml.t3
import uk.matvey.app.html.CommonHtml.vertical
import uk.matvey.pauk.ktor.KtorHtmx.hxGet
import uk.matvey.pauk.ktor.KtorHtmx.hxPost
import uk.matvey.pauk.ktor.KtorHtmx.hxTrigger
import uk.matvey.tmdb.SearchMovieResponse

object TmdbHtml {

    fun HtmlBlockTag.tmdbSearchResult(movies: List<SearchMovieResponse.Movie>) {
        vertical(16) {
            t3("Search results:")
            vertical(16) {
                movies.forEach { movie ->
                    horizontal(8) {
                        img {
                            src = "https://image.tmdb.org/t/p/w440_and_h660_face${movie.posterPath}"
                            alt = "${movie.title} poster"
                            height = "64"
                        }
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
                        form {
                            hxPost("/falafel/titles")
                            input {
                                type = InputType.hidden
                                name = "tmdbId"
                                value = movie.id.toString()
                            }
                            button {
                                type = ButtonType.submit
                                +"Import"
                            }
                        }
                    }
                }
            }
        }
    }
}