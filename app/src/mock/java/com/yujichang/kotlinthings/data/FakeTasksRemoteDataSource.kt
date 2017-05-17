package com.yujichang.kotlinthings.data

import android.support.annotation.VisibleForTesting
import com.google.common.collect.Lists
import com.yujichang.kotlinthings.data.source.TasksDataSource
import java.util.LinkedHashMap

/**
 *author : jichang
 *time   : 2017/05/05
 *desc   : complete
 * 实现具有静态访问数据的远程数据源，便于测试。
 *version: 1.0
 */
object FakeTasksRemoteDataSource :TasksDataSource{

    private val TASKS_SERVICE_DATA = LinkedHashMap<String, Task>().withDefault { Task("", "", "", false) }



    fun getInstance(): FakeTasksRemoteDataSource {
        return this
    }

    override fun getTasks(callback: TasksDataSource.LoadTasksCallback) {
        callback.onTasksLoaded(Lists.newArrayList(TASKS_SERVICE_DATA.values))
    }

    override fun getTask(taskId: String, callback: TasksDataSource.GetTaskCallback) {
        val task = TASKS_SERVICE_DATA.getValue(taskId)
        callback.onTaskLoaded(task)
    }

    override fun saveTask(task: Task) {
        TASKS_SERVICE_DATA.put(task.id, task)
    }

    override fun completeTask(task: Task) {
        val completedTask = Task(task.id, task.title, task.description, true)
        TASKS_SERVICE_DATA.put(task.id, completedTask)
    }

    override fun completeTask(taskId: String) {
        // Not required for the remote data source.
    }

    override fun activateTask(task: Task) {
        val activeTask = Task(task.id, task.title, task.description)
        TASKS_SERVICE_DATA.put(task.id, activeTask)
    }

    override fun activateTask(taskId: String) {
        // Not required for the remote data source.
    }

    override fun clearCompletedTasks() {
        val it = TASKS_SERVICE_DATA.entries.iterator()
        while (it.hasNext()) {
            val entry = it.next()
            if (entry.value.completed) {
                it.remove()
            }
        }
    }

    override fun refreshTasks() {
        // Not required because the {@link TasksRepository} handles the logic of refreshing the
        // tasks from all the unavailable data sources.
    }

    override fun deleteTask(taskId: String) {
        TASKS_SERVICE_DATA.remove(taskId)
    }

    override fun deleteAllTasks() {
        TASKS_SERVICE_DATA.clear()
    }

    @VisibleForTesting
    fun addTasks(vararg tasks: Task) {
        for (task in tasks) {
            TASKS_SERVICE_DATA.put(task.id, task)
        }
    }
}
