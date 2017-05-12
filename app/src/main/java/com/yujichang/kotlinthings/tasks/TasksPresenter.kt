package com.yujichang.kotlinthings.tasks

import android.app.Activity
import com.yujichang.kotlinthings.addedittask.AddEditTaskActivity
import com.yujichang.kotlinthings.data.Task
import com.yujichang.kotlinthings.data.source.TasksDataSource
import com.yujichang.kotlinthings.data.source.TasksRepository
import com.yujichang.kotlinthings.util.EspressoIdlingResource
import java.util.*

/**
 *author : jichang
 *time   : 2017/05/05
 *desc   : 喵喵
 *version: 1.0
 */
class TasksPresenter(val mTasksRepository: TasksRepository, val mTasksView: TasksContract.View) : TasksContract.Presenter {

    private var mFirstLoad = true

    private var mCurrentFiltering = TasksFilterType.ALL_TASKS

    init {
        mTasksView.setPresenter(this)
    }

    override fun start() {
        loadTasks(false)
    }

    override fun result(requestCode: Int, resultCode: Int) {
        // 如果一个任务成功添加，显示snackbar
        if (AddEditTaskActivity.REQUEST_ADD_TASK == requestCode && Activity.RESULT_OK == resultCode) {
            mTasksView.showSuccessfullySavedMessage()
        }
    }

    override fun loadTasks(forceUpdate: Boolean) {
        // 简化样本：网络重新加载将第一次加载强制进行。
        loadTasks(forceUpdate || mFirstLoad, true)
        mFirstLoad = false
    }

    /**
     * @param forceUpdate 传递真实的刷新数据的链接tasksdatasource } { @
     * @param showLoadingUI 通过则显示在UI加载图标
     */
    fun loadTasks(forceUpdate: Boolean, showLoadingUI: Boolean) {
        if (showLoadingUI) {
            mTasksView.setLoadingIndicator(true)
        }
        if (forceUpdate) {
            mTasksRepository.refreshTasks()
        }

        // 网络请求可以在不同的线程中处理，确保Espresso知道
        // 该应用程序忙，直到响应处理。
        EspressoIdlingResource.increment() // App is busy until further notice

        mTasksRepository.getTasks(object : TasksDataSource.LoadTasksCallback {
            override fun onDataNotAvailable() {
                // view 可能无法处理UI更新了
                if (!mTasksView.isActive) {
                    return
                }
                mTasksView.showLoadingTasksError()
            }

            override fun onTasksLoaded(tasks: List<Task>) {
                val tasksToShow = ArrayList<Task>()

                //此回调可被调用两次，一次用于缓存，一次用于加载
                //数据从服务器API，所以我们检查在递减，否则
                //它抛出“计数器已损坏！”异常。

                if (!EspressoIdlingResource.getIdlingResource().isIdleNow) {
                    EspressoIdlingResource.decrement() // Set app as idle.
                }

                // We filter the tasks based on the requestType
                for (task in tasks) {
                    when (mCurrentFiltering) {
                        TasksFilterType.ALL_TASKS -> tasksToShow.add(task)
                        TasksFilterType.ACTIVE_TASKS -> if (task.isActive()) {
                            tasksToShow.add(task)
                        }
                        TasksFilterType.COMPLETED_TASKS -> if (task.completed) {
                            tasksToShow.add(task)
                        }
                        else -> tasksToShow.add(task)
                    }
                }

                //TODO
            }

        })
    }

    override fun addNewTask() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun openTaskDetails(requestedTask: Task) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun completeTask(completedTask: Task) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun activateTask(activeTask: Task) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clearCompletedTasks() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override var filtering: TasksFilterType
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}

}