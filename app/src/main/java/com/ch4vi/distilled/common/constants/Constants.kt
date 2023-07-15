package com.ch4vi.distilled.common.constants

/**
 * Note: In another scenario, I would move BASE_URL and NETWORK_LOGGING_ENABLED to a different
 * package like debug/java/com... and release/java/com... to have different values depending on
 * the build variant due to the deprecation of the BuildConfig.
 *
 * Also I would put the API_KEY in local.properties file (or on the server side) to avoid including
 * it in the repository
 */
object Constants {
    const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
    const val START_PAGE = 1

    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val NETWORK_LOGGING_ENABLED = true

    const val API_KEY = "25a8f80ba018b52efb64f05140f6b43c"
}
