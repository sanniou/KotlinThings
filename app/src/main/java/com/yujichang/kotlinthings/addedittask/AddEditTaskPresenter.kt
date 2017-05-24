package com.yujichang.kotlinthings.addedittask

import com.yujichang.kotlinthings.data.Task
import com.yujichang.kotlinthings.data.source.TasksDataSource

/**
 *author : jichang
 *time   : 2017/05/15
 *desc   : complete
 *从UI听到用户操作（{@link AddEditTaskFragment}），检索数据和更新
 *根据需要的UI。
 *version: 1.0
 */
class AddEditTaskPresenter(val mTaskId: String?, val mTasksRepository: TasksDataSource,
                           val mAddTaskView: AddEditTaskContract.View,
                           var mIsDataMissing: Boolean)
    : AddEditTaskContract.Presenter
        , TasksDataSource.GetTaskCallback {

    init {
        mAddTaskView.setPresenter(this)
    }

    override fun start() {
        if (!isNewTask() && mIsDataMissing) {
            populateTask()
        }
    }

    override fun saveTask(title: String, description: String) {
        if (isNewTask()) {
            createTask(title, description)
        } else {
            updateTask(title, description)
        }
    }

    override fun populateTask() {
        if (isNewTask()) {
            throw RuntimeException("populateTask() was called but task is new.")
        }
        mTasksRepository.getTask(mTaskId!!, this)//TODO 不够智能的smart case ，先用 !! 来是使用
    }

    override val isDataMissing: Boolean get() = mIsDataMissing

    override fun onTaskLoaded(task: Task) {
        // 该视图可能无法处理UI更新
        if (mAddTaskView.isActive) {
            mAddTaskView.setTitle(task.title)
            mAddTaskView.setDescription(task.description)
        }
        mIsDataMissing = false
    }

    override fun onDataNotAvailable() {
        // 该视图可能无法处理UI更新
        if (mAddTaskView.isActive) {
            mAddTaskView.showEmptyTaskError()
        }
    }

    private fun isNewTask(): Boolean {
        return mTaskId == null//移植于原java代码，由于kotlin的null safe特性，总是false
    }

    private fun createTask(title: String, description: String) {
        val newTask = Task(title, description)
        if (newTask.isEmpty()) {
            mAddTaskView.showEmptyTaskError()
        } else {
            mTasksRepository.saveTask(newTask)
            mAddTaskView.showTasksList()
        }
    }

    private fun updateTask(title: String, description: String) {
        if (isNewTask()) {
            throw RuntimeException("updateTask() was called but task is new.")
        }
        mTasksRepository.saveTask(Task(title, description, mTaskId!!))//TODO 不够智能的smart case ，先用 !! 来是使用
        mAddTaskView.showTasksList() // After an edit, go back to the list.
    }
}