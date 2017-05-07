package com.yujichang.kotlinthings.data.source

import com.yujichang.kotlinthings.data.Task

/**
 *author : jichang
 *time   : 2017/05/05
 *desc   :complete
 **访问任务数据的主入口点。
 *为了简单起见，只有getTasks（）和getTask（）有回调。考虑将回调添加到其他
 *通知用户网络/数据库错误或成功操作的方法。
 *例如，当创建新任务时，它同步地存储在缓存中，但通常是每个
 *数据库或网络上的操作应该以不同的线程执行。
 *version: 1.0
 */
interface TasksDataSource {
    interface LoadTasksCallback {

        fun onTasksLoaded(tasks: List<Task>)

        fun onDataNotAvailable()
    }

    interface GetTaskCallback {

        fun onTaskLoaded(task: Task)

        fun onDataNotAvailable()
    }

    fun getTasks(callback: LoadTasksCallback)

     fun getTask(taskId: String, callback: GetTaskCallback)

     fun saveTask(task: Task)

     fun completeTask(task: Task)

     fun completeTask(taskId: String)

     fun activateTask(task: Task)

     fun activateTask(taskId: String)

     fun clearCompletedTasks()

     fun refreshTasks()

     fun deleteAllTasks()

     fun deleteTask(taskId: String)
}