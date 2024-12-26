package uk.matvey.server

import com.github.jasync.sql.db.postgresql.PostgreSQLConnectionBuilder.createConnectionPool
import uk.matvey.server.account.AccountService
import uk.matvey.server.crypto.CryptoService

class Services {

    val pool = createConnectionPool(Conf.db.jdbcUrl) {
        username = Conf.db.username
        password = Conf.db.password
    }

    val crypto = CryptoService()

    val accountService = AccountService(pool, crypto)
}