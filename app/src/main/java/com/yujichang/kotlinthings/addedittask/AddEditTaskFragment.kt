package com.yujichang.kotlinthings.addedittask

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yujichang.kotlinthings.R
import kotlinx.android.synthetic.main.addtask_act.*
import kotlinx.android.synthetic.main.addtask_frag.*

/**
 *author : jichang
 *time   : 2017/05/15
 *desc   : complete
 * 主UI添加任务屏幕。用户可以输入任务标题和说明。
 *version: 1.0
 */
class AddEditTaskFragment : Fragment(),
        AddEditTaskContract.View {
    companion object {
        @JvmStatic
        val ARGUMENT_EDIT_TASK_ID = "EDIT_TASK_ID"
    }

    private lateinit var mPresenter: AddEditTaskContract.Presenter

    override fun onResume() {
        super.onResume()
        mPresenter.start()
    }

    override fun setPresenter(presenter: AddEditTaskContract.Presenter) {
        mPresenter = presenter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity.fab_edit_task_done.run {
            setImageResource(R.drawable.ic_done)
            setOnClickListener {
                mPresenter.saveTask(add_task_title.text.toString(), add_task_description.text.toString())
            }

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.addtask_frag, container, false)
    }

    override fun showEmptyTaskError() {
        Snackbar.make(add_task_title, getString(R.string.empty_task_message), Snackbar.LENGTH_LONG).show()
    }

    override fun showTasksList() {
        activity.setResult(Activity.RESULT_OK)
        activity.finish()
    }

    override fun setTitle(title: String) {
        add_task_title.setText(title)
    }

    override fun setDescription(description: String) {
        add_task_description.setText(description)
    }

    override val isActive: Boolean
        get() = isAdded

}