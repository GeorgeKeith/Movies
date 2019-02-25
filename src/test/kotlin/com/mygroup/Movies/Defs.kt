package com.mygroup.Movies

import com.mygroup.Movies.model.request.MovieRequestModel
import com.mygroup.Movies.shared.dto.MovieDto
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull

// The definitions here are used in the tests
const val nameValue = "Gone with the Wind"
const val genreValue = "History"
const val yearReleasedValue = 1935
const val ratingValue = 5
const val savedIdValue = 111L

// This function creates a MovieRequestModel populated with test data
fun testMovieRequestModel() : MovieRequestModel {
    val movieRequestModel = MovieRequestModel(
            nameValue,
            genreValue,
            yearReleasedValue,
            ratingValue
    )
    return movieRequestModel
}

// This function looks for the expected contents of a MovieDto
fun testReturnedMovieDto(movieDto : MovieDto?) {

    assertNotNull(movieDto)
    if (movieDto != null) {
        assertEquals(savedIdValue, movieDto.id)
        assertEquals(nameValue, movieDto.name)
        assertEquals(genreValue, movieDto.genre)
        assertEquals(yearReleasedValue, movieDto.yearReleased)
        assertEquals(ratingValue, movieDto.rating)
    }
}