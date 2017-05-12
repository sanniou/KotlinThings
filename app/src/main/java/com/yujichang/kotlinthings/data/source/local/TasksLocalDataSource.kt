package com.yujichang.kotlinthings.data.source.local

import android.content.Context
import com.yujichang.kotlinthings.data.Task
import com.yujichang.kotlinthings.data.source.TasksDataSource

/**
 *author : jichang
 *time   : 2017/05/05
 *desc   :
 *具体实现数据源为db。
 *version: 1.0
 */
object TasksLocalDataSource : TasksDataSource {

    lateinit var mDbHelper: TasksDbHelper

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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun completeTask(task: Task) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun completeTask(taskId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun activateTask(task: Task) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun activateTask(taskId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clearCompletedTasks() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun refreshTasks() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllTasks() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteTask(taskId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}