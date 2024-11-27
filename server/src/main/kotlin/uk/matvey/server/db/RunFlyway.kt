package uk.matvey.server.db

import mu.KotlinLogging
import org.flywaydb.core.Flyway
import uk.matvey.server.Conf
import uk.matvey.server.Conf.Profile

private val log = KotlinLogging.logger {}

fun runFlyway() {
    log.info { "Running Flyway" }
    val flyway = Flyway.configure()
        .dataSource(Conf.db.jdbcUrl, Conf.db.username, Conf.db.password)
        .cleanDisabled(Conf.profile != Profile.TEST)
        .load()
    if (Conf.profile == Profile.TEST) {
        log.info { "Cleaning database" }
        flyway.clean()
    }
    log.info { "Applying database migrations" }
    flyway.migrate()
}