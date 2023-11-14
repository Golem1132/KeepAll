package com.example.keepall.di

import android.content.Context
import androidx.room.Room
import com.example.keepall.database.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class KeepAllModule {

    @Provides
    @Singleton
    fun provideNoteDao(database: KeepAllDatabase): NoteDao =
        database.noteDao()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): KeepAllDatabase =
        Room.databaseBuilder(
            context,
            KeepAllDatabase::class.java,
            "KeepAllDb"
<<<<<<< HEAD
        ).addMigrations(MIGRATION_6_TO_7, MIGRATION_7_TO_8)
            .fallbackToDestructiveMigration()
=======
        ).addMigrations(
            migration_1_2, migration_2_3, migration_3_4, migration_5_6
        ).fallbackToDestructiveMigration()
>>>>>>> c0771fe0f6a345d4d0ee783de90c4c3187897b93
            .build()
}