package uk.matvey.app.tmdb

import com.github.jasync.sql.db.pool.ConnectionPool
import com.github.jasync.sql.db.postgresql.PostgreSQLConnection
import io.ktor.server.html.respondHtml
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import kotlinx.html.body
import uk.matvey.app.auth.AuthJwt.Required.authJwtRequired
import uk.matvey.pauk.ktor.KtorKit.pathParam
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