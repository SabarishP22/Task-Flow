package com.example.taskflow.di

import android.content.Context
import androidx.room.Room
import com.example.taskflow.dao.TaskDao
import com.example.taskflow.db.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TaskDatabase =
        Room.databaseBuilder(context, TaskDatabase::class.java, "task_database").build()

    @Provides
    fun provideTaskDao(database: TaskDatabase): TaskDao = database.taskDao()

}