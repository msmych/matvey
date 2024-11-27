package uk.matvey.server.account

import java.util.UUID

data class Account(
    val id: UUID,
    val username: String,
    val state: State,
    val tags: List<Tag>,
    val passHash: String?,
) {

    enum class State {
        ACTIVE,
        PENDING,
        DELETED,
    }

    enum class Tag {
        ADMIN,
    }
}
