package uk.matvey.tmdb

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.http.ContentType.Application
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import uk.matvey.kit.json.JsonKit.JSON
import java.time.Year

/**
 * [TMDb API](https://developer.themoviedb.org/reference)
 */
class TmdbClient(
    token: String,
) {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(JSON)
        }
        install(Logging) {
            level = LogLevel.INFO
        }
        defaultRequest {
            contentType(Application.Json)
            bearerAuth(token)
        }
    }

    /**
     * [Search for movies](https://developer.themoviedb.org/reference/search-movie)
     * by their original, translated and alternative titles.
     */
    suspend fun searchMovie(
        query: String,
        includeAdult: Boolean = false,
        language: String = "en-US",
        page: Int = 1,
        year: Year? = null,
        primaryReleaseYear: Year? = null,
        region: String? = null,
    ): SearchMovieResponse {
        return client.get(path("/search/movie")) {
            url.parameters.append("query", query)
            url.parameters.append("include_adult", includeAdult.toString())
            url.parameters.append("language", language)
            url.parameters.append("page", page.toString())
            year?.let { url.parameters.append("year", it.toString()) }
            primaryReleaseYear?.let { url.parameters.append("primary_release_year", it.toString()) }
            region?.let { url.parameters.append("region", it) }
        }
            .body()
    }

    private fun path(path: String) = "https://api.themoviedb.org/3/$path"
}