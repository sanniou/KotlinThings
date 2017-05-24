package com.yujichang.kotlinthings.tasks


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.pressBack
import android.support.test.espresso.NoActivityResumedException
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.DrawerActions.open
import android.support.test.espresso.contrib.DrawerMatchers.isClosed
import android.support.test.espresso.contrib.DrawerMatchers.isOpen
import android.support.test.espresso.contrib.NavigationViewActions.navigateTo
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.Gravity
import com.yujichang.kotlinthings.R
import com.yujichang.kotlinthings.getToolbarNavigationContentDescription
import junit.framework.Assert.fail
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 *author : jichang
 *time   : 2017/05/19
 *desc   :
 *在{@link TasksActivity}中测试{@link DrawerLayout}布局组件，该组件管理应用程序中的导航。
 *version: 1.0
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class AppNavigationTest {
    /**
     * [ActivityTestRule] 是一个 JUnit [@Rule][Rule] 启动您的测试活动。

     *
     *
     * Rule是对每个测试方法执行的拦截器，是Junit测试的重要组成部分。
     */
    @Rule @JvmField
    var mActivityTestRule = ActivityTestRule(TasksActivity::class.java)

    @Test
    fun clickOnStatisticsNavigationItem_ShowsStatisticsScreen() {
        openStatisticsScreen()

        // 检查statistics Activity 被打开
        onView(withId(R.id.statistics)).check(matches(isDisplayed()))
    }

    @Test
    fun clickOnListNavigationItem_ShowsListScreen() {
        openStatisticsScreen()

        openTasksScreen()

        // Check that Tasks Activity was opened.
        onView(withId(R.id.tasksContainer)).check(matches(isDisplayed()))
    }

    @Test
    fun clickOnAndroidHomeIcon_OpensNavigation() {
        // Check that left drawer is closed at startup
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.

        // Open Drawer
        onView(withContentDescription(getToolbarNavigationContentDescription(
                mActivityTestRule.activity, R.id.toolbar))).perform(click())

        // Check if drawer is open
        onView(withId(R.id.drawer_layout))
                .check(matches(isOpen(Gravity.LEFT))) // Left drawer is open open.
    }

    @Test
    fun Statistics_backNavigatesToTasks() {
        openStatisticsScreen()

        // Press back to go back to the tasks list
        pressBack()

        // Check that Tasks Activity was restored.
        onView(withId(R.id.tasksContainer)).check(matches(isDisplayed()))
    }


    @Test
    fun backFromTasksScreen_ExitsApp() {
        // From the tasks screen, press back should exit the app.
        assertPressingBackExitsApp()
    }

    @Test
    fun backFromTasksScreenAfterStats_ExitsApp() {
        // This test checks that TasksActivity is a parent of StatisticsActivity

        // Open the stats screen
        openStatisticsScreen()

        // Open the tasks screen to restore the task
        openTasksScreen()

        // Pressing back should exit app
        assertPressingBackExitsApp()
    }

    private fun assertPressingBackExitsApp() {
        try {
            pressBack()
            fail("Should kill the app and throw an exception")
        } catch (e: NoActivityResumedException) {
            // Test OK
        }

    }

    private fun openTasksScreen() {
        // Open Drawer to click on navigation item.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(open()) // Open Drawer

        // Start tasks list screen.
        onView(withId(R.id.nav_view))
                .perform(navigateTo(R.id.list_navigation_menu_item))
    }


    private fun openStatisticsScreen() {
        // Open Drawer to click on navigation item.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(open()) // Open Drawer

        // Start statistics screen.
        onView(withId(R.id.nav_view))
                .perform(navigateTo(R.id.statistics_navigation_menu_item))
    }

}