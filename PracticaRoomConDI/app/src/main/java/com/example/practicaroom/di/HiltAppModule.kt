package com.example.practicaroom.di

import android.content.Context
import androidx.room3.Room.databaseBuilder
import com.example.practicaroom.db.AppDatabase
import com.example.practicaroom.db.AppDatabase.Companion.DB_NAME
import com.example.practicaroom.db.AppDatabase.Companion.MIGRATION_1_2
import com.example.practicaroom.repositories.ContactRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HiltAppModule {
    @Provides
    @Singleton
    fun provideContactRepository(db: AppDatabase): ContactRepository {
        return ContactRepository(db)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return databaseBuilder<AppDatabase>(context, DB_NAME)
            .addMigrations(MIGRATION_1_2)
            .build()
    }
}