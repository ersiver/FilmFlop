package com.ersiver.filmflop.utils

import org.mockito.Mockito

/**
 * Kotlin friendly mock that handles generics.
 */
inline fun <reified T> mock(): T = Mockito.mock(T::class.java)