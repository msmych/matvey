package uk.matvey.app

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.Year
import java.util.UUID

data class Title(
    val id : UUID,
    val type : Type,
    val title: String,
    val details: Details,
    val createdAt: Instant,
    val updatedAt: Instant,
) {

    enum class Type {
        MOVIE,
    }

    @Serializable
    data class Details(
        val tmdbId: Int? = null,
        val tmdbPosterPath: String? = null,
        val releaseYear : @Contextual Year? = null,
        val originalTitle: String? = null,
    )
}