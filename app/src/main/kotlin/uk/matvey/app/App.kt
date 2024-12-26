package uk.matvey.app

import uk.matvey.app.db.runFlyway
import uk.matvey.app.ktor.ktorServer

fun main() {
    runFlyway()
    val services = Services()
    ktorServer(services).start(true)
}