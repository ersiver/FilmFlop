package com.ersiver.filmflop.util

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.util.*

/**
 * Converts the Genre lists to String, so the DB knows how to insert it.
 */
class GenreConverter {
    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @TypeConverter
    fun genreListToString(genreIds: List<Int>?): String {
         val jsonAdapter: JsonAdapter<List<Int>> =
            moshi.adapter<List<Int>>(Types.newParameterizedType(List::class.java, Integer::class.java))
                .nonNull()

        if (genreIds == null)
            return jsonAdapter.toJson(Collections.emptyList<Int>())
        return jsonAdapter.toJson(genreIds)
    }

    @TypeConverter
    fun stringToGenreList(json: String?): List<Int>? {
        val jsonAdapter: JsonAdapter<List<Int>> =
            moshi.adapter<List<Int>>(Types.newParameterizedType(List::class.java, Integer::class.java))
                .nonNull()

        if (json == null)
            return Collections.emptyList()
        return jsonAdapter.fromJson(json)
    }

    @TypeConverter
    fun genreNameListToString(genreIds: List<String>?): String {
        val jsonAdapter: JsonAdapter<List<String>> =
            moshi.adapter<List<String>>(Types.newParameterizedType(List::class.java, String::class.java))
                .nonNull()

        if (genreIds == null)
            return jsonAdapter.toJson(Collections.emptyList<String>())
        return jsonAdapter.toJson(genreIds)
    }

    @TypeConverter
    fun stringToGenreNameList(json: String?): List<String>? {
        val jsonAdapter: JsonAdapter<List<String>> =
            moshi.adapter<List<String>>(Types.newParameterizedType(List::class.java, String::class.java))
                .nonNull()

        if (json == null)
            return Collections.emptyList()
        return jsonAdapter.fromJson(json)
    }
}
