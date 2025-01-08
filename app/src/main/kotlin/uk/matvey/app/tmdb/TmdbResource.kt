package uk.matvey.app.tmdb

import io.ktor.server.html.respondHtml
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import kotlinx.html.body
import kotlinx.html.div
import uk.matvey.app.html.CommonHtml.t3
import uk.matvey.app.html.CommonHtml.vertical
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
        }
    }

    private fun Route.searchMovie() {
        get {
            val query = call.queryParam("q")
            val movies = tmdbClient.searchMovie(query)
            call.respondHtml {
                body {
                    vertical(16) {
                        t3("Search results:")
                        vertical(16) {
                            movies.results.take(10).forEach {
                                div {
                                    +it.title
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}