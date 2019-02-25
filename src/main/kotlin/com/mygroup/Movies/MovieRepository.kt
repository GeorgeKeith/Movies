package com.mygroup.Movies

import com.mygroup.Movies.io.MovieEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

/*
 * The MovieRepository interface is built on the CrudRepository<> interface and the implementation is automatically
 * added when an instance is Autowired in MovieServiceImpl.
 *
 * This is mocked in the tests for MovieServiceImpl. During normal operations dependency injection connects through JPA
 * to H2
 */
@Repository
interface MovieRepository : CrudRepository<MovieEntity, Long> {
    fun findMovieByName(name : String) : MovieEntity?
}