package com.example.keepall.di

import androidx.room.Room
import com.example.keepall.database.KeepAllDatabase
import com.example.keepall.database.MIGRATION_6_TO_7
import com.example.keepall.database.MIGRATION_7_TO_8
import com.example.keepall.screens.homescreen.HomeViewModel
import com.example.keepall.screens.notescreen.NoteViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val keepAllModules = module {

    //provide database instance
    single {
        Room.databaseBuilder(
            androidContext(),
            KeepAllDatabase::class.java,
            "KeepAllDatabase"
        ).addMigrations(MIGRATION_6_TO_7, MIGRATION_7_TO_8)
            .fallbackToDestructiveMigration()
            .build()
    }

    //provide NoteDao
    single {
        get<KeepAllDatabase>().noteDao()
    }


    viewModel {
        HomeViewModel(get())
    }

    viewModel {
        NoteViewModel(get(), get())
    }


}