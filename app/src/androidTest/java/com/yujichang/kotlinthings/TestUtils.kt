package com.yujichang.kotlinthings

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.support.annotation.IdRes
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.runner.lifecycle.ActivityLifecycleMonitor
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import android.support.test.runner.lifecycle.Stage.RESUMED
import android.support.v7.widget.Toolbar

/**
 *author : jichang
 *time   : 2017/05/19
 *desc   : 喵喵
 *version: 1.0
 */

private fun rotateToLandscape(activity: Activity) {
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
}

private fun rotateToPortrait(activity: Activity) {
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
}

fun rotateOrientation(activity: Activity) {
    val currentOrientation = activity.resources.configuration.orientation

    when (currentOrientation) {
        Configuration.ORIENTATION_LANDSCAPE -> rotateToPortrait(activity)
        Configuration.ORIENTATION_PORTRAIT -> rotateToLandscape(activity)
        else -> rotateToLandscape(activity)
    }
}

/**
 * Returns the content description for the navigation button view in the toolbar.
 */
fun getToolbarNavigationContentDescription(
        activity: Activity, @IdRes toolbar1: Int): String {
    val toolbar = activity.findViewById(toolbar1) as Toolbar?
    if (toolbar != null) {
        return toolbar.navigationContentDescription as String
    } else {
        throw RuntimeException("No toolbar found.")
    }
}

/**
 * Gets an Activity in the RESUMED stage.
 *
 *
 * This method should never be called from the Main thread. In certain situations there might
 * be more than one Activities in RESUMED stage, but only one is returned.
 * See [ActivityLifecycleMonitor].
 */

fun getCurrentActivity(): Activity {
    // 该数组只是包装Activity，并可以从Runnable访问它。
    val resumedActivity = arrayOfNulls<Activity>(1)

    getInstrumentation().runOnMainSync {
        val resumedActivities = ActivityLifecycleMonitorRegistry.getInstance()
                .getActivitiesInStage(RESUMED)
        if (resumedActivities.iterator().hasNext()) {
            resumedActivity[0] = resumedActivities.iterator().next() as Activity
        }
    }
    return resumedActivity[0]!!
}