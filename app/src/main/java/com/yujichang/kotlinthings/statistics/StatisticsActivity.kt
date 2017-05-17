package com.yujichang.kotlinthings.statistics

import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.yujichang.kotlinthings.Injection
import com.yujichang.kotlinthings.R
import kotlinx.android.synthetic.main.statistcs_act.*

class StatisticsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.statistcs_act)

        // Set up the toolbar.
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            setTitle(R.string.statistics_title)
            setHomeAsUpIndicator(R.drawable.ic_menu)
            setDisplayHomeAsUpEnabled(true)
        }

        // Set up the navigation drawer.
        drawer_layout.setStatusBarBackground(R.color.colorPrimaryDark)
        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.list_navigation_menu_item -> NavUtils.navigateUpFromSameTask(this@StatisticsActivity)
                R.id.statistics_navigation_menu_item -> {
                    // Do nothing, we're already on that screen
                }
            }
            // Close the navigation drawer when an item is selected.
            it.isChecked = true
            drawer_layout.closeDrawers()
            true
        }

        var statisticsFragment: StatisticsFragment? = supportFragmentManager
                .findFragmentById(R.id.contentFrame) as StatisticsFragment?
        if (statisticsFragment == null) {
            statisticsFragment = StatisticsFragment()
            supportFragmentManager.beginTransaction().run {
                add(R.id.contentFrame,   statisticsFragment)
                commit()
            }
        }

        StatisticsPresenter(
                Injection.provideTasksRepository(applicationContext), statisticsFragment)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Open the navigation drawer when the home icon is selected from the toolbar.
                drawer_layout.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
