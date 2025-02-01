package uk.matvey.tmdb

import kotlinx.serialization.Serializable

@Serializable
data class PersonMovieCreditsResponse(
    val id: Int,
    val cast: List<Movie>,
    val crew: List<Movie>
)
