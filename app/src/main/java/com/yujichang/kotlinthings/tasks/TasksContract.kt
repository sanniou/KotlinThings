package com.yujichang.kotlinthings.tasks

import com.yujichang.kotlinthings.BasePresenter
import com.yujichang.kotlinthings.BaseView
import com.yujichang.kotlinthings.data.Task

/**
 *author : jichang
 *time   : 2017/05/05
 *desc   : 喵喵
 *version: 1.0
 */
interface TasksContract{
    interface View : BaseView<Presenter> {

        fun setLoadingIndicator(active: Boolean)

        fun showTasks(tasks: List<Task>)

        fun showAddTask()

        fun showTaskDetailsUi(taskId: String)

        fun showTaskMarkedComplete()

        fun showTaskMarkedActive()

        fun showCompletedTasksCleared()

        fun showLoadingTasksError()

        fun showNoTasks()

        fun showActiveFilterLabel()

        fun showCompletedFilterLabel()

        fun showAllFilterLabel()

        fun showNoActiveTasks()

        fun showNoCompletedTasks()

        fun showSuccessfullySavedMessage()

        val isActive: Boolean

        fun showFilteringPopUpMenu()
    }

    interface Presenter : BasePresenter {

        fun result(requestCode: Int, resultCode: Int)

        fun loadTasks(forceUpdate: Boolean)

        fun addNewTask()

        fun openTaskDetails(requestedTask: Task)

        fun completeTask(completedTask: Task)

        fun activateTask(activeTask: Task)

        fun clearCompletedTasks()

        var filtering: TasksFilterType
    }
}