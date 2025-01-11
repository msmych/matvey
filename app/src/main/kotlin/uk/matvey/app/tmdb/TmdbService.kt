package uk.matvey.app.tmdb

import uk.matvey.tmdb.TmdbClient

class TmdbService(
    private val tmdbClient: TmdbClient,
) {

    suspend fun saveMovie(id: Int){
        val movie = tmdbClient.getMovieCredits(id)

    }
}