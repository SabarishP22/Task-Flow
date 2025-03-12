package com.example.taskflow.repository

import com.example.taskflow.dao.TaskDao
import com.example.taskflow.entity.Task
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDao: TaskDao) {

    fun getAllTasks(): Flowable<List<Task>> = taskDao.getAllTasks()

    fun getFilteredTasks(completed: Boolean): Flowable<List<Task>> = taskDao.getFilteredTasks(completed)

    fun insert(task: Task): Completable = taskDao.insertTask(task)

    fun update(task: Task): Completable = taskDao.updateTask(task)

    fun delete(task: Task): Completable = taskDao.deleteTask(task)

    fun deleteCompletedTasks(): Completable = taskDao.deleteCompletedTasks()
}