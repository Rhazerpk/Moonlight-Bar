package com.kotlin.moonlightbarapp.di

import android.content.Context
import androidx.room.Room
import com.kotlin.moonlightbarapp.data.local.Database
import com.kotlin.moonlightbarapp.data.remote.DrinkApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
@InstallIn( SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideDrinkApi(moshi: Moshi): DrinkApi {
        return Retrofit.Builder()
            .baseUrl("https://www.thecocktaildb.com")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(DrinkApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDivisionDatabase(@ApplicationContext appContext: Context): Database =
        Room.databaseBuilder(
            appContext,
            Database::class.java,
            "Drink.db")
            .fallbackToDestructiveMigration()
            .build()
    @Provides
    fun providesDivisionDao(db: Database) = db.drinkDao()
}