package com.yujichang.kotlinthings.data

import com.google.common.base.Strings

/**
 *author : jichang
 *time   : 2017/05/05
 *desc   : complete
 *version: 1.0
 */
data class Task(val id: String ,
                val title: String,
                val description: String,
                val completed: Boolean = false) {
    fun getTitleForList(): String {
        if (!Strings.isNullOrEmpty(title)) {
            return title
        } else {
            return description
        }
    }

    fun isActive(): Boolean {
        return !completed
    }

    fun isEmpty(): Boolean {
        return Strings.isNullOrEmpty(title) && Strings.isNullOrEmpty(description)
    }
}