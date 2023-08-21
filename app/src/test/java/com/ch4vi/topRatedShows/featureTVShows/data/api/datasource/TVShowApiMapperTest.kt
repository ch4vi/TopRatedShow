package com.ch4vi.topRatedShows.featureTVShows.data.api.datasource

import com.ch4vi.topRatedShows.featureTVShows.data.api.datasource.TVShowApiMapper.toDomain
import com.ch4vi.topRatedShows.featureTVShows.data.api.model.PageDTO
import com.ch4vi.topRatedShows.featureTVShows.data.api.model.TVShowDTO
import com.ch4vi.topRatedShows.featureTVShows.domain.model.Failure
import com.ch4vi.topRatedShows.featureTVShows.domain.model.Page
import com.ch4vi.topRatedShows.featureTVShows.domain.model.TVShow
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertIs

internal class TVShowApiMapperTest {

    private lateinit var mapper: TVShowApiMapper
    private var moshi: Moshi? = null
    private var pageAdapter: JsonAdapter<PageDTO>? = null
    private var tvShowAdapter: JsonAdapter<TVShowDTO>? = null
    private val apiConstants by lazy { ApiConstants }

    @BeforeTest
    fun setUp() {
        moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        pageAdapter = moshi?.adapter(PageDTO::class.java)
        tvShowAdapter = moshi?.adapter(TVShowDTO::class.java)
        mapper = TVShowApiMapper
    }

    @AfterTest
    fun tearDown() {
        moshi = null
        pageAdapter = null
        tvShowAdapter = null
    }

    @Test
    fun `GIVEN page dto WHEN map THEN domain model is filled`() {
        val dto = pageAdapter?.fromJson(apiConstants.tvShowPageSuccessResponse)
        val output = dto?.toDomain()

        assertIs<PageDTO>(dto)
        assertIs<Page>(output)
    }

    @Test(expected = Failure.MapperFailure::class)
    fun `GIVEN error page dto WHEN map THEN throw MapperFailure`() {
        val dto = pageAdapter?.fromJson(apiConstants.tvShowPageErrorResponse)

        dto?.toDomain()
    }

    @Test
    fun `GIVEN TVShow dto WHEN map THEN domain model is filled`() {
        val dto = tvShowAdapter?.fromJson(apiConstants.tvShowItemSuccessResponse)
        val output = dto?.toDomain()

        assertIs<TVShowDTO>(dto)
        assertIs<TVShow>(output)
    }

    @Test(expected = Failure.MapperFailure::class)
    fun `GIVEN error TVShow dto WHEN map THEN throw MapperFailure`() {
        val dto = tvShowAdapter?.fromJson(apiConstants.tvShowItemErrorResponse)

        dto?.toDomain()
    }
}
