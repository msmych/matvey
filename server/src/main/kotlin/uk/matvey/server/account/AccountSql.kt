package uk.matvey.server.account

import uk.matvey.kit.time.TimeKit.instant
import uk.matvey.slon.RecordReader
import uk.matvey.slon.access.AccessKit.queryOne
import uk.matvey.slon.access.AccessKit.queryOneOrNull
import uk.matvey.slon.access.AccessKit.update
import uk.matvey.slon.query.InsertOneQueryBuilder.Companion.insertOneInto
import uk.matvey.slon.query.ReturningQuery.Companion.returning
import uk.matvey.slon.repo.Repo
import uk.matvey.slon.value.PgText.Companion.toPgText
import uk.matvey.slon.value.PgUuid.Companion.toPgUuid
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

    fun Repo.getAccount(id: UUID): Account {
        return access { a ->
            a.queryOne(
                """
                    select * from $ACCOUNTS
                    where $ID = ?
                """.trimIndent(),
                listOf(id.toPgUuid()),
                ::readAccount
            )
        }
    }

    fun Repo.findAccount(username: String): Account? {
        return access { a ->
            a.queryOneOrNull(
                """
                    select * from $ACCOUNTS
                    where $USERNAME = ?
                """.trimIndent(),
                listOf(username.toPgText()),
                ::readAccount
            )
        }
    }

    fun Repo.addAccount(username: String, passHash: String): Account {
        return access { a ->
            a.query(
                insertOneInto(ACCOUNTS) {
                    set(USERNAME, username)
                    set(STATE, Account.State.PENDING)
                    set(PASS_HASH, passHash)
                    set(CREATED_AT, instant())
                }.returning(::readAccount)
            )
        }.single()
    }

    fun Repo.updateUsername(id: UUID, username: String) {
        access { a ->
            a.update(ACCOUNTS) {
                set(USERNAME, username)
                set(UPDATED_AT, instant())
                where("$ID = ?", id.toPgUuid())
            }
        }
    }

    fun Repo.updatePassHash(id: UUID, passHash: String) {
        access { a ->
            a.update(ACCOUNTS) {
                set(PASS_HASH, passHash)
                set(UPDATED_AT, instant())
                where("$ID = ?", id.toPgUuid())
            }
        }
    }

    fun readAccount(reader: RecordReader): Account {
        return Account(
            id = reader.uuid(ID),
            username = reader.string(USERNAME),
            state = reader.enum(STATE),
            tags = reader.stringList(TAGS).map(Account.Tag::valueOf),
            passHash = reader.stringOrNull(PASS_HASH),
        )
    }
}