package com.yujichang.kotlinthings

import android.content.Context
import com.yujichang.kotlinthings.data.FakeTasksRemoteDataSource
import com.yujichang.kotlinthings.data.source.TasksRepository
import com.yujichang.kotlinthings.data.source.local.TasksLocalDataSource

/**
 *author : jichang
 *time   : 2017/05/05
 *desc   : complete
 *在编译时启用了{@link TasksDataSource}的模拟实现。 这对于测试是有用的，
 * 因为它允许我们使用类的假实例来隔离依赖关系并且密封地运行测试。
 *version: 1.0
 */
object Injection {
    fun provideTasksRepository(context: Context): TasksRepository {
        return TasksRepository.getInstance(FakeTasksRemoteDataSource,
                TasksLocalDataSource.getInstance(context))
    }
}