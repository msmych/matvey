package uk.matvey.tmdb

import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable

class TmdbClientTest {

    private fun tmdbClient() = TmdbClient(System.getenv("TMDB_TOKEN"))

    @Test
    @EnabledIfEnvironmentVariable(named = "TMDB_TOKEN", matches = ".+")
    fun `search movies`() = runTest {
        // given
        val client = tmdbClient()

        // when
        val rs = client.searchMovie("nosferatu")

        // then
        assertThat(rs.page).isEqualTo(1)
        assertThat(rs.results).isNotEmpty
        assertThat(rs.totalPages).isGreaterThanOrEqualTo(1)
        assertThat(rs.totalResults).isGreaterThanOrEqualTo(1)
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "TMDB_TOKEN", matches = ".+")
    fun `get movie details`() = runTest {
        // given
        val client = tmdbClient()

        // when
        val rs = client.getMovieDetails(426063)

        // then
        assertThat(rs.movieDetails.id).isEqualTo(426063)
        assertThat(rs.movieDetails.title).isEqualTo("Nosferatu")
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "TMDB_TOKEN", matches = ".+")
    fun `get credits`() = runTest {
        // given
        val client = tmdbClient()

        // when
        val rs = client.getCredits(426063)

        // then
        assertThat(rs.cast).isNotEmpty
        assertThat(rs.crew).isNotEmpty
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "TMDB_TOKEN", matches = ".+")
    fun `get movie credits`() = runTest {
        // given
        val client = tmdbClient()

        // when
        val rs = client.getMovieCredits(138781)

        // then
        assertThat(rs.cast).isNotEmpty
        assertThat(rs.crew).isNotEmpty
    }
}