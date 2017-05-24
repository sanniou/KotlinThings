package com.yujichang.kotlinthings.custom.action

import android.content.res.Resources
import android.support.design.widget.NavigationView
import android.support.test.espresso.PerformException
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.espresso.util.HumanReadables
import android.view.Menu
import android.view.View
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

/**
 *author : jichang
 *time   : 2017/05/19
 *desc   : complete
 * {@link NavigationView}交互的操作
 *version: 1.0
 */

/**
 * Returns a {@link ViewAction} that navigates to a menu item in {@link NavigationView} using a
 * menu item resource id.
 *
 * <p>
 * View constraints:
 * <ul>
 * <li>View must be a child of a {@link DrawerLayout}
 * <li>View must be of type {@link NavigationView}
 * <li>View must be visible on screen
 * <li>View must be displayed on screen
 * <ul>
 *
 * @param menuItemId the resource id of the menu item
 * @return a {@link ViewAction} that navigates on a menu item
 */
fun navigationTo(menuItemId: Int): ViewAction {
    return object : ViewAction {
        override fun perform(uiController: UiController, view: View) {
            with((view as NavigationView).menu) {
                if (findItem(menuItemId) == null) {
                    throw PerformException.Builder()
                            .withActionDescription(description)
                            .withViewDescription(HumanReadables.describe(view))
                            .withCause(RuntimeException(getErrorMessage(this, view)))
                            .build()
                } else {
                    performIdentifierAction(menuItemId, 0)
                    uiController.loopMainThreadUntilIdle()
                }
            }
        }

        fun getErrorMessage(menu: Menu, view: View): String {
            val NEW_LINE = System.getProperty("line.separator")
            val errorMessage = StringBuilder("Menu item was not found, available menu items:")
                    .append(NEW_LINE)
            for (position in 0..menu.size()) {
                errorMessage.append("[MenuItem] position=$position")
                menu.getItem(position)?.run {
                    errorMessage.append(", title=${title ?: ""}")
                    view.resources?.run {
                        errorMessage.append(", id=${
                        try {
                            view.resources.getResourceName(itemId)
                        } catch (nfe: Resources.NotFoundException) {
                            errorMessage.append("not found");
                        }}")
                    }
                }
                errorMessage.append(NEW_LINE)
            }
            return errorMessage.toString()

        }

        override fun getDescription(): String {
            return "click on menu item with id"
        }

        override fun getConstraints(): Matcher<View> {
            return allOf<View>(isAssignableFrom(NavigationView::class.java),
                    withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                    isDisplayingAtLeast(90)
            )
        }

    }
}