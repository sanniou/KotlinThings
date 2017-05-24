package com.yujichang.kotlinthings.tasks

import android.content.Intent
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.test.espresso.IdlingResource
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.yujichang.kotlinthings.Injection
import com.yujichang.kotlinthings.R
import com.yujichang.kotlinthings.statistics.StatisticsActivity
import com.yujichang.kotlinthings.util.EspressoIdlingResource
import kotlinx.android.synthetic.main.task_act.*
import kotlinx.android.synthetic.main.task_act.drawer_layout as mDrawer
import kotlinx.android.synthetic.main.task_act.nav_view as mNavigation

/**
 * complete
 */
class TasksActivity : AppCompatActivity() {

    private val CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY"

    private lateinit var mTasksPresenter: TasksPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_act)

        //ToolBar
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setHomeAsUpIndicator(R.drawable.ic_menu)
            it.setDisplayHomeAsUpEnabled(true)
        }

        //NavigationView
        mDrawer.setStatusBarBackground(R.color.colorPrimaryDark)
        mDrawer.setOnClickListener { v: View -> v.callOnClick() }
        mNavigation.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.list_navigation_menu_item -> {
                    // 什么都不做，已经在这个界面
                }
                R.id.statistics_navigation_menu_item -> {
                    startActivity(Intent(this, StatisticsActivity::class.java))
                }
                else -> {
                }
            }
            true
        }
        // Create the tasksFragment
        var tasksFragment = supportFragmentManager.findFragmentById(R.id.contentFrame)
        if (tasksFragment == null) {
            tasksFragment = TaskFragment()
        }
        supportFragmentManager.beginTransaction()
                .add(R.id.contentFrame, tasksFragment)
                .commit()

        // Create the presenter
        mTasksPresenter = TasksPresenter(
                Injection.provideTasksRepository(applicationContext), tasksFragment as TaskFragment)

        // 加载先前保存的状态，如果可用。
        if (savedInstanceState != null) {
            val currentFiltering = savedInstanceState.getSerializable(CURRENT_FILTERING_KEY) as TasksFilterType
            mTasksPresenter.filtering = currentFiltering
        }
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(CURRENT_FILTERING_KEY, mTasksPresenter.filtering)

        super.onSaveInstanceState(outState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // 当从工具栏中选择主页图标时打开导航抽屉。
                mDrawer.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @VisibleForTesting
    fun getCountingIdlingResource(): IdlingResource {
        return EspressoIdlingResource.getIdlingResource()
    }
}

