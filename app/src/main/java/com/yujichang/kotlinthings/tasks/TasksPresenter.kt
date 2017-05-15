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
 *desc   : complete
 *version: 1.0
 */
class TasksPresenter(val mTasksRepository: TasksRepository, val mTasksView: TasksContract.View) : TasksContract.Presenter {

    private var mFirstLoad = true

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

                // 我们基于requestType筛选tasks
                for (task in tasks) {
                    when (filtering) {
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

                // 该视图可能无法处理UI更新了
                if (!mTasksView.isActive) {
                    return
                }
                if (showLoadingUI) {
                    mTasksView.setLoadingIndicator(false)
                }

                processTasks(tasksToShow)
            }

        })
    }

    private fun processTasks(tasks: List<Task>) {
        if (tasks.isEmpty()) {
            //显示指示该筛选器类型没有任务的消息。
            processEmptyTasks()
        } else {
            // 显示任务列表
            mTasksView.showTasks(tasks)
            // 设置筛选器标签的文本。
            showFilterLabel()
        }
    }

    private fun showFilterLabel() {
        when (filtering) {
            TasksFilterType.ACTIVE_TASKS -> mTasksView.showActiveFilterLabel()
            TasksFilterType.COMPLETED_TASKS -> mTasksView.showCompletedFilterLabel()
            else -> mTasksView.showAllFilterLabel()
        }
    }

    private fun processEmptyTasks() {
        when (filtering) {
            TasksFilterType.ACTIVE_TASKS -> mTasksView.showNoActiveTasks()
            TasksFilterType.COMPLETED_TASKS -> mTasksView.showNoCompletedTasks()
            else -> mTasksView.showNoTasks()
        }
    }


    override fun addNewTask() {
        mTasksView.showAddTask()
    }

    override fun openTaskDetails(requestedTask: Task) {
        mTasksView.showTaskDetailsUi(requestedTask.id)
    }

    override fun completeTask(completedTask: Task) {
        mTasksRepository.completeTask(completedTask)
        mTasksView.showTaskMarkedComplete()
        loadTasks(false, false)
    }

    override fun activateTask(activeTask: Task) {
        mTasksRepository.activateTask(activeTask)
        mTasksView.showTaskMarkedActive()
        loadTasks(false, false)
    }

    override fun clearCompletedTasks() {
        mTasksRepository.clearCompletedTasks()
        mTasksView.showCompletedTasksCleared()
        loadTasks(false, false)
    }

    /**
     * 设置当前任务筛选类型。
     *
     * 可以是{@link TasksFilterType#ALL_TASKS},
     * {@link TasksFilterType#COMPLETED_TASKS},
     * {@link TasksFilterType#ACTIVE_TASKS}
     */
    override var filtering = TasksFilterType.ALL_TASKS

}