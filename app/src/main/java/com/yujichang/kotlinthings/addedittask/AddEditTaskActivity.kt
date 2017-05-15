package com.yujichang.kotlinthings.addedittask

import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.VisibleForTesting
import android.support.test.espresso.IdlingResource
import android.support.v7.app.AppCompatActivity
import com.yujichang.kotlinthings.Injection
import com.yujichang.kotlinthings.R
import com.yujichang.kotlinthings.util.EspressoIdlingResource
import kotlinx.android.synthetic.main.addtask_act.*

class AddEditTaskActivity : AppCompatActivity() {

    companion object {
        @JvmStatic
        val REQUEST_ADD_TASK = 1
        @JvmStatic
        val SHOULD_LOAD_DATA_FROM_REPO_KEY = "SHOULD_LOAD_DATA_FROM_REPO_KEY"
    }

    private lateinit var mAddEditTaskPresenter: AddEditTaskPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addtask_act)
        // Set up the toolbar.
        setSupportActionBar(toolbar)

        val taskId = intent.getStringExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID)

        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)

            setTitle(if (taskId == null) {
                R.string.add_task
            } else {
                R.string.edit_task
            })
        }
        var addEditTaskFragment = supportFragmentManager
                .findFragmentById(R.id.contentFrame) as AddEditTaskFragment?

        if (addEditTaskFragment == null) {
            addEditTaskFragment = AddEditTaskFragment()

            if (intent.hasExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID)) {
                addEditTaskFragment.arguments = Bundle().apply {
                    putString(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID, taskId)
                }
            }

            with(supportFragmentManager.beginTransaction()) {
                add(R.id.contentFrame, addEditTaskFragment)
                commit()
            }
        }
        var shouldLoadDataFromRepo = true
        // 如果这是配置更改，则阻止presenter从存储库加载数据。
        if (savedInstanceState != null) {
            // 当配置更改发生时，数据可能没有加载，所以我们保存了状态。
            shouldLoadDataFromRepo = savedInstanceState.getBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY)
        }

        // Create the presenter
        mAddEditTaskPresenter = AddEditTaskPresenter(
                taskId,
                Injection.provideTasksRepository(applicationContext),
                addEditTaskFragment,
                shouldLoadDataFromRepo)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        // 保存状态，以便下次我们知道是否需要刷新数据。
        outState.putBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY, mAddEditTaskPresenter.isDataMissing)
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    @VisibleForTesting
    fun getCountingIdlingResource(): IdlingResource {
        return EspressoIdlingResource.getIdlingResource()
    }
}
