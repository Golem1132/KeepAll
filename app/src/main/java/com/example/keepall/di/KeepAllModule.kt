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
        ).fallbackToDestructiveMigration()
            .build()
}