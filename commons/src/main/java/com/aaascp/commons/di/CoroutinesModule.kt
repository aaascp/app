package com.aaascp.commons.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UseCaseDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DispatcherIO

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationScope

@InstallIn(SingletonComponent::class)
@Module
object CoroutinesModule {

    @Provides
    @UseCaseDispatcher
    fun provideUseCaseDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @DispatcherIO
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    @ApplicationScope
    fun provideCoroutineScope() = CoroutineScope(
        SupervisorJob() + CoroutineExceptionHandler { _, exception ->
            Timber.e(exception)
        }
    )
}
