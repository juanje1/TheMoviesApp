@file:Suppress("unused")
package com.juanje.themoviesapp

import android.content.Context
import com.juanje.domain.IoDispatcher
import com.juanje.domain.MainDispatcher
import com.juanje.themoviesapp.common.AppIdlingResource
import com.juanje.themoviesapp.common.ConnectivityObserver
import com.juanje.themoviesapp.common.EspressoAppIdlingResource
import com.juanje.themoviesapp.common.NetworkConnectivityObserver
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    companion object {
        @Provides
        @Singleton
        @Named("apiKey")
        fun apiKeyProvider(@ApplicationContext applicationContext: Context): String =
            applicationContext.getString(R.string.api_key)

        @Provides
        @Singleton
        @MainDispatcher
        fun mainDispatcherProvider(): CoroutineDispatcher =
            Dispatchers.Main

        @Provides
        @Singleton
        @IoDispatcher
        fun ioDispatcherProvider(): CoroutineDispatcher =
            Dispatchers.IO

        @Provides
        @Singleton
        @Named("baseUrl")
        fun baseUrlProvider(@ApplicationContext applicationContext: Context): String =
            applicationContext.getString(R.string.base_url)

        @Provides
        @Singleton
        @Named("databaseName")
        fun databaseNameProvider(@ApplicationContext applicationContext: Context): String =
            applicationContext.getString(R.string.database_name)
    }

    @Binds
    @Singleton
    abstract fun bindIdlingResource(espressoAppIdlingResource: EspressoAppIdlingResource): AppIdlingResource

    @Binds
    @Singleton
    abstract fun bindConnectivityObserver(networkConnectivityObserver: NetworkConnectivityObserver): ConnectivityObserver
}