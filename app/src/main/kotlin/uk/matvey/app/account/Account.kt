package uk.matvey.app.account

data class Account(
    val id: Int,
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
