package com.yujichang.kotlinthings.tasks

import android.support.v4.app.Fragment
import com.yujichang.kotlinthings.data.Task

/**
 *author : jichang
 *time   : 2017/05/03
 *desc   : 喵喵
 *version: 1.0
 */

class TaskFragment : Fragment(),
        TasksContract.View {
    override fun setPresenter(presenter: TasksContract.Presenter) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setLoadingIndicator(active: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showTasks(tasks: List<Task>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showAddTask() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showTaskDetailsUi(taskId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showTaskMarkedComplete() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showTaskMarkedActive() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showCompletedTasksCleared() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoadingTasksError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showNoTasks() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showActiveFilterLabel() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showCompletedFilterLabel() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showAllFilterLabel() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showNoActiveTasks() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showNoCompletedTasks() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showSuccessfullySavedMessage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val isActive: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun showFilteringPopUpMenu() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}