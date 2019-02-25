package com.mygroup.Movies.io

import javax.persistence.*

/*
 * The MovieEntity class is used by the MovieRepository. To drive the interactions with JPA and the H2 database.
 *
 */
@Entity(name = "movies")
data class MovieEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false, length = 50)
    var name: String = "",

    @Column(nullable = false, length = 50)
    var genre: String = "",

    @Column(nullable = false)
    var yearReleased: Int = 0,

    @Column(nullable = false)
    var rating: Int = 0
)