package uk.matvey.tmdb

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieCreditsResponse(
    val id: Int,
    val cast: List<CastMember>,
    val crew: List<CrewMember>
) {

    @Serializable
    data class CastMember(
        val id: Int,
        val adult: Boolean,
        val gender: Int?,
        @SerialName("known_for_department")
        val knownForDepartment: String,
        val name: String,
        @SerialName("original_name")
        val originalName: String,
        val popularity: Double,
        @SerialName("profile_path")
        val profilePath: String?,
        @SerialName("cast_id")
        val castId: Int,
        val character: String,
        @SerialName("credit_id")
        val creditId: String,
        val order: Int,
    )

    @Serializable
    data class CrewMember(
        val id: Int,
        val adult: Boolean,
        val gender: Int?,
        @SerialName("known_for_department")
        val knownForDepartment: String,
        val name: String,
        @SerialName("original_name")
        val originalName: String,
        val popularity: Double,
        @SerialName("profile_path")
        val profilePath: String?,
        @SerialName("credit_id")
        val creditId: String,
        val department: String,
        val job: String,
    )

    fun directors() = crew.filter { it.job == "Director" }
}