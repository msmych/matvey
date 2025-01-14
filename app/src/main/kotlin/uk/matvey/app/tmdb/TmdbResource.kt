package uk.matvey.app.tmdb

import com.github.jasync.sql.db.pool.ConnectionPool
import com.github.jasync.sql.db.postgresql.PostgreSQLConnection
import io.ktor.server.html.respondHtml
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import kotlinx.html.body
import uk.matvey.app.auth.AuthJwt.Required.accountPrincipal
import uk.matvey.app.auth.AuthJwt.Required.authJwtRequired
import uk.matvey.app.movie.AccountMovie
import uk.matvey.app.movie.AccountMovieSql.getAccountMovies
import uk.matvey.app.movie.Movie
import uk.matvey.app.tmdb.TmdbHtml.movieSearchResults
import uk.matvey.pauk.ktor.KtorKit.pathParam
import uk.matvey.pauk.ktor.KtorKit.queryParam
import uk.matvey.pauk.ktor.Resource
import uk.matvey.tmdb.TmdbClient

class TmdbResource(
    private val tmdbClient: TmdbClient,
    private val pool: ConnectionPool<PostgreSQLConnection>,
) : Resource {

    override fun Route.routing() {
        authJwtRequired {
            route("/falafel/tmdb") {
                route("/movies") {
                    route("/search") {
                        searchMovie()
                    }
                    route("/{id}") {
                        getMovieDetails()
                    }
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
                    movie.directors().takeIf { it.isNotEmpty() }?.let { directors ->
                        +"by "
                        +directors.joinToString { it.name }
                    }
                }
            }
        }
    }

    private fun Route.searchMovie() {
        get {
            val principal = call.accountPrincipal()
            val query = call.queryParam("q")
            val moviesResponse = tmdbClient.searchMovie(query)
            val movies = moviesResponse.results.take(5)
            val accountMovies = pool.getAccountMovies(principal.id)
                .associateBy { it.movie.id }
            call.respondHtml {
                body {
                    movieSearchResults(movies.map {
                        accountMovies[it.id]
                            ?: AccountMovie(Movie.from(it), false, false)
                    }, emptyMap())
                }
            }
        }
    }
}