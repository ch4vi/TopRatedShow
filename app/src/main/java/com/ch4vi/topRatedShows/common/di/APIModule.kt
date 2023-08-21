package com.ch4vi.topRatedShows.common.di

import com.ch4vi.topRatedShows.featureTVShows.data.api.RetrofitConfiguration
import com.ch4vi.topRatedShows.featureTVShows.data.api.service.TVShowsService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class APIModule {

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    fun provideRetrofitClient(): Retrofit = RetrofitConfiguration().client()

    @Provides
    fun provideTVShowsService(retrofit: Retrofit): TVShowsService =
        retrofit.create(TVShowsService::class.java)
}
