package uk.matvey.server.account

import com.github.jasync.sql.db.pool.ConnectionPool
import com.github.jasync.sql.db.postgresql.PostgreSQLConnection
import uk.matvey.pauk.exception.AuthException
import uk.matvey.server.account.AccountSql.addAccount
import uk.matvey.server.account.AccountSql.findAccount
import uk.matvey.server.account.AccountSql.getAccount
import uk.matvey.server.account.AccountSql.updatePassHash
import uk.matvey.server.crypto.CryptoService
import java.util.UUID

class AccountService(private val pool: ConnectionPool<PostgreSQLConnection>, private val cryptoService: CryptoService) {

    suspend fun ensureAccount(username: String, password: String): Account {
        return pool.findAccount(username)?.let {
            verifyPassword(it, password)
            it
        } ?: pool.addAccount(username, cryptoService.hashPassword(password))
    }

    suspend fun updatePassword(accountId: UUID, currentPassword: String, newPassword: String) {
        val account = pool.getAccount(accountId)
        account.passHash?.let {
            verifyPassword(account, currentPassword)
        }
        pool.updatePassHash(accountId, cryptoService.hashPassword(newPassword))
    }

    private fun verifyPassword(account: Account, password: String) {
        account.passHash?.let { passHash ->
            if (!cryptoService.verifyPassword(password, passHash)) {
                throw AuthException("Invalid password")
            }
        }
    }
}