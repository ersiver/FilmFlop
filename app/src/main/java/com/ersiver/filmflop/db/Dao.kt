package com.ersiver.filmflop.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.ersiver.filmflop.model.Genre
import com.ersiver.filmflop.model.Movie
import com.ersiver.filmflop.model.Trailer

@Dao
interface MovieDao {
    //Inserts movies from the network.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(list: List<Movie>)

    //Returns list of movies by a title.
    @Query("SELECT * FROM movies_table WHERE movie_title LIKE '%' || :query || '%' ORDER BY movie_popularity DESC")
    suspend fun search(query: String): List<Movie>

    //Updates movie's fields.
    @Update
    suspend fun update(movie: Movie)

    //Returns a movie by ID.
    @Query("SELECT * FROM movies_table WHERE movie_id = :id")
    suspend fun getMovie(id: Int): Movie

    //Returns list of favourite movies.
    @RawQuery(observedEntities = [Movie::class])
    fun getFavouriteMovies(query: SupportSQLiteQuery): LiveData<List<Movie>>
}

@Dao
interface GenreDao {
    //Inserts genre list from the network.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(genres: List<Genre>)

    //Returns the list of genres names matching movie's genre ids.
    @Query("SELECT genre_name FROM genres_table WHERE genre_id IN (:idList)")
    suspend fun getGenres(idList: List<Int>): List<String>
}

@Dao
interface TrailerDao {
    //Inserts a trailer from the network.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trailer: Trailer)

    //Returns a trailer matching movie's id.
    @Query("SELECT * FROM movie_trailer WHERE movie_id= :id")
    suspend fun getTrailer(id: Int): Trailer
}