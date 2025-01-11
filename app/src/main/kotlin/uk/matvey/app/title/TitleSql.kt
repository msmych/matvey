package uk.matvey.app.title

import com.github.jasync.sql.db.Connection
import com.github.jasync.sql.db.RowData
import uk.matvey.kit.json.JsonKit.jsonDeserialize
import uk.matvey.kit.json.JsonKit.jsonSerialize
import uk.matvey.kit.time.TimeKit.instant
import uk.matvey.tmdb.MovieCreditsResponse
import uk.matvey.tmdb.MovieDetailsResponse.MovieDetails
import java.time.LocalDate
import java.time.ZoneOffset.UTC
import java.util.UUID

object TitleSql {

    fun Connection.addTitle(tmdbMovieDetails: MovieDetails, tmdbCredits: MovieCreditsResponse?): Title {
        return sendPreparedStatement(
            """
                insert into titles (type, title, details, created_at)
                values (?, ?, ?, ?)
                returning *
            """.trimIndent(),
            listOf(
                Title.Type.MOVIE,
                tmdbMovieDetails.title,
                jsonSerialize(
                    Title.Details(
                        tmdb = Title.Details.Tmdb(
                            id = tmdbMovieDetails.id,
                            releaseDate = LocalDate.parse(tmdbMovieDetails.releaseDate),
                            originalTitle = tmdbMovieDetails.originalTitle,
                            posterPath = tmdbMovieDetails.posterPath,
                            directorsNames = tmdbCredits?.directors()?.map { it.name },
                        )
                    )
                ),
                instant().toString()
            ),
        ).join()
            .rows
            .single()
            .toTitle()
    }

    fun RowData.toTitle(): Title {
        return Title(
            id = get("id") as UUID,
            type = Title.Type.valueOf(requireNotNull(getString("type"))),
            title = requireNotNull(getString("title")),
            details = jsonDeserialize<Title.Details>(requireNotNull(getString("details"))),
            createdAt = requireNotNull(getDate("created_at")).toInstant(UTC),
            updatedAt = requireNotNull(getDate("updated_at")).toInstant(UTC),
        )
    }
}