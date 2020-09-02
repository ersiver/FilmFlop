package com.ersiver.filmflop.util

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQueryBuilder

class SortUtil {
    companion object {
        enum class MovieSortType {
            TITLE, DEFAULT
        }

        /**
         * A raw query at runtime to oder by column
         * for getting all the favourite movies sorted.
         */
        fun getAllQuery(sortBy: MovieSortType): SimpleSQLiteQuery {

            val queryBuilder: SupportSQLiteQueryBuilder = SupportSQLiteQueryBuilder
                .builder(DataMovieNames.TABLE_NAME)
                .orderBy(getSortColumn(sortBy))
                queryBuilder.selection(DataMovieNames.COL_FAVORITE, arrayOf("1"))

            return SimpleSQLiteQuery(queryBuilder.create().sql)
        }

        /**
         * Get a column name in from a preference value.
         */
        private fun getSortColumn(value: MovieSortType): String {
            return when (value) {
                MovieSortType.TITLE -> DataMovieNames.COL_TITLE
                MovieSortType.DEFAULT -> DataMovieNames.COL_DATE + " DESC"
            }
        }
    }
}

/**
 * Database naming schema for sorting purpose
 */
class DataMovieNames {
    /**
     * Names must match columns' names.
     */
    companion object {
        val TABLE_NAME: String = "movies_table"

        /**
         * Column name for title
         */
        val COL_TITLE: String = "movie_title"

        /**
         * Column name for Movie favorite
         */
        val COL_FAVORITE: String = "is_favourite"
        
        /**
         * Column name for saving date
         */
        val COL_DATE: String = "saved_date"
    }
}