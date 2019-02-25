package com.mygroup.Movies.shared.dto

/*
 * The MovieDto class is used to pass around movie data between methods in the service.
 */
data class MovieDto (
    var id : Long = 0L,
    var name : String = "",
    var genre : String = "",
    var yearReleased : Int = 0,
    var rating : Int = 0
)
