package com.yujichang.kotlinthings.addedittask

import com.yujichang.kotlinthings.BasePresenter
import com.yujichang.kotlinthings.BaseView

/**
 *author : jichang
 *time   : 2017/05/15
 *desc   : complete
 *version: 1.0
 */
interface AddEditTaskContract {
    interface View : BaseView<Presenter> {

        fun showEmptyTaskError()

        fun showTasksList()

        fun setTitle(title: String)

        fun setDescription(description: String)

        val isActive: Boolean
    }

    interface Presenter : BasePresenter {

        fun saveTask(title: String, description: String)

        fun populateTask()

        val isDataMissing: Boolean
    }
}