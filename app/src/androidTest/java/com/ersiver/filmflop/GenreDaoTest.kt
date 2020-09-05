package com.ersiver.filmflop

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.ersiver.filmflop.db.GenreDao
import com.ersiver.filmflop.utils.TestUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented  unit test, for implementation of [GenreDao].
 */

@SmallTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class GenreDaoTest : LocalDatabase() {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var genreDao: GenreDao

    @Before
    fun setup(){
        genreDao = database.genreDao
    }

    @Test
    @Throws(Exception::class)
    fun insertAndLoad() = runBlockingTest {
        val genre =  TestUtil.createGenre()
        genreDao.insertAll(listOf(genre))

        //Load genres by id from database. Verify there is 1 genre
        // in the database,  and contains the expected values.
        val idList = listOf(genre.id)
        val load = genreDao.getGenres(idList)
        assertThat(load.size, `is`(1))
        assertThat(load[0], `is`(genre.name))
    }
}