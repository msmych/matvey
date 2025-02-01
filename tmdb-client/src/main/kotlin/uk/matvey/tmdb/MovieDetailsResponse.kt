package uk.matvey.tmdb

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import uk.matvey.kit.json.JsonKit.JSON
import uk.matvey.kit.json.JsonKit.arr
import uk.matvey.kit.json.JsonKit.objOrNull
import uk.matvey.tmdb.CreditsResponse.CastPerson
import uk.matvey.tmdb.CreditsResponse.CrewPerson

class MovieDetailsResponse(
    val jsonObject: JsonObject,
) {

    val movieDetails = JSON.decodeFromJsonElement<MovieDetails>(jsonObject)
    val cast = jsonObject.objOrNull("credits")?.let { credits ->
        JSON.decodeFromJsonElement<List<CastPerson>>(credits.arr("cast"))
    }
    val crew = jsonObject.objOrNull("credits")?.let { credits ->
        JSON.decodeFromJsonElement<List<CrewPerson>>(credits.arr("crew"))
    }

    @Serializable
    data class MovieDetails(
        val id: Int,
        val title: String,
        val adult: Boolean,
        @SerialName("backdrop_path")
        val backdropPath: String? = null,
        @SerialName("belongs_to_collection")
        val belongsToCollection: TmdbCollection? = null,
        val budget: Int,
        val genres: List<Genre>,
        val homepage: String?,
        val imdbId: String? = null,
        @SerialName("original_language")
        val originalLanguage: String? = null,
        @SerialName("original_title")
        val originalTitle: String? = null,
        val overview: String? = null,
        val popularity: Double,
        @SerialName("poster_path")
        val posterPath: String?,
        @SerialName("production_companies")
        val productionCompanies: List<ProductionCompany>,
        @SerialName("production_countries")
        val productionCountries: List<ProductionCountry>,
        @SerialName("release_date")
        val releaseDate: String? = null,
        val revenue: Int,
        val runtime: Int,
        @SerialName("spoken_languages")
        val spokenLanguages: List<SpokenLanguage>,
        val status: String? = null,
        val tagline: String? = null,
        val video: Boolean,
        @SerialName("vote_average")
        val voteAverage: Double,
        @SerialName("vote_count")
        val voteCount: Int,
    ) {

        @Serializable
        data class TmdbCollection(
            val id: Int,
            val name: String,
            @SerialName("poster_path")
            val posterPath: String? = null,
            @SerialName("backdrop_path")
            val backdropPath: String? = null,
        )

        @Serializable
        data class Genre(
            val id: Int,
            val name: String,
        )

        @Serializable
        data class ProductionCompany(
            val id: Int,
            val name: String,
            @SerialName("logo_path")
            val logoPath: String? = null,
            @SerialName("origin_country")
            val originCountry: String,
        )

        @Serializable
        data class ProductionCountry(
            @SerialName("iso_3166_1")
            val iso31661: String,
            val name: String,
        )

        @Serializable
        data class SpokenLanguage(
            @SerialName("iso_639_1")
            val iso6391: String,
            val name: String,
            @SerialName("english_name")
            val englishName: String,
        )
    }
}
