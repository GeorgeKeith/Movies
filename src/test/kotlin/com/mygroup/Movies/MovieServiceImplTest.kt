package com.mygroup.Movies

//import com.mygroup.Movies.*
import com.mygroup.Movies.io.MovieEntity
import com.mygroup.Movies.service.Impl.MovieServiceImpl
import junit.framework.TestCase.*
//import org.junit.Test
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.doNothing
import org.mockito.MockitoAnnotations
import org.springframework.beans.BeanUtils
import org.springframework.boot.test.context.SpringBootTest

/* MovieServiceImplTest is implemented in Kotlin and uses JUnit5 Jupiter and Mockito and MovieServiceImpl.
The methods of MovieServiceImpl use methods of the MovieRepository interface.
Therefore, selected methods of MovieRepository are mocked to facilitate the testing.
 */
@SpringBootTest
class MovieServiceImplTest {
//    internal class MovieServiceImplTest {

    @InjectMocks
    lateinit var movieService : MovieServiceImpl

    @Mock
    lateinit var movieRepository : MovieRepository

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun createMovieTest() {
        var movieRequestModel = testMovieRequestModel()

        var savedEntity = MovieEntity()
        BeanUtils.copyProperties(movieRequestModel, savedEntity)
        savedEntity.id = savedIdValue

        Mockito.`when`(movieRepository.save(Mockito.any(MovieEntity::class.java))).thenReturn(savedEntity)

        var resultDto = movieService.createMovie(movieRequestModel)
        if (resultDto != null) {
            testReturnedMovieDto(resultDto)
        }
    }

    @Test
    fun getMovieTest() {
        var movieRequestModel = testMovieRequestModel()

        var savedEntity = MovieEntity()
        BeanUtils.copyProperties(movieRequestModel, savedEntity)
        savedEntity.id = savedIdValue

        // Test movie found
        Mockito.`when`(movieRepository.findMovieByName(nameValue)).thenReturn(savedEntity)

        var movieDto = movieService.getMovie(nameValue)
        testReturnedMovieDto(movieDto)

        // Test movie not found
        Mockito.`when`(movieRepository.findMovieByName("non name")).thenReturn(null)

        movieDto = movieService.getMovie("non name")
        assertNull(movieDto)

    }

    @Test
    fun updateMovieTest() {
        val movieRequestModel = testMovieRequestModel()

        val savedEntity = MovieEntity()
        BeanUtils.copyProperties(movieRequestModel, savedEntity)
        savedEntity.id = savedIdValue

        // Simulate an existing movie
        Mockito.`when`(movieRepository.findMovieByName(nameValue)).thenReturn(savedEntity)
        Mockito.`when`(movieRepository.save(Mockito.any(MovieEntity::class.java))).thenReturn(savedEntity)

        val resultDto1 = movieService.updateMovie(movieRequestModel)
        assertNotNull(resultDto1)
        if (resultDto1 != null) {
            testReturnedMovieDto(resultDto1)
        }

        // Simulate a nonexistent movie
        movieRequestModel.name = "non movie"
        Mockito.`when`(movieRepository.findMovieByName("non movie")).thenReturn(null)

        val resultDto2 = movieService.updateMovie(movieRequestModel)
        assertNull(resultDto2)
    }

    @Test
    fun deleteMovieTest() {
        val initialMovieRequestModel = testMovieRequestModel()

        val savedEntity = MovieEntity()
        BeanUtils.copyProperties(initialMovieRequestModel,savedEntity)

        Mockito.`when`(movieRepository.findMovieByName(anyString())).thenReturn(savedEntity)
        Mockito.`when`(movieRepository.findMovieByName("non movie")).thenReturn(null)

        doNothing().`when`(movieRepository).delete(Mockito.any(MovieEntity::class.java))

        val deleted1 = movieService.deleteMovie(nameValue)
        assertTrue(deleted1)

        val deleted2 = movieService.deleteMovie("non movie")
        assertFalse(deleted2)
    }
}