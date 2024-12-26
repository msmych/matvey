package uk.matvey.app.db

import mu.KotlinLogging
import org.flywaydb.core.Flyway
import uk.matvey.app.Conf
import uk.matvey.app.Conf.Profile

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