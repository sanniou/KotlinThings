/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yujichang.kotlinthings.data.source.local

import android.provider.BaseColumns

/**
 * The contract used for the db to save the tasks locally.
 */
object TasksPersistenceContract {

    /* Inner class that defines the table contents */
    abstract class TaskEntry : BaseColumns {
        companion object {
            val _ID = BaseColumns._ID //不这样写无法使用？
            val TABLE_NAME = "task"
            val COLUMN_NAME_ENTRY_ID = "entryid"
            val COLUMN_NAME_TITLE = "title"
            val COLUMN_NAME_DESCRIPTION = "description"
            val COLUMN_NAME_COMPLETED = "completed"
        }
    }
}// To prevent someone from accidentally instantiating the contract class,
// give it an empty constructor.
