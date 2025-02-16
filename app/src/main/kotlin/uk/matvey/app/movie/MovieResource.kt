package uk.matvey.app.movie

import com.github.jasync.sql.db.pool.ConnectionPool
import com.github.jasync.sql.db.postgresql.PostgreSQLConnection
import io.ktor.server.html.respondHtml
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.i
import kotlinx.html.p
import uk.matvey.app.auth.AuthJwt.Optional.authJwtOptional
import uk.matvey.app.auth.AuthJwt.Required.accountPrincipalOrNull
import uk.matvey.app.html.CommonHtml.col
import uk.matvey.app.index.IndexHtml.page
import uk.matvey.app.index.MenuHtml
import uk.matvey.pauk.ktor.KtorKit.pathParam
import uk.matvey.pauk.ktor.Resource
import uk.matvey.tmdb.TmdbClient
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class MovieResource(
    private val tmdbClient: TmdbClient,
    private val pool: ConnectionPool<PostgreSQLConnection>,
) : Resource {

    override fun Route.routing() {
        authJwtOptional {
            route("/vtornik") {
                route("/movies") {
                    route("/{id}") {
                        getMovie()
                        route("/directors") {
                            getMovieDirectors()
                        }
                    }
                }
            }
        }
    }

    private fun Route.getMovie() {
        get {
            val principal = call.accountPrincipalOrNull()
            val movieId = call.pathParam("id").toInt()
            val details = tmdbClient.getMovieDetails(movieId, listOf("credits"))
            call.respondHtml {
                page(principal, MenuHtml.MenuTab.VTORNIK) {
                    col(gap = 8) {
                        col {
                            h1 {
                                +details.movieDetails.title
                            }
                            details.movieDetails.originalTitle?.takeIf { it != details.movieDetails.title }?.let {
                                i {
                                    +it
                                }
                            }
                            details.crew?.filter { it.isDirector() }?.takeIf { it.isNotEmpty() }?.let { directors ->
                                +"Directed by "
                                +directors.joinToString { it.name }
                            }
                            details.movieDetails.releaseDate?.let { releaseDate ->
                                i {
                                    +"Released on "
                                    +LocalDate.parse(releaseDate)
                                        .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))
                                }
                            }
                        }
                        col {
                            details.movieDetails.overview?.let {
                                p {
                                    +it
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun Route.getMovieDirectors() {
        get {
            val id = call.pathParam("id").toInt()
            val movie = tmdbClient.getCredits(id)
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
}