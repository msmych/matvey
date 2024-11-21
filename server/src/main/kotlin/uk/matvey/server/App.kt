package uk.matvey.server

import uk.matvey.server.ktor.ktorServer

fun main() {
    ktorServer().start(true)
}