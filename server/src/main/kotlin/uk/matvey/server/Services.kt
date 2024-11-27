package uk.matvey.server

import com.zaxxer.hikari.HikariDataSource
import uk.matvey.server.account.AccountService
import uk.matvey.server.crypto.CryptoService
import uk.matvey.slon.repo.Repo

class Services {

    val repo = Repo(HikariDataSource(Conf.db.hikariConfig()))

    val crypto = CryptoService()

    val accountService = AccountService(repo, crypto)
}