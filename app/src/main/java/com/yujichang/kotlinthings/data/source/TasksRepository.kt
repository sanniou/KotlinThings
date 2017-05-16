package com.yujichang.kotlinthings.data.source

import android.util.Log
import com.yujichang.kotlinthings.data.Task

/**
 *author : jichang
 *time   : 2017/05/05
 *desc   : complete
 * 将任务从数据源加载到缓存中的具体实现。
 * <p>
 *为了简单起见，这将实现本地持久数据和数据之间的哑同步
 *从服务器获取，只有当本地数据库没有使用远程数据源时
 *存在或是空的
 *version: 1.0
 */
object TasksRepository : TasksDataSource {

    /**
     *将缓存标记为无效，以便在下次请求数据时强制进行更新。这个变量
     *具有包的本地可见性，可以从测试中访问。
     */
    var mCacheIsDirty = false
    /**
     * 该变量具有包本地可见性，因此可以从测试中访问。
     */
    var mCachedTasks = mutableMapOf<String, Task>().withDefault { Task("", "", "", false) }

    lateinit var mTasksLocalDataSource: TasksDataSource

    lateinit var mTasksRemoteDataSource: TasksDataSource

    fun getInstance(tasksRemoteDataSource: TasksDataSource,
                    tasksLocalDataSource: TasksDataSource): TasksRepository {
        this.mTasksLocalDataSource = tasksLocalDataSource
        this.mTasksRemoteDataSource = tasksRemoteDataSource
        return this
    }


    /**
     *从缓存，本地数据源（SQLite）或远程数据源获取任务，取其中之一
     *先到先得
     *注意：
     *{ @link LoadTasksCallback＃onDataNotAvailable（） }如果所有数据源都失败获取数据触发。
     */
    override fun getTasks(callback: TasksDataSource.LoadTasksCallback) {
        // 立即返回缓存，如果没被标记弃用
        if (!mCacheIsDirty) {
            callback.onTasksLoaded(ArrayList(mCachedTasks.values))
            return
            /*//mCachedTasks ==null
            //查询本地存储（如果可用）。如果没有，请查询网络。
            mTasksLocalDataSource.getTasks(object : TasksDataSource.LoadTasksCallback() {
                override fun onTasksLoaded(tasks: List<Task>) {
                    refreshCache(tasks)
                    callback.onTasksLoaded(ArrayList(mCachedTasks.values))
                }

                override fun onDataNotAvailable() {
                    getTasksFromRemoteDataSource(callback)
                }
            })
            */
        }
        //如果缓存是脏的，我们需要从网络中获取新的数据。
        getTasksFromRemoteDataSource(callback)
    }

    /**
     *从本地数据源（sqlite）获取任务，除非该表是新的或空的。在这种情况下
     *使用网络数据源。这样做是为了简化样本。
     * <p>
     * Note: {@link GetTaskCallback#onDataNotAvailable()} is fired if both data sources fail to
     * get the data.
     */
    override fun getTask(taskId: String, callback: TasksDataSource.GetTaskCallback) {
        val cachedTask = getTaskWithId(taskId)
        // 立即回应缓存（如果有）
        if (cachedTask.id == "null") {
            callback.onTaskLoaded(cachedTask)
            return
        }
        //从服务器加载/需要时保留。

        //任务是否在本地数据源中？如果没有，请查询网络。
        mTasksLocalDataSource.getTask(taskId, object : TasksDataSource.GetTaskCallback {
            override fun onTaskLoaded(task: Task) {
                // 在内存缓存更新中保持应用UI更新
                mCachedTasks.put(task.id, task)
                callback.onTaskLoaded(task)
            }

            override fun onDataNotAvailable() {
                mTasksRemoteDataSource.getTask(taskId, object : TasksDataSource.GetTaskCallback {
                    override fun onTaskLoaded(task: Task) {
                        // 在内存缓存更新中保持应用UI更新
                        mCachedTasks.put(task.id, task)
                        callback.onTaskLoaded(task)
                    }

                    override fun onDataNotAvailable() {
                        callback.onDataNotAvailable()
                    }
                })
            }
        })
    }

    override fun saveTask(task: Task) {
        mTasksRemoteDataSource.saveTask(task)
        mTasksLocalDataSource.saveTask(task)

        // 在内存缓存更新中保持应用UI更新
        mCachedTasks.put(task.id, task)
    }

    override fun completeTask(task: Task) {
        mTasksRemoteDataSource.completeTask(task)
        mTasksLocalDataSource.completeTask(task)

        val completedTask = Task(task.id, task.title, task.description, true)

        //在内存缓存更新中保持应用UI更新
        mCachedTasks.put(task.id, completedTask)
    }

    override fun completeTask(taskId: String) {
        completeTask(getTaskWithId(taskId))
    }

    override fun activateTask(task: Task) {
        mTasksRemoteDataSource.activateTask(task)
        mTasksLocalDataSource.activateTask(task)

        val activeTask = Task(task.id, task.title, task.description)

        // 在内存缓存更新中保持应用UI更新
        mCachedTasks.put(task.id, activeTask)
    }

    override fun activateTask(taskId: String) {
        activateTask(getTaskWithId(taskId))
    }

    override fun clearCompletedTasks() {
        mTasksRemoteDataSource.clearCompletedTasks()
        mTasksLocalDataSource.clearCompletedTasks()
        // 在内存缓存更新中保持应用UI更新
        mCachedTasks.filter { it.value.completed }
                .forEach { k, _ -> mCachedTasks.remove(k) }
    }

    override fun refreshTasks() {
        mCacheIsDirty = true
    }

    override fun deleteAllTasks() {
        mTasksRemoteDataSource.deleteAllTasks()
        mTasksLocalDataSource.deleteAllTasks()
        mCachedTasks.clear()
    }

    override fun deleteTask(taskId: String) {
        mTasksRemoteDataSource.deleteTask(checkNotNull(taskId))
        mTasksLocalDataSource.deleteTask(checkNotNull(taskId))
        mCachedTasks.remove(taskId)
    }

    private fun getTasksFromRemoteDataSource(callback: TasksDataSource.LoadTasksCallback) {
        mTasksRemoteDataSource.getTasks(object : TasksDataSource.LoadTasksCallback {
            override fun onTasksLoaded(tasks: List<Task>) {
                refreshCache(tasks)
                refreshLocalDataSource(tasks)
                callback.onTasksLoaded(ArrayList(mCachedTasks.values))
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    private fun refreshCache(tasks: List<Task>) {
        mCachedTasks.clear()
        tasks.forEach {
            mCachedTasks.put(it.id, it)
        }
        mCacheIsDirty = false
    }

    private fun refreshLocalDataSource(tasks: List<Task>) {
        mTasksLocalDataSource.deleteAllTasks()
        tasks.forEach {
            mTasksLocalDataSource.saveTask(it)
        }
    }

    private fun getTaskWithId(id: String): Task {
        return mCachedTasks.getValue(id)

    }

}