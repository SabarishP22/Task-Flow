package com.example.taskflow.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.taskflow.entity.Task
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: Task): Completable

    @Update
    fun updateTask(task: Task): Completable

    @Delete
    fun deleteTask(task: Task): Completable

    @Query("select * from  tasks order by createdAt desc")
    fun getAllTasks(): Flowable<List<Task>>

    @Query("select * from tasks where isCompleted = :completed order by createdAt desc")
    fun getFilteredTasks(completed: Boolean): Flowable<List<Task>>

    @Query("delete from tasks where isCompleted = 1")
    fun deleteCompletedTasks(): Completable
}