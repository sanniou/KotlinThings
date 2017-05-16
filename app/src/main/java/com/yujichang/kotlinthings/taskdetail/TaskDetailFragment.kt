package com.yujichang.kotlinthings.taskdetail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.*
import com.yujichang.kotlinthings.R
import com.yujichang.kotlinthings.addedittask.AddEditTaskActivity
import com.yujichang.kotlinthings.addedittask.AddEditTaskFragment
import kotlinx.android.synthetic.main.taskdetail_act.*
import kotlinx.android.synthetic.main.taskdetail_frag.*

/**
 *author : jichang
 *time   : 2017/05/16
 *desc   : 喵喵
 *version: 1.0
 */
class TaskDetailFragment : Fragment()
        , TaskDetailContract.View {

    private lateinit var mPresenter: TaskDetailContract.Presenter

    companion object {
        @JvmStatic
        private val ARGUMENT_TASK_ID = "TASK_ID"

        @JvmStatic
        private val REQUEST_EDIT_TASK = 1

        @JvmStatic
        fun newInstance(taskId: String?): TaskDetailFragment {
            val arguments = Bundle()
            arguments.putString(ARGUMENT_TASK_ID, taskId)
            val fragment = TaskDetailFragment()
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onResume() {
        super.onResume()
        mPresenter.start()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)

        // Set up floating action button
        activity.fab_edit_task.setOnClickListener { mPresenter.editTask() }

        return inflater.inflate(R.layout.taskdetail_frag, container, false)
    }

    override fun setPresenter(presenter: TaskDetailContract.Presenter) {
        mPresenter = presenter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete -> {
                mPresenter.deleteTask()
                return true
            }
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.taskdetail_fragment_menu, menu)
    }

    override fun setLoadingIndicator(active: Boolean) {
        if (active) {
            task_detail_title.text = ""
            task_detail_description.text = getString(R.string.loading)
        }
    }

    override fun hideDescription() {
        task_detail_description.visibility = View.GONE
    }

    override fun hideTitle() {
        task_detail_title.visibility = View.GONE
    }

    override fun showDescription(description: String) {
        task_detail_description.visibility = View.VISIBLE
        task_detail_description.text = description
    }

    override fun showCompletionStatus(complete: Boolean) {
        task_detail_complete.isChecked = complete
        task_detail_complete.setOnCheckedChangeListener {
            _, isChecked ->
            if (isChecked) {
                mPresenter.completeTask()
            } else {
                mPresenter.activateTask()
            }
        }
    }

    override fun showEditTask(taskId: String) {
        val intent = Intent(context, AddEditTaskActivity::class.java)
        intent.putExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID, taskId)
        startActivityForResult(intent, REQUEST_EDIT_TASK)
    }

    override fun showTaskDeleted() {
        activity.finish()
    }

    override fun showTaskMarkedComplete() {
        Snackbar.make(view!!, getString(R.string.task_marked_complete), Snackbar.LENGTH_LONG)
                .show()
    }

    override fun showTaskMarkedActive() {
        Snackbar.make(view!!, getString(R.string.task_marked_active), Snackbar.LENGTH_LONG)
                .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_EDIT_TASK) {
            //如果任务编辑成功，返回到列表中。
            if (resultCode == Activity.RESULT_OK) {
                activity.finish()
            }
        }
    }


    override fun showTitle(title: String) {
        task_detail_title.visibility = View.VISIBLE
        task_detail_title.text = title
    }

    override fun showMissingTask() {
        task_detail_title.text = ""
        task_detail_description.text = getString(R.string.no_data)
    }

    override val isActive: Boolean
        get() = isAdded
}