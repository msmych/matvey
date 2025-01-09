package uk.matvey.app.tmdb

import io.ktor.server.html.respondHtml
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import kotlinx.html.body
import kotlinx.html.div
import uk.matvey.app.tmdb.TmdbHtml.tmdbSearchResult
import uk.matvey.pauk.ktor.KtorKit.pathParam
import uk.matvey.pauk.ktor.KtorKit.queryParam
import uk.matvey.pauk.ktor.Resource
import uk.matvey.tmdb.TmdbClient

class TmdbResource(
    private val tmdbClient: TmdbClient,
) : Resource {

    override fun Route.routing() {
        route("/falafel/tmdb") {
            route("/search") {
                searchMovie()
            }
            route("/movies") {
                route("/{id}/details") {
                    get {
                        val id = call.pathParam("id").toInt()
                        val movie = tmdbClient.getMovieCredits(id)
                        call.respondHtml {
                            body {
                                div {
                                    +"Director: ${movie.directors().joinToString { it.name }}"
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun Route.searchMovie() {
        get {
            val query = call.queryParam("q")
            val moviesResponse = tmdbClient.searchMovie(query)
            val movies = moviesResponse.results.take(5)
            call.respondHtml {
                body {
                    tmdbSearchResult(movies)
                }
            }
        }
    }
}