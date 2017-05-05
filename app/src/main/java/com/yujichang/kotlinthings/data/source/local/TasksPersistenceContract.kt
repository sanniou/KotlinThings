package com.yujichang.kotlinthings.data.source.local

import android.provider.BaseColumns

/**
 *author : jichang
 *time   : 2017/05/05
 *desc   : The contract used for the db to save the tasks locally.
 *version: 1.0
 */

// To prevent someone from accidentally instantiating the contract class,
// give it an empty constructor.
class TasksPersistenceContract private constructor() {
    /* Inner class that defines the table contents */
    companion object TaskEntry : BaseColumns {
        val TABLE_NAME = "task"
        val COLUMN_NAME_ENTRY_ID = "entryid"
        val COLUMN_NAME_TITLE = "title"
        val COLUMN_NAME_DESCRIPTION = "description"
        val COLUMN_NAME_COMPLETED = "completed"
    }
}