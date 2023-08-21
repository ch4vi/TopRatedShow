package com.ch4vi.topRatedShows.common.di

import com.ch4vi.topRatedShows.featureTVShows.data.api.datasource.TVShowApiDataSource
import com.ch4vi.topRatedShows.featureTVShows.data.api.service.TVShowsService
import com.ch4vi.topRatedShows.featureTVShows.data.repository.TVShowRepositoryImpl
import com.ch4vi.topRatedShows.featureTVShows.domain.repository.TVShowRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

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
