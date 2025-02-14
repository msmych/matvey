package uk.matvey.app.vtornik

import com.github.jasync.sql.db.pool.ConnectionPool
import com.github.jasync.sql.db.postgresql.PostgreSQLConnection
import io.ktor.server.html.respondHtml
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.route
import kotlinx.html.body
import uk.matvey.app.auth.AuthJwt.Optional.authJwtOptional
import uk.matvey.app.auth.AuthJwt.Required.accountPrincipal
import uk.matvey.app.auth.AuthJwt.Required.accountPrincipalOrNull
import uk.matvey.app.director.DirectorSql.addDirector
import uk.matvey.app.director.DirectorSql.getDirectors
import uk.matvey.app.index.IndexHtml.page
import uk.matvey.app.index.MenuHtml.MenuTab
import uk.matvey.app.movie.AccountMovie
import uk.matvey.app.movie.AccountMovieSql.getAccountMovie
import uk.matvey.app.movie.AccountMovieSql.getAccountMovies
import uk.matvey.app.movie.AccountMovieSql.updateAccountMovieToWatch
import uk.matvey.app.movie.AccountMovieSql.updateAccountMovieWatched
import uk.matvey.app.movie.Movie
import uk.matvey.app.movie.MovieSql.addMovie
import uk.matvey.app.movie.MovieSql.findMovie
import uk.matvey.app.tmdb.TmdbHtml.movieSearchResults
import uk.matvey.app.tmdb.TmdbHtml.toggleToWatch
import uk.matvey.app.tmdb.TmdbHtml.toggleWatched
import uk.matvey.app.vtornik.VtornikHtml.vtornik
import uk.matvey.pauk.ktor.KtorKit.pathParam
import uk.matvey.pauk.ktor.KtorKit.queryParam
import uk.matvey.pauk.ktor.KtorKit.queryParamOrNull
import uk.matvey.pauk.ktor.KtorKit.receiveParamsMap
import uk.matvey.pauk.ktor.Resource
import uk.matvey.tmdb.TmdbClient
import java.util.concurrent.CompletableFuture

class VtornikResource(
    private val tmdbClient: TmdbClient,
    private val pool: ConnectionPool<PostgreSQLConnection>
) : Resource {

    override fun Route.routing() {
        authJwtOptional {
            route("/vtornik") {
                getVtornikPage()
                route("/movies") {
                    route("/search") {
                        searchMovies()
                    }
                    route("/{id}") {
                        updateMovie()
                    }
                }
            }
        }
    }

    private fun Route.getVtornikPage() {
        get {
            val principal = call.accountPrincipalOrNull()
            call.respondHtml {
                page(principal, MenuTab.VTORNIK) {
                    vtornik(principal)
                }
            }
        }
    }

    private fun Route.searchMovies() {
        get {
            val query = call.queryParam("q")
            when (val filter = call.queryParamOrNull("filter")) {
                "TO_WATCH", "WATCHED" -> {
                    val principal = call.accountPrincipal()
                    val accountMovies = pool.getAccountMovies(principal.id, filter)
                    val directors = accountMovies.takeIf { it.isNotEmpty() }?.let {
                        pool.getDirectors(accountMovies.map { it.movie.directorsIds }.flatten().distinct())
                    } ?: emptyList()
                    call.respondHtml {
                        body {
                            movieSearchResults(accountMovies, directors.associateBy { it.id })
                        }
                    }
                }
                else -> {
                    val principal = call.accountPrincipalOrNull()
                    val moviesResponse = tmdbClient.searchMovie(query)
                    val movies = moviesResponse.results.take(5)
                    val accountMoviesById = principal?.let { p ->
                        pool.getAccountMovies(p.id, "NONE").associateBy { it.movie.id }
                    } ?: emptyMap()
                    call.respondHtml {
                        body {
                            movieSearchResults(movies.map {
                                accountMoviesById[it.id]
                                    ?: AccountMovie(Movie.from(it), false, false)
                            }, emptyMap())
                        }
                    }
                }
            }
        }
    }

    private fun Route.updateMovie() {
        patch {
            val principal = call.accountPrincipal()
            val id = call.pathParam("id").toInt()
            val tmdbMovieDetails = tmdbClient.getMovieDetails(id, listOf("credits"))
            val movie = pool.findMovie(id) ?: run {
                val directors = tmdbMovieDetails.crew?.filter { it.job == "Director" }
                directors?.forEach { director ->
                    pool.addDirector(director.id, director.name)
                }
                pool.addMovie(tmdbMovieDetails.movieDetails, directors?.map { it.id } ?: emptyList())
            }
            val params = call.receiveParamsMap()
            params["toWatch"]?.let { toWatch ->
                val accountMovie = pool.inTransaction {
                    it.updateAccountMovieToWatch(principal.id, movie.id, toWatch.toBoolean())
                    CompletableFuture.completedFuture(it.getAccountMovie(principal.id, movie.id))
                }
                    .join()
                call.respondHtml {
                    body {
                        params["toWatch"]?.let {
                            toggleToWatch(accountMovie)
                        }
                    }
                }
                return@patch
            }
            params["watched"]?.let { watched ->
                val accountMovie = pool.inTransaction {
                    it.updateAccountMovieWatched(principal.id, movie.id, watched.toBoolean())
                    CompletableFuture.completedFuture(it.getAccountMovie(principal.id, movie.id))
                }.join()
                call.respondHtml {
                    body {
                        params["watched"]?.let {
                            toggleWatched(accountMovie)
                        }
                    }
                }
                return@patch
            }
        }
    }
}