package com.yujichang.kotlinthings.taskdetail

import com.google.common.base.Strings
import com.yujichang.kotlinthings.data.Task
import com.yujichang.kotlinthings.data.source.TasksDataSource
import com.yujichang.kotlinthings.data.source.TasksRepository

/**
 *author : jichang
 *time   : 2017/05/16
 *desc   : 喵喵
 *从UI听到用户操作（{@link TaskDetailFragment}），检索数据和更新
 *根据需要的UI。
 *version: 1.0
 */
class TaskDetailPresenter(val mTaskId: String,
                          val mTasksRepository: TasksRepository,
                          val mTaskDetailView: TaskDetailContract.View)
    : TaskDetailContract.Presenter {
    init {
        mTaskDetailView.setPresenter(this)
    }

    override fun start() {
        openTask()
    }


    private fun openTask() {
        if (Strings.isNullOrEmpty(mTaskId)) {
            mTaskDetailView.showMissingTask()
            return
        }

        mTaskDetailView.setLoadingIndicator(true)
        mTasksRepository.getTask(mTaskId, object : TasksDataSource.GetTaskCallback {
            override fun onTaskLoaded(task: Task) {
                // 该视图可能无法处理UI更新
                if (!mTaskDetailView.isActive) {
                    return
                }
                mTaskDetailView.setLoadingIndicator(false)
                if (task.available()) {
                    mTaskDetailView.showMissingTask()
                } else {
                    showTask(task)
                }
                showTask(task)
            }

            override fun onDataNotAvailable() {
                // 该视图可能无法处理UI更新
                if (!mTaskDetailView.isActive) {
                    return
                }
                mTaskDetailView.showMissingTask()
            }
        })
    }


    override fun editTask() {
        if (mTaskId.isEmpty()) {
            mTaskDetailView.showMissingTask()
            return
        }
        mTaskDetailView.showEditTask(mTaskId)
    }

    override fun deleteTask() {
        if (mTaskId.isEmpty()) {
            mTaskDetailView.showMissingTask()
            return
        }
        mTasksRepository.deleteTask(mTaskId)
        mTaskDetailView.showTaskDeleted()
    }

    override fun completeTask() {
        if (mTaskId.isEmpty()) {
            mTaskDetailView.showMissingTask()
            return
        }
        mTasksRepository.completeTask(mTaskId)
        mTaskDetailView.showTaskMarkedComplete()
    }

    override fun activateTask() {
        if (mTaskId.isEmpty()) {
            mTaskDetailView.showMissingTask()
            return
        }
        mTasksRepository.activateTask(mTaskId)
        mTaskDetailView.showTaskMarkedActive()
    }


    private fun showTask(task: Task) {
        val title = task.title
        val description = task.description

        if (title.isEmpty()) {
            mTaskDetailView.hideTitle()
        } else {
            mTaskDetailView.showTitle(title)
        }

        if (description.isEmpty()) {
            mTaskDetailView.hideDescription()
        } else {
            mTaskDetailView.showDescription(description)
        }
        mTaskDetailView.showCompletionStatus(task.completed)
    }
}