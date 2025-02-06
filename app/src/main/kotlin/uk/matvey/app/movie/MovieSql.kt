package uk.matvey.app.movie

import com.github.jasync.sql.db.Connection
import com.github.jasync.sql.db.RowData
import uk.matvey.kit.time.TimeKit.instant
import uk.matvey.tmdb.MovieDetailsResponse.MovieDetails
import java.time.LocalDate
import java.time.ZoneOffset.UTC

object MovieSql {

    fun Connection.findMovie(id: Int): Movie? {
        return sendPreparedStatement("select * from movies where id = ?", listOf(id))
            .join()
            .rows
            .singleOrNull()
            ?.toMovie()
    }

    fun Connection.addMovie(tmdbMovieDetails: MovieDetails, directorsIds: List<Int>): Movie {
        return sendPreparedStatement(
            """
                insert into movies (id, title, release_date, original_title, poster_path, directors_ids, created_at)
                values (?, ?, ?, ?, ?, ?, ?)
                returning *
            """.trimIndent(),
            listOf(
                tmdbMovieDetails.id,
                tmdbMovieDetails.title,
                tmdbMovieDetails.releaseDate?.let(LocalDate::parse),
                tmdbMovieDetails.originalTitle,
                tmdbMovieDetails.posterPath,
                directorsIds,
                instant().toString()
            ),
        ).join()
            .rows
            .single()
            .toMovie()
    }

    @Suppress("UNCHECKED_CAST")
    fun RowData.toMovie(): Movie {
        return Movie(
            id = requireNotNull(getInt("id")),
            title = requireNotNull(getString("title")),
            releaseDate = get("release_date") as LocalDate?,
            originalTitle = getString("original_title"),
            posterPath = getString("poster_path"),
            directorsIds = get("directors_ids") as List<Int>,
            createdAt = requireNotNull(getDate("created_at")).toInstant(UTC),
            updatedAt = requireNotNull(getDate("updated_at")).toInstant(UTC),
        )
    }
}