package com.ersiver.filmflop

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.ersiver.filmflop.db.GenreDao
import com.ersiver.filmflop.db.TrailerDao
import com.ersiver.filmflop.model.Trailer
import com.ersiver.filmflop.utils.TestUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`


/**
 * Instrumented  unit test, for implementation of [TrailerDao].
 */


@SmallTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class TrailerDaoTest : LocalDatabase(){
    private lateinit var trailerDao: TrailerDao
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        trailerDao = database.trailerDao
    }

    @Test
    @Throws(Exception::class)
    fun insertAndLoadTrailer() = runBlockingTest {
        val trailer = TestUtil.createTrailer()

        //Insert trailer to db.
        trailerDao.insert(trailer)

        //Load trailed from the db by id. Assert that
        // loaded data contains the expected values.
        val load = trailerDao.getTrailer(trailer.movieId)
        assertThat(load.movieId, `is` (trailer.movieId))
        assertThat(load.url, `is`(trailer.url))
    }
}