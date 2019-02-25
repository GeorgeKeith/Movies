package com.mygroup.Movies.service

import com.mygroup.Movies.model.request.MovieRequestModel
import com.mygroup.Movies.shared.dto.MovieDto

/*
 * The MovieService interface is defined to allow for multiple implementations using different technologies as needed.
 * Currently, the only implementation, MovieServiceImpl, uses MovieRepository which in turn uses JPA and H2.
 */
interface MovieService {
    fun createMovie(movieRequestModel : MovieRequestModel?) : MovieDto?
    fun getMovie(name : String) : MovieDto?
    fun updateMovie(movieRequestModel : MovieRequestModel?) : MovieDto?
    fun deleteMovie(name : String) : Boolean
}