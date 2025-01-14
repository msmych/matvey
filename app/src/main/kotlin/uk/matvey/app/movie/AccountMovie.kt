package uk.matvey.app.movie

data class AccountMovie(
    val movie: Movie,
    val toWatch: Boolean,
    val watched: Boolean,
)