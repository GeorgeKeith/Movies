# movies
MacMillan code challenge

This repository contains my submission for the MacMillan Movies code challenge.

It implements a REST API in Kotlin using Spring JPA and H2.

There is a complete set of unit tests using JUnit5 and Mockito. The coverage is 95% (all but two lines that should never be
executed.

The Kotlin code creates the H2 database table (Movies) as controlled by the MoviesEntity class.

The website uses port 8888 (as set in the application.properties file).

The API consists of five http methods:
    POST   localhost:8888/api/movies -- body is JSON {"name" : String, "genre" : String, "yearReleased": Int, "rating": Int}
      If a movie by that name does not exist, a new record is inserted with the given data. The response body is in JSON
        format with an additional field: "id" : Long.
      If a movie by that name already exists, an error is returned.
    GET    localhost:8888/api/movies/{name}
      If a movie by that name exists the body of the response is in JSON
      If a movie by that name does not exist an error is returned
    PUT    localhost:8888/api/movies -- body is JSON {"name" : String, "genre" : String, "yearReleased": Int, "rating": Int}
      If a movie by that name exists, the corresponding fields are update in the H2 database. The response body is in JSON
        format with an additional field: "id" : Long.
      If a movie by that name does not exist an error is returned
    DELETE localhost:8888/api/movies/{name}
      If a movie by that name exists, it's record is deleted from the movies table and a statusCode of HttpStatus.OK is
        returned.
      If a movie by that name does not exist, and error is returned.
    GET    localhost:8888/api/timeOfDay
      The body of the reply is in text form.
    
