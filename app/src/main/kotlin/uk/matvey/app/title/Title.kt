package uk.matvey.app.title

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.LocalDate
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
        val tmdb: Tmdb? = null,
    ) {

        @Serializable
        data class Tmdb(
            val id: Int,
            val releaseDate : @Contextual LocalDate? = null,
            val originalTitle: String? = null,
            val posterPath: String? = null,
            val directorsNames: List<String>? = null,
        )
    }
}