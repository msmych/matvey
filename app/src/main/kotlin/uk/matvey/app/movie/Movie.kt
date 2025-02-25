package uk.matvey.app.movie

import uk.matvey.kit.time.TimeKit.instant
import java.time.Instant
import java.time.LocalDate

data class Movie(
    val id : Int,
    val title: String,
    val releaseDate: LocalDate?,
    val originalTitle: String?,
    val posterPath: String?,
    val directorsIds: List<Int>,
    val createdAt: Instant,
    val updatedAt: Instant,
) {

    companion object {

        fun from(tmdbMovie: uk.matvey.tmdb.Movie): Movie {
            return Movie(
                tmdbMovie.id,
                tmdbMovie.title,
                tmdbMovie.releaseDate(),
                tmdbMovie.originalTitle,
                tmdbMovie.posterPath,
                listOf(),
                instant(),
                instant(),
            )
        }
    }
}