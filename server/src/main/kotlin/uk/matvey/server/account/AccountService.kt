package uk.matvey.server.account

import uk.matvey.pauk.exception.AuthException
import uk.matvey.server.account.AccountSql.addAccount
import uk.matvey.server.account.AccountSql.findAccount
import uk.matvey.server.account.AccountSql.getAccount
import uk.matvey.server.account.AccountSql.updatePassHash
import uk.matvey.server.crypto.CryptoService
import uk.matvey.slon.repo.Repo
import java.util.UUID

class AccountService(private val repo: Repo, private val cryptoService: CryptoService) {

    fun ensureAccount(username: String, password: String): Account {
        return repo.findAccount(username)?.let {
            verifyPassword(it, password)
            it
        } ?: repo.addAccount(username, cryptoService.hashPassword(password))
    }

    fun updatePassword(accountId: UUID, currentPassword: String, newPassword: String) {
        val account = repo.getAccount(accountId)
        account.passHash?.let {
            verifyPassword(account, currentPassword)
        }
        repo.updatePassHash(accountId, cryptoService.hashPassword(newPassword))
    }

    private fun verifyPassword(account: Account, password: String) {
        account.passHash?.let { passHash ->
            if (!cryptoService.verifyPassword(password, passHash)) {
                throw AuthException("Invalid password")
            }
        }
    }
}