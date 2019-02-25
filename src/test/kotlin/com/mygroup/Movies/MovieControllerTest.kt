package com.mygroup.Movies

import com.mygroup.Movies.controller.MovieController
import com.mygroup.Movies.io.MovieEntity
import com.mygroup.Movies.model.request.MovieRequestModel
//import com.mygroup.Movies.nameValue
//import com.mygroup.Movies.savedIdValue
import com.mygroup.Movies.service.Impl.MovieServiceImpl
import com.mygroup.Movies.shared.dto.MovieDto
//import com.mygroup.Movies.testMovieRequestModel
//import com.mygroup.Movies.testReturnedMovieDto
import junit.framework.TestCase.*
//import org.junit.Test
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.*
import java.net.URI
import java.net.URLEncoder
import java.time.Instant

/*
 * MovieControllerTest is implemented in Kotlin and uses JUnit5 Jupiter and Mockito and TestRestTemplate.
 * The tests here test the REST API implemented in MovieController.kt
 * TestRestTemplate is used to simulate the http calls to the API.
 * The methods of MovieController use methods of the MovieService interface as implemented in MovieServiceImpl.
 * Therefore, selected methods of MovieServiceImpl are mocked to facilitate the testing.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class MovieControllerTest {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @LocalServerPort
    var port: Int = 0

    @InjectMocks
    lateinit var movieController: MovieController

    @MockBean
    lateinit var movieService: MovieServiceImpl

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun createMovieTest() {
        val movieRequestModel = testMovieRequestModel()

        val savedDto = MovieDto()
        BeanUtils.copyProperties(movieRequestModel, savedDto)
        savedDto.id = savedIdValue

        // Try non existent movie
        Mockito.`when`(movieService.createMovie(Mockito.any(MovieRequestModel::class.java))).thenReturn(savedDto)

        val result1 = testRestTemplate.postForEntity("/api/movies", movieRequestModel, MovieDto::class.java)

        assertNotNull(result1)
        if (result1 != null) {
            val body = result1.body
            if (body != null) {
                val resultDto = MovieDto()
                BeanUtils.copyProperties(body, resultDto)
                testReturnedMovieDto(resultDto)
            }
        }

        // Try existent movie
        Mockito.`when`(movieService.getMovie("exister")).thenReturn(savedDto)

        movieRequestModel.name = "exister"
        val result2 = testRestTemplate.postForEntity("/api/movies", movieRequestModel, MovieDto::class.java)

        assertNotNull(result2)
        if (result2 != null) {
            assertEquals(HttpStatus.CONFLICT, result2.statusCode)
        }
    }

    @Test
    fun getMovieTest() {
        val movieRequestModel = testMovieRequestModel()
        val savedDto = MovieDto()
        BeanUtils.copyProperties(movieRequestModel, savedDto)
        savedDto.id = savedIdValue

        // Movie found mock
        Mockito.`when`(movieService.getMovie(anyString())).thenReturn(null)
        Mockito.`when`(movieService.getMovie(nameValue)).thenReturn(savedDto)

        var encodedName1 = URLEncoder.encode(nameValue, "UTF-8")
        val url = "/api/movies/$encodedName1"

        val result = testRestTemplate.getForEntity(url, MovieDto::class.java)

        assertNotNull(result)
        if (result != null) {
            assertEquals(HttpStatus.OK, result.statusCode)
            if (result.statusCode == HttpStatus.OK) {
                val body = result.body
                if (body != null) {
                    testReturnedMovieDto(body)
                }
            }
        }

        // Try a non movie name
        val encodedName2 = URLEncoder.encode("non name", "UTF-8")
        val url2 = "/api/movies/$encodedName2"

        val result2 = testRestTemplate.getForEntity(url2, MovieDto::class.java)

        assertNotNull(result2)
        if (result2 != null) {
            assertEquals(HttpStatus.NOT_FOUND, result2.statusCode)
        }
    }

    @Test
    fun getTimeOfDayTest() {
        val url = "/api/timeOfDay"

        val firstInstant = Instant.now()

        val responseEntity = testRestTemplate.getForEntity(url, String::class.java)

        assertNotNull(responseEntity)
        if (responseEntity != null) {
            assertEquals(HttpStatus.OK, responseEntity.statusCode)
            if (responseEntity.statusCode == HttpStatus.OK) {
                val resultInstant = Instant.parse(responseEntity.body)
                val lastInstant = Instant.now()
                assertTrue(firstInstant <= resultInstant)
                assertTrue(resultInstant <= lastInstant)
            }
        }
    }

    @Test
    fun updateMovieTest() {
        val movieRest = testMovieRequestModel()

        val movieRequestModel = MovieRequestModel()
        BeanUtils.copyProperties(movieRest, movieRequestModel)

        val savedDto = MovieDto()
        BeanUtils.copyProperties(movieRest, savedDto)
        savedDto.id = savedIdValue

        val url = "http://localhost:$port/api/movies"

        // Movie found and updated
        Mockito.`when`(movieService.getMovie(nameValue)).thenReturn(savedDto)
        Mockito.`when`(movieService.updateMovie(Mockito.any(MovieRequestModel::class.java))).thenReturn(savedDto)

        val requestEntity1 = RequestEntity
                .put(URI("/api/movies"))
                .accept(MediaType.APPLICATION_JSON)
                .body(movieRequestModel);

        val responseEntity1 = testRestTemplate.exchange(url, HttpMethod.PUT, requestEntity1, MovieDto::class.java)

        assertNotNull(responseEntity1)
        if (responseEntity1 != null) {
            assertEquals(HttpStatus.OK, responseEntity1.statusCode)
            if (responseEntity1.statusCode == HttpStatus.OK) {
                val body = responseEntity1.body
                if (body != null) {
                    testReturnedMovieDto(body)
                }
            }
        }

        // Movie not found, bad name
        Mockito.`when`(movieService.getMovie(anyString())).thenReturn(null)

        val requestEntity2 = RequestEntity
                .put(URI("/api/movies"))
                .accept(MediaType.APPLICATION_JSON)
                .body(movieRequestModel);

        val responseEntity2 = testRestTemplate.exchange(url, HttpMethod.PUT, requestEntity2, MovieEntity::class.java)

        assertNotNull(responseEntity2)
        if (responseEntity2 != null) {
            assertEquals(HttpStatus.NOT_FOUND, responseEntity2.statusCode)
        }
    }

    @Test
    fun deleteMovieTest() {

        // Movie found mock

        Mockito.`when`(movieService.deleteMovie(nameValue)).thenReturn(true)

        val url = "http://localhost:$port/api/movies/{name}"

        // Test found and deleted
        var encodedName1 = URLEncoder.encode(nameValue, "UTF-8")

        // exchange
        val responseEntity1 = testRestTemplate.exchange(url,
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                String::class.java,
                encodedName1)

        assertNotNull(responseEntity1)
        if (responseEntity1 != null) {
            assertEquals(HttpStatus.OK, responseEntity1.statusCode)
        }

        // Test not found
        Mockito.`when`(movieService.deleteMovie(anyString())).thenReturn(false)

        var encodedName2 = URLEncoder.encode("non movie", "UTF-8")

        // exchange
        val responseEntity2 = testRestTemplate.exchange(url,
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                String::class.java,
                encodedName2)

        assertNotNull(responseEntity2)
        if (responseEntity2 != null) {
            assertEquals(HttpStatus.NOT_FOUND, responseEntity2.statusCode)
        }
    }
}