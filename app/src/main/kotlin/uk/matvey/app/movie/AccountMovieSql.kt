package uk.matvey.app.movie

import com.github.jasync.sql.db.Connection
import com.github.jasync.sql.db.RowData
import uk.matvey.app.movie.MovieSql.toMovie
import uk.matvey.kit.time.TimeKit.instant

object AccountMovieSql {

    fun Connection.getAccountMovie(accountId: Int, movieId: Int): AccountMovie {
        return sendPreparedStatement(
            """
                SELECT * 
                FROM movies m 
                join accounts_movies am on m.id = am.movie_id 
                WHERE account_id = ? AND movie_id = ?
                """.trimIndent(),
            listOf(accountId, movieId)
        )
            .join()
            .rows
            .single()
            .toAccountMovie()
    }

    fun Connection.getAccountMovies(accountId: Int, filter: String): List<AccountMovie> {
        val filterCondition = when (filter) {
            "TO_WATCH" -> "to_watch"
            "WATCHED" -> "watched"
            else -> "true"
        }
        return sendPreparedStatement(
            """
                SELECT * 
                FROM movies m 
                join accounts_movies am on m.id = am.movie_id 
                WHERE account_id = ? and $filterCondition
                """.trimIndent(),
            listOf(accountId)
        )
            .join()
            .rows
            .map { it.toAccountMovie() }
    }

    fun Connection.getAccountMovies(accountId: Int, moviesIds: List<Int>): List<AccountMovie> {
        return sendPreparedStatement(
            """
                SELECT * 
                FROM movies m 
                join accounts_movies am on m.id = am.movie_id 
                WHERE account_id = ? AND movie_id = any(${moviesIds.joinToString()})
                """.trimIndent(),
            listOf(accountId)
        )
            .join()
            .rows
            .map { it.toAccountMovie() }
    }

    fun Connection.updateAccountMovieToWatch(accountId: Int, movieId: Int, toWatch: Boolean) {
        sendPreparedStatement(
            """
                insert into accounts_movies (account_id, movie_id, to_watch, created_at)
                values (?, ?, ?, ?)
                on conflict (account_id, movie_id) do update set to_watch = excluded.to_watch
                """.trimIndent(),
            listOf(accountId, movieId, toWatch, instant().toString())
        )
            .join()
    }

    fun Connection.updateAccountMovieWatched(accountId: Int, movieId: Int, watched: Boolean) {
        sendPreparedStatement(
            """
                insert into accounts_movies (account_id, movie_id, watched, created_at)
                values (?, ?, ?, ?)
                on conflict (account_id, movie_id) do update set watched = excluded.watched
                """.trimIndent(),
            listOf(accountId, movieId, watched, instant().toString())
        )
            .join()
    }

    fun RowData.toAccountMovie(): AccountMovie {
        return AccountMovie(
            movie = toMovie(),
            toWatch = requireNotNull(getBoolean("to_watch")),
            watched = requireNotNull(getBoolean("watched")),
        )
    }
}