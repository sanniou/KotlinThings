package com.yujichang.kotlinthings.data.source.local

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.yujichang.kotlinthings.data.Task
import com.yujichang.kotlinthings.data.source.TasksDataSource

/**
 *author : jichang
 *time   : 2017/05/05
 *desc   :complete
 *具体实现数据源为db。
 *version: 1.0
 */
object TasksLocalDataSource : TasksDataSource {

    private  lateinit var mDbHelper: TasksDbHelper

    fun getInstance(context: Context): TasksLocalDataSource {
        mDbHelper = TasksDbHelper(context)
        return this
    }

    override fun getTasks(callback: TasksDataSource.LoadTasksCallback) {

        val projection = arrayOf(TasksPersistenceContract.TaskEntry.COLUMN_NAME_ENTRY_ID,
                TasksPersistenceContract.TaskEntry.COLUMN_NAME_TITLE,
                TasksPersistenceContract.TaskEntry.COLUMN_NAME_DESCRIPTION,
                TasksPersistenceContract.TaskEntry.COLUMN_NAME_COMPLETED)

        val tasks = mutableListOf<Task>()

        mDbHelper.readableDatabase.use {
            it.query(
                    TasksPersistenceContract.TaskEntry.TABLE_NAME,
                    projection, null, null, null, null, null).use {
                while (it.moveToNext()) {
                    val itemId = it.getString(it.getColumnIndexOrThrow(TasksPersistenceContract.TaskEntry.COLUMN_NAME_ENTRY_ID))
                    val title = it.getString(it.getColumnIndexOrThrow(TasksPersistenceContract.TaskEntry.COLUMN_NAME_TITLE))
                    val description = it.getString(it.getColumnIndexOrThrow(TasksPersistenceContract.TaskEntry.COLUMN_NAME_DESCRIPTION))
                    val completed = it.getInt(it.getColumnIndexOrThrow(TasksPersistenceContract.TaskEntry.COLUMN_NAME_COMPLETED)) == 1
                    val task = Task(title, description, itemId, completed)
                    tasks.add(task)
                }
            }
        }

        if (tasks.isEmpty()) {
            // 如果表是新的或是空的，这将被调用
            callback.onDataNotAvailable()
        } else {
            callback.onTasksLoaded(tasks)
        }
    }

    override fun getTask(taskId: String, callback: TasksDataSource.GetTaskCallback) {

        val projection = arrayOf(TasksPersistenceContract.TaskEntry.COLUMN_NAME_ENTRY_ID,
                TasksPersistenceContract.TaskEntry.COLUMN_NAME_TITLE,
                TasksPersistenceContract.TaskEntry.COLUMN_NAME_DESCRIPTION,
                TasksPersistenceContract.TaskEntry.COLUMN_NAME_COMPLETED)

        val selection = TasksPersistenceContract.TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?"
        val selectionArgs = arrayOf(taskId)

        var task: Task? = null
        mDbHelper.readableDatabase
                .use {
                    it.query(
                            TasksPersistenceContract.TaskEntry.TABLE_NAME,
                            projection, selection, selectionArgs, null, null, null)
                            .use {
                                if (it.count > 0) {
                                    it.moveToFirst()
                                    val itemId = it.getString(
                                            it.getColumnIndexOrThrow(TasksPersistenceContract.TaskEntry.COLUMN_NAME_ENTRY_ID))
                                    val title = it.getString(
                                            it.getColumnIndexOrThrow(TasksPersistenceContract.TaskEntry.COLUMN_NAME_TITLE))
                                    val description = it.getString(
                                            it.getColumnIndexOrThrow(TasksPersistenceContract.TaskEntry.COLUMN_NAME_DESCRIPTION))
                                    val completed = it.getInt(
                                            it.getColumnIndexOrThrow(TasksPersistenceContract.TaskEntry.COLUMN_NAME_COMPLETED)) == 1
                                    task = Task(title, description, itemId, completed)
                                }
                            }
                }


        if (task != null) {
            callback.onTaskLoaded(task as Task)
        } else {
            callback.onDataNotAvailable()
        }
    }

    override fun saveTask(task: Task) {

        val values = ContentValues()
        values.put(TasksPersistenceContract.TaskEntry.COLUMN_NAME_ENTRY_ID, task.id)
        values.put(TasksPersistenceContract.TaskEntry.COLUMN_NAME_TITLE, task.title)
        values.put(TasksPersistenceContract.TaskEntry.COLUMN_NAME_DESCRIPTION, task.description)
        values.put(TasksPersistenceContract.TaskEntry.COLUMN_NAME_COMPLETED, task.completed)

        mDbHelper.writableDatabase
                .use {
                    val insert = it.insert(TasksPersistenceContract.TaskEntry.TABLE_NAME, null, values)
                }
    }

    override fun completeTask(task: Task) {

        val values = ContentValues()
        values.put(TasksPersistenceContract.TaskEntry.COLUMN_NAME_COMPLETED, true)

        val selection = TasksPersistenceContract.TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?"
        val selectionArgs = arrayOf(task.id)

        mDbHelper.writableDatabase
                .use {
                    it.update(TasksPersistenceContract.TaskEntry.TABLE_NAME, values, selection, selectionArgs)
                }
    }

    override fun completeTask(taskId: String) {
        // 由于{@link TasksRepository}处理，本地数据源不需要
        // 使用其缓存的数据从{@code taskId}转换为{@link任务}。
    }

    override fun activateTask(task: Task) {
        val values = ContentValues()
        values.put(TasksPersistenceContract.TaskEntry.COLUMN_NAME_COMPLETED, false)

        val selection = TasksPersistenceContract.TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?"
        val selectionArgs = arrayOf(task.id)
        mDbHelper.writableDatabase
                .use {
                    it.update(TasksPersistenceContract.TaskEntry.TABLE_NAME, values, selection, selectionArgs)
                }
    }

    override fun activateTask(taskId: String) {
        // 由于{@link TasksRepository}处理，本地数据源不需要
        // 使用其缓存的数据从{@code taskId}转换为{@link任务}。
    }

    override fun clearCompletedTasks() {
        val selection = TasksPersistenceContract.TaskEntry.COLUMN_NAME_COMPLETED + " LIKE ?"
        val selectionArgs = arrayOf("1")
        mDbHelper.writableDatabase
                .use {
                    it.delete(TasksPersistenceContract.TaskEntry.TABLE_NAME, selection, selectionArgs)
                }
    }

    override fun refreshTasks() {
        // 不需要，因为{@link TasksRepository}处理刷新的逻辑
        // 来自所有可用数据源的任务。
    }

    override fun deleteAllTasks() {
        mDbHelper.writableDatabase
                .use {
                    it.delete(TasksPersistenceContract.TaskEntry.TABLE_NAME, null, null)
                }
    }

    override fun deleteTask(taskId: String) {
        val selection = TasksPersistenceContract.TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?"
        val selectionArgs = arrayOf(taskId)
        mDbHelper.writableDatabase
                .use {
                    it.delete(TasksPersistenceContract.TaskEntry.TABLE_NAME, selection, selectionArgs)
                }
    }


}