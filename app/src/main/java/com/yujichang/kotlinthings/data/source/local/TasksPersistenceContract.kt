package com.yujichang.kotlinthings.data.source.local

import android.provider.BaseColumns

/**
 *author : jichang
 *time   : 2017/05/05
 *desc   : The contract used for the db to save the tasks locally.
 *version: 1.0
 */

// 防止外界偶然 实例化契约类，
// 给它一个空的构造函数.
class TasksPersistenceContract private constructor() {

    /* 定义表内容的内部类 */
    abstract class TaskEntry : BaseColumns {
        companion object {
            val _COUNT = BaseColumns._COUNT
            val _ID = BaseColumns._ID //kotlin的interface 属性要么是抽象的，要么提供访问器的实现，这里无法在外部引用_ID,只好使用本地val
            val TABLE_NAME = "task"
            val COLUMN_NAME_ENTRY_ID = "entryid"
            val COLUMN_NAME_TITLE = "title"
            val COLUMN_NAME_DESCRIPTION = "description"
            val COLUMN_NAME_COMPLETED = "completed"
        }
    }
}