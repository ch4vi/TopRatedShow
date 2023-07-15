package com.ch4vi.distilled.common.di

import com.ch4vi.distilled.featureTVShows.data.api.RetrofitConfiguration
import com.ch4vi.distilled.featureTVShows.data.api.datasource.TVShowApiDataSource
import com.ch4vi.distilled.featureTVShows.data.api.service.TVShowsService
import com.ch4vi.distilled.featureTVShows.data.repository.TVShowRepositoryImpl
import com.ch4vi.distilled.featureTVShows.domain.repository.TVShowRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    fun provideRetrofitClient(): Retrofit = RetrofitConfiguration().client()

    @Provides
    fun provideTVShowsService(retrofit: Retrofit): TVShowsService =
        retrofit.create(TVShowsService::class.java)

    @Provides
    fun provideTVShowApiDataSource(service: TVShowsService): TVShowApiDataSource {
        return TVShowApiDataSource(service)
    }
}

@Module
@InstallIn(SingletonComponent::class)
interface BindingModule {

    @Binds
    fun bindsTVShowRepository(repository: TVShowRepositoryImpl): TVShowRepository
}
