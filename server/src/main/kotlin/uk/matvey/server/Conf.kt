package uk.matvey.server

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import io.ktor.server.config.tryGetString
import mu.KotlinLogging

object Conf {

    const val APP_NAME = "matvey-app"

    private val log = KotlinLogging.logger {}

    val profile = System.getenv("PROFILE")?.uppercase()?.let(Profile::valueOf) ?: Profile.LOCAL

    init {
        log.info { "Profile: $profile" }
    }

    val config: Config = ConfigFactory.load("matvey.conf")
        .withFallback(ConfigFactory.load("matvey.$profile.conf".lowercase()))

    val server = ServerConfig(config.getConfig("server"))

    val db = DbConfig(config.getConfig("db"))

    enum class Profile {
        LOCAL, TEST, PROD
    }

    class ServerConfig(config: Config) : Config by config {

        val port = getInt("port")

        val jksPass = tryGetString("jksPass")

        fun jksPass() = requireNotNull(jksPass) {
            "JKS password is missing"
        }

        val jwtSecret = getString("jwtSecret")

        val assetsUrl = getString("assetsUrl")
    }

    class DbConfig(config: Config) : Config by config {

        val jdbcUrl = getString("jdbcUrl")

        val username = getString("username")

        val password = getString("password")

        fun hikariConfig(): HikariConfig {
            val config = HikariConfig()
            config.jdbcUrl = jdbcUrl
            config.username = username
            config.password = password
            return config
        }
    }
}