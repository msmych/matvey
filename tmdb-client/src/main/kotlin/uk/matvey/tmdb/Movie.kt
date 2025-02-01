package uk.matvey.tmdb

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Year

@Serializable
data class Movie(
    val id: Int,
    val title: String,
    val adult: Boolean,
    val backdropPath: String? = null,
    @SerialName("genre_ids")
    val genreIds: Collection<Int>,
    @SerialName("original_language")
    val originalLanguage: String? = null,
    @SerialName("original_title")
    val originalTitle: String? = null,
    val overview: String?,
    val popularity: Double,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("release_date")
    val releaseDate: String? = null,
    val video: Boolean,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int,
) {
    fun releaseYear(): Year? = releaseDate?.let { Year.parse(it.substringBefore('-')) }
}