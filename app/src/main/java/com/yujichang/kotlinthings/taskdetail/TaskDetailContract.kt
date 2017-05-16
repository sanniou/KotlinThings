package com.yujichang.kotlinthings.taskdetail

import com.yujichang.kotlinthings.BasePresenter
import com.yujichang.kotlinthings.BaseView

/**
 *author : jichang
 *time   : 2017/05/16
 *desc   : complete
 *version: 1.0
 */

interface TaskDetailContract {
    interface View : BaseView<Presenter> {

        fun setLoadingIndicator(active: Boolean)

        fun showMissingTask()

        fun hideTitle()

        fun showTitle(title: String)

        fun hideDescription()

        fun showDescription(description: String)

        fun showCompletionStatus(complete: Boolean)

        fun showEditTask(taskId: String)

        fun showTaskDeleted()

        fun showTaskMarkedComplete()

        fun showTaskMarkedActive()

        val isActive: Boolean
    }

    interface Presenter : BasePresenter {

        fun editTask()

        fun deleteTask()

        fun completeTask()

        fun activateTask()
    }
}