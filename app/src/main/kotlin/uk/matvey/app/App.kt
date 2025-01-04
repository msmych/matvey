package uk.matvey.app

import uk.matvey.app.db.runFlyway
import uk.matvey.app.ktor.ktorServer
import uk.matvey.tmdb.TmdbClient

fun main() {
    runFlyway()
    val services = Services()
    val tmdbClient = TmdbClient(Conf.tmdb.token)
    ktorServer(services, tmdbClient).start(true)
}