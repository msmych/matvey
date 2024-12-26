package uk.matvey.app.auth

import java.util.UUID

data class AccountPrincipal(
    val id: UUID,
    val username: String
)
