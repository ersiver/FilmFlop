package com.ersiver.filmflop.util

import android.net.Uri
import androidx.core.net.toUri
import java.util.*

/**
 * Helper function used to cut full movie's release date
 * to the year only and surround it by paranthesis.
 */
fun String.surroundedWithParenthesis(): String {
    return "(" + if (this.length > 4) this.substring(0, 4) + ")" else "$this)"
}

/**
 * Helper function to append the [IMAGE_URL] with a path to the poster.
 */
fun String.concatUrl(): Uri {
    return (IMAGE_URL + this).toUri().buildUpon().scheme("https").build()
}

/**
 * Helper function to the concat [TRAILER_URL] with a path.
 */
fun String.asTrailerURL(): String{
return TRAILER_URL+this
}

/**
 * Helper function to build a String of genres separated
 * by comas and with the first word capitalized.
 */
@ExperimentalStdlibApi
fun List<String>.asStringWithComas(): String {
    var toBeReturned = ""
    for (genre in this) {
        toBeReturned += genre.toLowerCase(Locale.ROOT)

        if (this.indexOf(genre) == lastIndex)
            break

        toBeReturned += ", "
    }
    return toBeReturned.capitalize(Locale.ROOT)
}