package uk.matvey.tmdb

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreditsResponse(
    val id: Int,
    val cast: List<CastPerson>,
    val crew: List<CrewPerson>
) {

    @Serializable
    data class CastPerson(
        val id: Int,
        val adult: Boolean,
        val gender: Int? = null,
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
    data class CrewPerson(
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
    ) {
        fun isDirector() = job == "Director"
    }

    fun directors() = crew.filter { it.isDirector() }
}