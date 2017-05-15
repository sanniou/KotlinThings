package com.yujichang.kotlinthings.data.source.remote

import android.os.Handler
import com.google.common.collect.Lists
import com.yujichang.kotlinthings.data.Task
import com.yujichang.kotlinthings.data.source.TasksDataSource
import java.util.*

/**
 *author : jichang
 *time   : 2017/05/07
 *desc   : complete
 * 实现增加延迟模拟网络的数据源
 *version: 1.0
 */
object TaskRemoteDataSource : TasksDataSource {
    init {
        addTask("Build tower in Pisa", "Ground looks good, no foundation work required.")
        addTask("Finish bridge in Tacoma", "Found awesome girders at half the cost!")
    }

    private val SERVICE_LATENCY_IN_MILLIS = 5000L

    private val TASKS_SERVICE_DATA = mutableMapOf<String, Task>()


    fun addTask(title: String, description: String) {
        val newTask = Task(UUID.randomUUID().toString(),title, description)
        TASKS_SERVICE_DATA.put(newTask.id, newTask)
    }

    /**
     *注意：{@link LoadTasksCallback＃onDataNotAvailable（）}从未被触发。在一个真正的远程数据
     *实现中，如果无法联系服务器或服务器，则会触发返回一个错误。
     */
    override fun getTasks(callback: TasksDataSource.LoadTasksCallback) {
        //通过延迟执行来模拟网络
        Handler().postDelayed(
                {
                    callback.onTasksLoaded(Lists.newArrayList(TASKS_SERVICE_DATA.values))
                },
                SERVICE_LATENCY_IN_MILLIS)
    }

    override fun getTask(taskId: String, callback: TasksDataSource.GetTaskCallback) {
        //通过延迟执行来模拟网络
        Handler().postDelayed(
                {
                    callback.onTaskLoaded(TASKS_SERVICE_DATA.getValue(taskId))
                },
                SERVICE_LATENCY_IN_MILLIS)
    }

    override fun saveTask(task: Task) {
        TASKS_SERVICE_DATA.put(task.id, task)
    }

    override fun completeTask(task: Task) {
        TASKS_SERVICE_DATA.put(task.id,
                Task(task.id, task.title, task.description, true))
    }

    override fun completeTask(taskId: String) {
        //不需要远程数据源，因为{@link TasksRepository}处理
        //使用其缓存的数据从{@code taskId}转换为{@link任务}。
    }

    override fun activateTask(task: Task) {
        val activeTask = Task(task.id, task.title, task.description)
        TASKS_SERVICE_DATA.put(task.id, activeTask)
    }

    override fun activateTask(taskId: String) {
        //不需要远程数据源，因为{@link TasksRepository}处理
        //使用其缓存的数据从{@code taskId}转换为{@link任务}。
    }

    override fun clearCompletedTasks() {
        TASKS_SERVICE_DATA.filter { it.value.completed }
                .forEach { k, _ -> TASKS_SERVICE_DATA.remove(k) }
    }

    override fun refreshTasks() {
        //不需要，因为{@link TasksRepository}处理刷新的逻辑
        //所有可用数据源的任务。
    }

    override fun deleteAllTasks() {
        TASKS_SERVICE_DATA.clear()
    }

    override fun deleteTask(taskId: String) {
        TASKS_SERVICE_DATA.remove(taskId)
    }
}