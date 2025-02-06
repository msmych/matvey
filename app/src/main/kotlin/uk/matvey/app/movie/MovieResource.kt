package uk.matvey.app.movie

import com.github.jasync.sql.db.pool.ConnectionPool
import com.github.jasync.sql.db.postgresql.PostgreSQLConnection
import io.ktor.server.html.respondHtml
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import kotlinx.html.h1
import kotlinx.html.p
import uk.matvey.app.auth.AuthJwt.Required.accountPrincipal
import uk.matvey.app.auth.AuthJwt.Required.authJwtRequired
import uk.matvey.app.index.IndexHtml.page
import uk.matvey.app.index.MenuHtml
import uk.matvey.pauk.ktor.KtorKit.pathParam
import uk.matvey.pauk.ktor.Resource
import uk.matvey.tmdb.TmdbClient
import java.time.LocalDate

class MovieResource(
    private val tmdbClient: TmdbClient,
    private val pool: ConnectionPool<PostgreSQLConnection>,
) : Resource {

    override fun Route.routing() {
        authJwtRequired {
            route("/falafel") {
                route("/movies") {
                    route("/{id}") {
                        getMovie()
                    }
                }
            }
        }
    }

    private fun Route.getMovie() {
        get {
            val principal = call.accountPrincipal()
            val movieId = call.pathParam("id").toInt()
            val details = tmdbClient.getMovieDetails(movieId)
            call.respondHtml {
                page(principal, MenuHtml.MenuTab.FALAFEL) {
                    h1 {
                        +details.movieDetails.title
                        details.movieDetails.releaseDate?.let { releaseDate ->
                            val year = LocalDate.parse(releaseDate).year
                            +" ($year)"
                        }
                    }
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