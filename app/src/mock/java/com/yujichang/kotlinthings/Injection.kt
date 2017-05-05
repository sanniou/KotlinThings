package com.yujichang.kotlinthings

import android.content.Context
import com.yujichang.kotlinthings.data.FakeTasksRemoteDataSource
import com.yujichang.kotlinthings.data.source.TasksRepository
import com.yujichang.kotlinthings.data.source.local.TasksLocalDataSource

/**
 *author : jichang
 *time   : 2017/05/05
 *desc   : 喵喵
 *version: 1.0
 */
object Injection {
    fun provideTasksRepository(context: Context): TasksRepository {
        return TasksRepository.getInstance(FakeTasksRemoteDataSource,
                TasksLocalDataSource.getInstance(context))
    }
}