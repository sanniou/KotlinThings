package com.yujichang.kotlinthings.tasks

import com.yujichang.kotlinthings.data.Task
import com.yujichang.kotlinthings.data.source.TasksRepository

/**
 *author : jichang
 *time   : 2017/05/05
 *desc   : 喵喵
 *version: 1.0
 */
class TasksPresenter(tasksRepository: TasksRepository, tasksView: TasksContract.View) : TasksContract.Presenter {
    override fun start() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun result(requestCode: Int, resultCode: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadTasks(forceUpdate: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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