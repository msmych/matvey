package uk.matvey.server.auth

import java.util.UUID

data class AccountPrincipal(
    val id: UUID,
    val username: String
)
