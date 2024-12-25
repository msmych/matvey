package uk.matvey.server

import uk.matvey.server.db.runFlyway
import uk.matvey.server.ktor.ktorServer

fun main() {
    runFlyway()
    val services = Services()
    ktorServer(services).start(true)
}