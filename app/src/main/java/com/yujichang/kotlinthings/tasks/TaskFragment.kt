package com.yujichang.kotlinthings.tasks

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.*
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.PopupMenu
import android.widget.TextView
import com.yujichang.kotlinthings.R
import com.yujichang.kotlinthings.addedittask.AddEditTaskActivity
import com.yujichang.kotlinthings.data.Task
import com.yujichang.kotlinthings.taskdetail.TaskDetailActivity
import kotlinx.android.synthetic.main.task_act.*
import kotlinx.android.synthetic.main.tasks_frag.*
import kotlinx.android.synthetic.main.tasks_frag.view.*

/**
 *author : jichang
 *time   : 2017/05/03
 *desc   :complete
 * 显示{@link Task}的网格。用户可以选择查看所有，活动或已完成的任务。
 *version: 1.0
 */

class TaskFragment : Fragment(),
        TasksContract.View {

    /**
     * ListView中任务点击的侦听器。
     */
    private val mItemListener: TaskItemListener = object : TaskItemListener {
        override fun onTaskClick(clickedTask: Task) =
                mPresenter.openTaskDetails(clickedTask)


        override fun onCompleteTaskClick(completedTask: Task) =
                mPresenter.completeTask(completedTask)


        override fun onActivateTaskClick(activatedTask: Task) =
                mPresenter.activateTask(activatedTask)

    }

    private lateinit var mPresenter: TasksContract.Presenter

    private val mListAdapter = TasksAdapter(ArrayList<Task>(0), mItemListener)

    override fun onCreate(savedInstanceState: Bundle?) =
            super.onCreate(savedInstanceState)


    override fun onResume() {
        super.onResume()
        mPresenter.start()
    }

    override fun setPresenter(presenter: TasksContract.Presenter) {
        mPresenter = presenter
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mPresenter.result(requestCode, resultCode)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.tasks_frag, container, false)
                .apply {
                    // Set up tasks view
                    this.tasks_list.adapter = mListAdapter
                    // Set up  no tasks view
                    noTasksAdd.setOnClickListener { showAddTask() }
                    // Set up floating action button
                    activity.fab_add_task
                            .run {
                                setImageResource(R.drawable.ic_add)
                                setOnClickListener {
                                    mPresenter.addNewTask()
                                }
                            }
                    refresh_layout.run {

                        // Set up progress indicator
                        setColorSchemeColors(
                                ContextCompat.getColor(activity, R.color.colorPrimary),
                                ContextCompat.getColor(activity, R.color.colorAccent),
                                ContextCompat.getColor(activity, R.color.colorPrimaryDark)
                        )
                        // Set the scrolling view in the custom SwipeRefreshLayout.
                        setScrollUpChild(tasks_list)
                        setOnRefreshListener { mPresenter.loadTasks(false) }
                    }

                }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_clear -> mPresenter.clearCompletedTasks()
            R.id.menu_filter -> showFilteringPopUpMenu()
            R.id.menu_refresh -> mPresenter.loadTasks(true)
        }
        return true
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) =
            inflater.inflate(R.menu.tasks_fragment_menu, menu)


    override fun showFilteringPopUpMenu() {
        PopupMenu(context, activity.findViewById(R.id.menu_filter))
                .run {
                    menuInflater.inflate(R.menu.filter_tasks, menu)
                    setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.active -> mPresenter.filtering = TasksFilterType.ACTIVE_TASKS
                            R.id.completed -> mPresenter.filtering = TasksFilterType.COMPLETED_TASKS
                            else -> mPresenter.filtering = TasksFilterType.ALL_TASKS
                        }
                        mPresenter.loadTasks(false)
                        true
                    }
                    show()
                }
    }

    override fun setLoadingIndicator(active: Boolean) {
        refresh_layout.run {
            // 在布局完成后，确保setRefreshing（）被调用。
            this.post {
                this.isRefreshing = active
            }
        }
    }

    override fun showTasks(tasks: List<Task>) {
        mListAdapter.replaceData(tasks)
        tasksLL.visibility = View.VISIBLE
        noTasks.visibility = View.GONE
    }

    override fun showNoActiveTasks() =
            showNoTasksViews(
                    resources.getString(R.string.no_tasks_active),
                    R.drawable.ic_check_circle_24dp,
                    false
            )


    override fun showNoTasks() =
            showNoTasksViews(
                    resources.getString(R.string.no_tasks_all),
                    R.drawable.ic_assignment_turned_in_24dp,
                    false
            )


    override fun showNoCompletedTasks() =
            showNoTasksViews(
                    resources.getString(R.string.no_tasks_completed),
                    R.drawable.ic_verified_user_24dp,
                    false
            )


    override fun showSuccessfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_task_message))
    }


    private fun showNoTasksViews(mainText: String, iconRes: Int, showAddView: Boolean) {
        tasksLL.visibility = View.GONE
        noTasks.visibility = View.VISIBLE

        noTasksMain.text = mainText
        noTasksIcon.setImageDrawable(resources.getDrawable(iconRes))
        noTasksAdd.visibility = if (showAddView) View.VISIBLE else View.GONE
    }

    override fun showActiveFilterLabel() {
        filteringLabel.text = resources.getString(R.string.label_active)
    }

    override fun showCompletedFilterLabel() {
        filteringLabel.text = resources.getString(R.string.label_completed)
    }

    override fun showAllFilterLabel() {
        filteringLabel.text = resources.getString(R.string.label_all)
    }

    override fun showAddTask() {
        startActivityForResult(Intent(context, AddEditTaskActivity::class.java),
                AddEditTaskActivity.REQUEST_ADD_TASK)
    }

    override fun showTaskDetailsUi(taskId: String) {
        // 在它自己的活动中，因为它更有意义的方式，它给我们的灵活性
        //显示一些意图存根。
        val intent = Intent(context, TaskDetailActivity::class.java)
        intent.putExtra(TaskDetailActivity.EXTRA_TASK_ID, taskId)
        startActivity(intent)
    }

    override fun showTaskMarkedComplete() =
            showMessage(getString(R.string.task_marked_complete))


    override fun showTaskMarkedActive() =
            showMessage(getString(R.string.task_marked_active))


    override fun showCompletedTasksCleared() =
            showMessage(getString(R.string.completed_tasks_cleared))


    override fun showLoadingTasksError() =
            showMessage(getString(R.string.loading_tasks_error))


    private fun showMessage(message: String) =
            Snackbar.make(view!!, message, Snackbar.LENGTH_LONG).show()


    override val isActive: Boolean
        get() = isAdded


    private class TasksAdapter(tasks: List<Task>, val mItemListener: TaskItemListener) : BaseAdapter() {

        lateinit var mTasks: List<Task>

        init {
            setList(tasks)
        }

        fun replaceData(tasks: List<Task>) {
            setList(tasks)
            notifyDataSetChanged()
        }

        private fun setList(tasks: List<Task>) {
            mTasks = tasks
        }

        override fun getCount(): Int {
            return mTasks.size
        }

        override fun getItem(i: Int): Task {
            return mTasks[i]
        }

        override fun getItemId(i: Int): Long {
            return i.toLong()
        }

        override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {

            val rowView = view ?: LayoutInflater.from(viewGroup.context).inflate(R.layout.task_item, viewGroup, false)

            val task = getItem(i)
            val titleTV = rowView.findViewById(R.id.title) as TextView
            titleTV.text = task.getTitleForList()

            val completeCB = rowView.findViewById(R.id.complete) as CheckBox

            // 活动的/已完成任务UI
            completeCB.isChecked = task.completed
            if (task.completed) {
                rowView.setBackgroundDrawable(viewGroup.context
                        .resources.getDrawable(R.drawable.list_completed_touch_feedback))
            } else {
                rowView.setBackgroundDrawable(viewGroup.context
                        .resources.getDrawable(R.drawable.touch_feedback))
            }

            completeCB.setOnClickListener {
                if (!task.completed) {
                    mItemListener.onCompleteTaskClick(task)
                } else {
                    mItemListener.onActivateTaskClick(task)
                }
            }

            rowView.setOnClickListener { mItemListener.onTaskClick(task) }

            return rowView
        }
    }


    interface TaskItemListener {

        fun onTaskClick(clickedTask: Task)

        fun onCompleteTaskClick(completedTask: Task)

        fun onActivateTaskClick(activatedTask: Task)
    }
}