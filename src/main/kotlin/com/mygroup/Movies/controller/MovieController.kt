package com.mygroup.Movies.controller

import com.mygroup.Movies.model.request.MovieRequestModel
import com.mygroup.Movies.service.MovieService
import com.mygroup.Movies.shared.dto.MovieDto
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URLDecoder
import java.time.Instant

/* The MovieController class implements the methods of the API. It implements a RestController to map URLs to each API
 * method.
 */

@RestController
@RequestMapping("/api")
class MovieController {

    @Autowired
    lateinit var movieService : MovieService

    @PostMapping("/movies")
    fun createMovie(@RequestBody movieRequestModel : MovieRequestModel) : ResponseEntity<MovieDto> {
        lateinit var responseEntity : ResponseEntity<MovieDto>
        val returnValue = MovieDto()

        // Try to see if movie exists
        val movieRest = movieService.getMovie(movieRequestModel.name)
        if (movieRest == null) {
            // Movie does not exist, create it

            val createdMovie = movieService.createMovie(movieRequestModel)
            if (createdMovie != null) {
                BeanUtils.copyProperties(createdMovie, returnValue)
                responseEntity = ResponseEntity(returnValue, HttpStatus.OK)
            } else {
                responseEntity = ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
            }
        } else {
            // Movie exists, can't create it
            responseEntity = ResponseEntity(HttpStatus.CONFLICT)
        }
        return responseEntity
    }

    @GetMapping("/movies/{name}")
    fun getMovie(@PathVariable name: String): ResponseEntity<MovieDto?> {
        var responseEntity : ResponseEntity<MovieDto?> = ResponseEntity(null as MovieDto?, HttpStatus.NOT_FOUND)
        val decodedName = URLDecoder.decode(name, "UTF-8")
        val movieDto = movieService.getMovie(decodedName)
        if (movieDto != null) {
            responseEntity = ResponseEntity(movieDto, HttpStatus.OK)
        }
        return responseEntity
    }

    @GetMapping("/timeOfDay")
    fun getTimeOfDay(): ResponseEntity<String> {
        return ResponseEntity(Instant.now().toString(), HttpStatus.OK)
    }

    @PutMapping("/movies")
    fun updateMovie(@RequestBody movieRequestModel : MovieRequestModel) : ResponseEntity<MovieDto?> {
        var responseEntity :  ResponseEntity<MovieDto?> = ResponseEntity(null as MovieDto?, HttpStatus.NOT_FOUND)
        val movieDto = movieService.getMovie(movieRequestModel.name)
        if (movieDto != null) {
            val createdMovie = movieService.updateMovie(movieRequestModel)
            if (createdMovie != null) {
                val returnValue = MovieDto()
                BeanUtils.copyProperties(createdMovie, returnValue)
                responseEntity = ResponseEntity(returnValue, HttpStatus.OK)
            }
        }
        return responseEntity
    }

    @DeleteMapping("/movies/{name}")
    fun deleteMovie(@PathVariable name: String): ResponseEntity<String> {
        val decodedName = URLDecoder.decode(name, "UTF-8")
        val found = movieService.deleteMovie(decodedName)
        return if (found)
            ResponseEntity("Movie: \"$decodedName\" deleted", HttpStatus.OK)
        else
            ResponseEntity("Movie: \"$decodedName\" not found", HttpStatus.NOT_FOUND)
    }
}