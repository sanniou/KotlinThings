package com.yujichang.kotlinthings.tasks


import android.support.test.InstrumentationRegistry
import android.support.test.InstrumentationRegistry.getTargetContext
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.filters.SdkSuppress
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.widget.ListView
import com.google.common.base.Preconditions.checkArgument
import com.yujichang.kotlinthings.*
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsNot.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 *author : jichang
 *time   : 2017/05/23
 *desc   :
 * 测试Task界面，包含所有任务列表的主界面。
 *version: 1.0
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class TaskScreeTest {
    private val TITLE1 = "TITLE1"

    private val DESCRIPTION = "DESCRIPTION"

    private val TITLE2 = "TITLE2"

    @Rule
    @JvmField
    var mTasksActivityTestRule = object : ActivityTestRule<TasksActivity>(TasksActivity::class.java) {

        /**
         *为了避免长列表的任务和需要滚动列表以查找任务，我们在每次测试之前调用[TasksDataSource.deleteAllTask​​s]。
         */
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            //在@Before中执行此操作会生成race condition.。
            Injection.provideTasksRepository(InstrumentationRegistry.getTargetContext())
                    .deleteAllTasks()
        }
    }

    /**
     * 与[ListView]中的项目匹配的文本的自定义[Matcher]。
     *
     *
     * View constraints:
     *
     *  * View must be a child of a [ListView]
     *

     * @param itemText the text to match
     * *
     * @return匹配给定视图中的文本
     */
    private fun withItemText(itemText: String): Matcher<View> {
        checkArgument(!itemText.isEmpty(), "itemText cannot be null or empty")
        return object : TypeSafeMatcher<View>() {
            public override fun matchesSafely(item: View): Boolean {
                return allOf<View>(
                        isDescendantOfA(isAssignableFrom(ListView::class.java)),
                        withText(itemText)).matches(item)
            }

            override fun describeTo(description: Description) {
                description.appendText("is isDescendantOfA LV with text " + itemText)
            }
        }
    }

    @Test
    fun clickAddTaskButton_opensAddTaskUi() {
        // Click on the add task button
        onView(withId(R.id.fab_add_task)).perform(click())

        // Check if the add task screen is displayed
        onView(withId(R.id.add_task_title)).check(matches(isDisplayed()))
    }

    @Test
    @Throws(Exception::class)
    fun editTask() {
        // First add a task
        createTask(TITLE1, DESCRIPTION)

        // Click on the task on the list
        onView(withText(TITLE1)).perform(click())

        // Click on the edit task button
        onView(withId(R.id.fab_edit_task)).perform(click())

        val editTaskTitle = TITLE2
        val editTaskDescription = "New Description"

        // Edit task title and description
        onView(withId(R.id.add_task_title))
                .perform(replaceText(editTaskTitle), closeSoftKeyboard()) // Type new task title
        onView(withId(R.id.add_task_description)).perform(replaceText(editTaskDescription),
                closeSoftKeyboard()) // Type new task description and close the keyboard

        // Save the task
        onView(withId(R.id.fab_edit_task_done)).perform(click())

        // Verify task is displayed on screen in the task list.
        onView(withItemText(editTaskTitle)).check(matches(isDisplayed()))

        // Verify previous task is not displayed
        onView(withItemText(TITLE1)).check(doesNotExist())
    }

    @Test
    @Throws(Exception::class)
    fun addTaskToTasksList() {
        createTask(TITLE1, DESCRIPTION)

        // Verify task is displayed on screen
        onView(withItemText(TITLE1)).check(matches(isDisplayed()))
    }

    @Test
    fun markTaskAsComplete() {
        viewAllTasks()

        // Add active task
        createTask(TITLE1, DESCRIPTION)

        // Mark the task as complete
        clickCheckBoxForTask(TITLE1)

        // Verify task is shown as complete
        viewAllTasks()
        onView(withItemText(TITLE1)).check(matches(isDisplayed()))
        viewActiveTasks()
        onView(withItemText(TITLE1)).check(matches(not(isDisplayed())))
        viewCompletedTasks()
        onView(withItemText(TITLE1)).check(matches(isDisplayed()))
    }


    @Test
    fun markTaskAsActive() {
        viewAllTasks()

        // Add completed task
        createTask(TITLE1, DESCRIPTION)
        clickCheckBoxForTask(TITLE1)

        // Mark the task as active
        clickCheckBoxForTask(TITLE1)

        // Verify task is shown as active
        viewAllTasks()
        onView(withItemText(TITLE1)).check(matches(isDisplayed()))
        viewActiveTasks()
        onView(withItemText(TITLE1)).check(matches(isDisplayed()))
        viewCompletedTasks()
        onView(withItemText(TITLE1)).check(matches(not(isDisplayed())))
    }

    @Test
    fun showAllTasks() {
        // Add 2 active tasks
        createTask(TITLE1, DESCRIPTION)
        createTask(TITLE2, DESCRIPTION)

        //Verify that all our tasks are shown
        viewAllTasks()
        onView(withItemText(TITLE1)).check(matches(isDisplayed()))
        onView(withItemText(TITLE2)).check(matches(isDisplayed()))
    }

    @Test
    fun showActiveTasks() {
        // Add 2 active tasks
        createTask(TITLE1, DESCRIPTION)
        createTask(TITLE2, DESCRIPTION)

        //Verify that all our tasks are shown
        viewActiveTasks()
        onView(withItemText(TITLE1)).check(matches(isDisplayed()))
        onView(withItemText(TITLE2)).check(matches(isDisplayed()))
    }

    @Test
    fun showCompletedTasks() {
        // Add 2 completed tasks
        createTask(TITLE1, DESCRIPTION)
        clickCheckBoxForTask(TITLE1)
        createTask(TITLE2, DESCRIPTION)
        clickCheckBoxForTask(TITLE2)

        // Verify that all our tasks are shown
        viewCompletedTasks()
        onView(withItemText(TITLE1)).check(matches(isDisplayed()))
        onView(withItemText(TITLE2)).check(matches(isDisplayed()))
    }

    @Test
    fun clearCompletedTasks() {
        viewAllTasks()

        // Add 2 complete tasks
        createTask(TITLE1, DESCRIPTION)
        clickCheckBoxForTask(TITLE1)
        createTask(TITLE2, DESCRIPTION)
        clickCheckBoxForTask(TITLE2)

        // Click clear completed in menu
        openActionBarOverflowOrOptionsMenu(getTargetContext())
        onView(withText(R.string.menu_clear)).perform(click())

        //Verify that completed tasks are not shown
        onView(withItemText(TITLE1)).check(matches(not(isDisplayed())))
        onView(withItemText(TITLE2)).check(matches(not(isDisplayed())))
    }

    @Test
    fun createOneTask_deleteTask() {
        viewAllTasks()

        // Add active task
        createTask(TITLE1, DESCRIPTION)

        // Open it in details view
        onView(withText(TITLE1)).perform(click())

        // Click delete task in menu
        onView(withId(R.id.menu_delete)).perform(click())

        // Verify it was deleted
        viewAllTasks()
        onView(withText(TITLE1)).check(matches(not(isDisplayed())))
    }

    @Test
    fun createTwoTasks_deleteOneTask() {
        // Add 2 active tasks
        createTask(TITLE1, DESCRIPTION)
        createTask(TITLE2, DESCRIPTION)

        // Open the second task in details view
        onView(withText(TITLE2)).perform(click())

        // Click delete task in menu
        onView(withId(R.id.menu_delete)).perform(click())

        // Verify only one task was deleted
        viewAllTasks()
        onView(withText(TITLE1)).check(matches(isDisplayed()))
        onView(withText(TITLE2)).check(doesNotExist())
    }

    @Test
    fun markTaskAsCompleteOnDetailScreen_taskIsCompleteInList() {
        viewAllTasks()

        // Add 1 active task
        createTask(TITLE1, DESCRIPTION)

        // Click on the task on the list
        onView(withText(TITLE1)).perform(click())

        // Click on the checkbox in task details screen
        onView(withId(R.id.task_detail_complete)).perform(click())

        // Click on the navigation up button to go back to the list
        onView(withContentDescription(getToolbarNavigationContentDescription())).perform(click())

        // Check that the task is marked as completed
        onView(allOf(withId(R.id.complete),
                hasSibling(withText(TITLE1)))).check(matches(isChecked()))
    }

    @Test
    fun markTaskAsActiveOnDetailScreen_taskIsActiveInList() {
        viewAllTasks()

        // Add 1 completed task
        createTask(TITLE1, DESCRIPTION)
        clickCheckBoxForTask(TITLE1)

        // Click on the task on the list
        onView(withText(TITLE1)).perform(click())

        // Click on the checkbox in task details screen
        onView(withId(R.id.task_detail_complete)).perform(click())

        // Click on the navigation up button to go back to the list
        onView(withContentDescription(getToolbarNavigationContentDescription())).perform(click())

        // Check that the task is marked as active
        onView(allOf(withId(R.id.complete),
                hasSibling(withText(TITLE1)))).check(matches(not(isChecked())))
    }

    @Test
    fun markTaskAsAcompleteAndActiveOnDetailScreen_taskIsActiveInList() {
        viewAllTasks()

        // Add 1 active task
        createTask(TITLE1, DESCRIPTION)

        // Click on the task on the list
        onView(withText(TITLE1)).perform(click())

        // Click on the checkbox in task details screen
        onView(withId(R.id.task_detail_complete)).perform(click())

        // Click again to restore it to original state
        onView(withId(R.id.task_detail_complete)).perform(click())

        // Click on the navigation up button to go back to the list
        onView(withContentDescription(getToolbarNavigationContentDescription())).perform(click())

        // Check that the task is marked as active
        onView(allOf(withId(R.id.complete),
                hasSibling(withText(TITLE1)))).check(matches(not(isChecked())))
    }

    @Test
    fun markTaskAsActiveAndCompleteOnDetailScreen_taskIsCompleteInList() {
        viewAllTasks()

        // Add 1 completed task
        createTask(TITLE1, DESCRIPTION)
        clickCheckBoxForTask(TITLE1)

        // Click on the task on the list
        onView(withText(TITLE1)).perform(click())

        // Click on the checkbox in task details screen
        onView(withId(R.id.task_detail_complete)).perform(click())

        // Click again to restore it to original state
        onView(withId(R.id.task_detail_complete)).perform(click())

        // Click on the navigation up button to go back to the list
        onView(withContentDescription(getToolbarNavigationContentDescription())).perform(click())

        // Check that the task is marked as active
        onView(allOf(withId(R.id.complete),
                hasSibling(withText(TITLE1)))).check(matches(isChecked()))
    }

    @Test
    fun orientationChange_FilterActivePersists() {

        // Add a completed task
        createTask(TITLE1, DESCRIPTION)
        clickCheckBoxForTask(TITLE1)

        // when switching to active tasks
        viewActiveTasks()

        // then no tasks should appear
        onView(withText(TITLE1)).check(matches(not(isDisplayed())))

        // when rotating the screen
        rotateOrientation(mTasksActivityTestRule.activity)

        // then nothing changes
        onView(withText(TITLE1)).check(doesNotExist())
    }

    @Test
    fun orientationChange_FilterCompletedPersists() {

        // Add a completed task
        createTask(TITLE1, DESCRIPTION)
        clickCheckBoxForTask(TITLE1)

        // when switching to completed tasks
        viewCompletedTasks()

        // the completed task should be displayed
        onView(withText(TITLE1)).check(matches(isDisplayed()))

        // when rotating the screen
        rotateOrientation(mTasksActivityTestRule.activity)

        // then nothing changes
        onView(withText(TITLE1)).check(matches(isDisplayed()))
        onView(withText(R.string.label_completed)).check(matches(isDisplayed()))
    }

    @Test
    @SdkSuppress(minSdkVersion = 21) // Blinking cursor after rotation breaks this in API 19
    @Throws(Throwable::class)
    fun orientationChange_DuringEdit_ChangePersists() {
        // Add a completed task
        createTask(TITLE1, DESCRIPTION)

        // Open the task in details view
        onView(withText(TITLE1)).perform(click())

        // Click on the edit task button
        onView(withId(R.id.fab_edit_task)).perform(click())

        // Change task title (but don't save)
        onView(withId(R.id.add_task_title))
                .perform(replaceText(TITLE2), closeSoftKeyboard()) // Type new task title

        // Rotate the screen
        rotateOrientation(getCurrentActivity())

        // Verify task title is restored
        onView(withId(R.id.add_task_title)).check(matches(withText(TITLE2)))
    }

    @Test
    @SdkSuppress(minSdkVersion = 21) // Blinking cursor after rotation breaks this in API 19
    fun orientationChange_DuringEdit_NoDuplicate() {
        // Add a completed task
        createTask(TITLE1, DESCRIPTION)

        // Open the task in details view
        onView(withText(TITLE1)).perform(click())

        // Click on the edit task button
        onView(withId(R.id.fab_edit_task)).perform(click())

        // Rotate the screen
        rotateOrientation(getCurrentActivity())

        // Edit task title and description
        onView(withId(R.id.add_task_title))
                .perform(replaceText(TITLE2), closeSoftKeyboard()) // Type new task title
        onView(withId(R.id.add_task_description)).perform(replaceText(DESCRIPTION),
                closeSoftKeyboard()) // Type new task description and close the keyboard

        // Save the task
        onView(withId(R.id.fab_edit_task_done)).perform(click())

        // Verify task is displayed on screen in the task list.
        onView(withItemText(TITLE2)).check(matches(isDisplayed()))

        // Verify previous task is not displayed
        onView(withItemText(TITLE1)).check(doesNotExist())
    }

    private fun viewAllTasks() {
        onView(withId(R.id.menu_filter)).perform(click())
        onView(withText(R.string.nav_all)).perform(click())
    }

    private fun viewActiveTasks() {
        onView(withId(R.id.menu_filter)).perform(click())
        onView(withText(R.string.nav_active)).perform(click())
    }

    private fun viewCompletedTasks() {
        onView(withId(R.id.menu_filter)).perform(click())
        onView(withText(R.string.nav_completed)).perform(click())
    }

    private fun createTask(title: String, description: String) {
        // Click on the add task button
        onView(withId(R.id.fab_add_task)).perform(click())

        // Add task title and description
        onView(withId(R.id.add_task_title)).perform(typeText(title),
                closeSoftKeyboard()) // Type new task title
        onView(withId(R.id.add_task_description)).perform(typeText(description),
                closeSoftKeyboard()) // Type new task description and close the keyboard

        // Save the task
        onView(withId(R.id.fab_edit_task_done)).perform(click())
    }

    private fun clickCheckBoxForTask(title: String) {
        onView(allOf(withId(R.id.complete), hasSibling(withText(title)))).perform(click())
    }

    private fun getText(stringId: Int): String {
        return mTasksActivityTestRule.activity.resources.getString(stringId)
    }

    private fun getToolbarNavigationContentDescription(): String {
        return getToolbarNavigationContentDescription(
                mTasksActivityTestRule.activity, R.id.toolbar)
    }
}