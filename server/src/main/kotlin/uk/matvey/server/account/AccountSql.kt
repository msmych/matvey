package uk.matvey.server.account

import com.github.jasync.sql.db.Connection
import com.github.jasync.sql.db.RowData
import kotlinx.coroutines.future.await
import uk.matvey.kit.time.TimeKit.instant
import java.util.UUID

object AccountSql {

    const val ACCOUNTS = "accounts"
    const val ID = "id"
    const val USERNAME = "username"
    const val STATE = "state"
    const val TAGS = "tags"
    const val PASS_HASH = "pass_hash"
    const val EMAIL = "email"
    const val CREATED_AT = "created_at"
    const val UPDATED_AT = "updated_at"

    suspend fun Connection.getAccount(id: UUID): Account {
        return sendQuery(
            """
                    select * from $ACCOUNTS
                    where $ID = '$id'
                """.trimIndent(),
        )
            .await()
            .rows
            .single()
            .toAccount()
    }

    suspend fun Connection.findAccount(username: String): Account? {
        val sendQuery = sendQuery(
            """
                select * from $ACCOUNTS
                where $USERNAME = '$username'
            """.trimIndent(),
        )
        return sendQuery
            .await()
            .rows
            .singleOrNull()
            ?.toAccount()
    }

    suspend fun Connection.addAccount(username: String, passHash: String): Account {
        return sendPreparedStatement(
            """
                insert into $ACCOUNTS ($USERNAME, $STATE, $PASS_HASH, $CREATED_AT)
                values (?, ?, ?, ?)
                returning *
            """.trimIndent(),
            listOf(username, Account.State.PENDING.name, passHash, instant().toString()),
        )
            .await()
            .rows
            .single()
            .toAccount()
    }

    suspend fun Connection.updateUsername(id: UUID, username: String) {
        sendPreparedStatement(
            """
                update $ACCOUNTS
                set $USERNAME = ?, $UPDATED_AT = ?
                where $ID = ?
            """.trimIndent(),
            listOf(username, instant().toString(), id),
        )
            .await()
    }

    suspend fun Connection.updatePassHash(id: UUID, passHash: String) {
        sendPreparedStatement(
            """
                update $ACCOUNTS
                set $PASS_HASH = ?, $UPDATED_AT = ?
                where $ID = ?
            """.trimIndent(),
            listOf(passHash, instant().toString(), id),
        )
            .await()
    }

    fun RowData.toAccount(): Account {
        return Account(
            id = getAs(ID),
            username = getAs(USERNAME),
            state = Account.State.valueOf(requireNotNull(getString(STATE))),
            tags = getAs<List<String>>(TAGS).map(Account.Tag::valueOf),
            passHash = getAs(PASS_HASH),
        )
    }
}