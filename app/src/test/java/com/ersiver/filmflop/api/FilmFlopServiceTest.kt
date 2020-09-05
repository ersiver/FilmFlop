package com.ersiver.filmflop.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ersiver.filmflop.MainCoroutinesRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class FilmFlopServiceTest : ApiAbstract<FilmFlopService>() {
    @get: Rule
    val coroutinesRule = MainCoroutinesRule()

    @get: Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: FilmFlopService

    @Before
    fun setUp() {
        service = createService(FilmFlopService::class.java)
    }

    @Throws(IOException::class)
    @Test
    fun getMoviesTest() = runBlocking {
        enqueueResponse("/MovieResponse.json")
        val response = service.getMoviesAsync(title = "Joker").await()
        val responseBody = requireNotNull(response.apiMovies)
        mockWebServer.takeRequest()

        val loaded = responseBody[0]
        assertThat(responseBody.count(), `is`(20))
        assertThat(loaded.id, `is`(475557))
        assertThat(loaded.title, `is`("Joker"))
        assertThat(loaded.popularity, `is`(215.35))
        assertThat(loaded.posterUrl, `is`("/udDclJoHjfjb8Ekgsd4FDteOkCU.jpg"))
        assertThat(loaded.language, `is`("en"))
        assertThat(
            loaded.overview,
            `is`("During the 1980s, a failed stand-up comedian is driven insane and turns to a life of crime and chaos in Gotham City while becoming an infamous psychopathic crime figure.")
        )
        assertThat(loaded.releaseDate, `is`("2019-10-02"))
        assertThat(loaded.genreIds, `is`(listOf(80, 18, 53)))
        assertThat(loaded.rating, `is`(8.2))
    }

    @Throws(IOException::class)
    @Test
    fun getGenreTest() = runBlocking {
        enqueueResponse("/GenreResponse.json")
        val response = service.getGenresAsync().await()
        val responseBody = requireNotNull(response.apiGenres)
        mockWebServer.takeRequest()

        val loaded = responseBody[0]
        assertThat(responseBody.size, `is`(19))
        assertThat(loaded.id, `is`(28))
        assertThat(loaded.name, `is`("Action"))
    }

    @Throws(IOException::class)
    @Test
    fun getTrailerTest() = runBlocking() {
        enqueueResponse("/TrailerResponse.json")
        val response = service.getTrailerAsync(475557).await()
        val responseBody = requireNotNull(response.trailersApi)
        mockWebServer.takeRequest()

        val loaded = responseBody[0]
        assertThat(responseBody.size, `is`(4))
        assertThat(loaded.url, `is`("t433PEQGErc"))
    }
}