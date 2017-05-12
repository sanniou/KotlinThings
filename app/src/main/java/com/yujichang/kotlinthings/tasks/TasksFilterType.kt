package com.yujichang.kotlinthings.tasks

/**
 *author : jichang
 *time   : 2017/05/05
 *desc   : complete
 *version: 1.0
 */
enum class TasksFilterType {
    /**
     * Do not filter tasks.
     */
    ALL_TASKS,

    /**
     * Filters only the active (not completed yet) tasks.
     */
    ACTIVE_TASKS,

    /**
     * Filters only the completed tasks.
     */
    COMPLETED_TASKS
}