package uk.matvey.app.tmdb

import com.github.jasync.sql.db.pool.ConnectionPool
import com.github.jasync.sql.db.postgresql.PostgreSQLConnection
import io.ktor.server.html.respondHtml
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.html.body
import kotlinx.html.div
import uk.matvey.app.title.TitleSql.addTitle
import uk.matvey.app.tmdb.TmdbHtml.tmdbSearchResult
import uk.matvey.pauk.ktor.KtorKit.pathParam
import uk.matvey.pauk.ktor.KtorKit.queryParam
import uk.matvey.pauk.ktor.Resource
import uk.matvey.tmdb.TmdbClient

class TmdbResource(
    private val tmdbClient: TmdbClient,
    private val pool: ConnectionPool<PostgreSQLConnection>,
) : Resource {

    override fun Route.routing() {
        route("/falafel/tmdb") {
            route("/movies") {
                route("/search") {
                    searchMovie()
                }
                route("/{id}") {
                    getMovieDetails()
                    importMovie()
                }
            }
        }
    }

    private fun Route.getMovieDetails() {
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

    private fun Route.importMovie() {
        post {
            val details = tmdbClient.getMovieDetails(call.pathParam("id").toInt(), listOf("credits"))
            pool.addTitle(details.movieDetails, details.credits)
            call.respondHtml {
                body {
                    +"Done"
                }
            }
        }
    }
}