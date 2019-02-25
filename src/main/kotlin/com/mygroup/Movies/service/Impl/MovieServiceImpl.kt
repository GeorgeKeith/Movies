package com.mygroup.Movies.service.Impl

import com.mygroup.Movies.MovieRepository
import com.mygroup.Movies.io.MovieEntity
import com.mygroup.Movies.model.request.MovieRequestModel
import com.mygroup.Movies.service.MovieService
import com.mygroup.Movies.shared.dto.MovieDto
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/*
 * The MovieServiceImpl class implements the MovieService interface. It uses the MovieRepository class to perform the
 * database operations.
 */

@Service
class MovieServiceImpl : MovieService {

    @Autowired
    lateinit var movieRepository: MovieRepository

    override fun createMovie(movieRequestModel : MovieRequestModel?): MovieDto? {
        var returnValue : MovieDto? = null

        if (movieRequestModel != null) {
            var temp = movieRepository.findMovieByName(movieRequestModel.name)
            if (temp == null) {
                val movieEntity = MovieEntity()
                BeanUtils.copyProperties(movieRequestModel, movieEntity)
                val storedMovie = movieRepository.save(movieEntity)

                returnValue = MovieDto()
                BeanUtils.copyProperties(storedMovie, returnValue)
            }
        }
        return returnValue
    }

    override fun getMovie(name: String): MovieDto? {
        var returnValue: MovieDto? = null

        var movieEntity = movieRepository.findMovieByName(name)
        if (movieEntity != null) {
            returnValue = MovieDto()
            BeanUtils.copyProperties(movieEntity, returnValue)
        }
        return returnValue
    }

    override fun updateMovie(movieRequestModel: MovieRequestModel?): MovieDto? {
        var returnValue : MovieDto? = null
        if (movieRequestModel != null) {
            // The update is only attempted if the movie is in the repository.
            // Notice that the MovieEntity returned by the find is used in the save to do the update.
            var movieEntity = movieRepository.findMovieByName(movieRequestModel.name)
            if (movieEntity != null) {
                // Note that since the MovieRequestModel does not contain an id field, this copy does not modify the
                // id retrieved in the find and so the save performs the update as desired.
                BeanUtils.copyProperties(movieRequestModel, movieEntity)

                movieEntity = movieRepository.save(movieEntity)

                returnValue = MovieDto()
                BeanUtils.copyProperties(movieEntity, returnValue)
            }
        }
        return returnValue
    }

    override fun deleteMovie(name: String) : Boolean {
        // The delete is only attempted if the movie is in the repository.
        var movieEntity = movieRepository.findMovieByName(name)
        if (movieEntity != null) {

            movieRepository.delete(movieEntity)

            return true
        } else {
            return false
        }
    }
}