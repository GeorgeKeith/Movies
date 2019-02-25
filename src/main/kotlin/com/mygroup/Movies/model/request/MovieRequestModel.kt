package com.mygroup.Movies.model.request

/*
 * The MovieRequestModel data class is used as input to the MovieController methods for createMovie and updateMovie.
 * Notice that it does not have an id field.
*/
data class MovieRequestModel(
        var name: String = "",
        var genre: String = "",
        var yearReleased: Int = 0,
        var rating: Int = 0
)