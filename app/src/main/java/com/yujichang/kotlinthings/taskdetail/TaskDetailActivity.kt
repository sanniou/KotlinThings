package com.yujichang.kotlinthings.taskdetail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.yujichang.kotlinthings.Injection
import com.yujichang.kotlinthings.R
import kotlinx.android.synthetic.main.taskdetail_act.*

class TaskDetailActivity : AppCompatActivity() {
    companion object {
        val EXTRA_TASK_ID = "TASK_ID"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.taskdetail_act)

        // Set up the toolbar.
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        // Get the requested task id
        val taskId = intent.getStringExtra(EXTRA_TASK_ID)

        var taskDetailFragment = supportFragmentManager
                .findFragmentById(R.id.contentFrame) as TaskDetailFragment?

        if (taskDetailFragment == null) {
            taskDetailFragment = TaskDetailFragment.newInstance(taskId)
            with(supportFragmentManager.beginTransaction()) {
                add(R.id.contentFrame, taskDetailFragment)
                commit()
            }
        }

        // Create the presenter
        TaskDetailPresenter(
                taskId,
                Injection.provideTasksRepository(applicationContext),
                taskDetailFragment)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
