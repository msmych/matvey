package uk.matvey.tmdb

import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable

class TmdbClientTest {

    @Test
    @EnabledIfEnvironmentVariable(named = "TMDB_API_KEY", matches = ".+")
    fun `search movies`() = runTest {
        // given
        val client = TmdbClient(System.getenv("TMDB_API_KEY"))

        // when
        val rs = client.searchMovie("nosferatu")

        // then
        assertThat(rs.page).isEqualTo(1)
        assertThat(rs.results).isNotEmpty
        assertThat(rs.totalPages).isGreaterThanOrEqualTo(1)
        assertThat(rs.totalResults).isGreaterThanOrEqualTo(1)
    }
}