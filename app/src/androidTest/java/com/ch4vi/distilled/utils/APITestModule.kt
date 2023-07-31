package com.ch4vi.distilled.utils

import com.ch4vi.distilled.featureTVShows.data.api.service.TVShowsService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class APITestModule {

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    fun provideRetrofitClient(): Retrofit = RetrofitTestConfiguration().client()

    @Provides
    fun provideTVShowsService(retrofit: Retrofit): TVShowsService =
        retrofit.create(TVShowsService::class.java)
}
