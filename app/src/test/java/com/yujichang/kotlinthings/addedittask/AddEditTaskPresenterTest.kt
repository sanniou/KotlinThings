package com.yujichang.kotlinthings.addedittask

import com.yujichang.kotlinthings.data.Task
import com.yujichang.kotlinthings.data.source.TasksDataSource
import com.yujichang.kotlinthings.data.source.TasksRepository
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Matchers.any
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

/**
 *author : jichang
 *time   : 2017/05/18
 *desc   :
 *单元测试执行{@link AddEditTaskPresenter}。
 *version: 1.0
 */
class AddEditTaskPresenterTest {
    @Mock
    private lateinit var mTasksRepository: TasksRepository

    @Mock
    private lateinit var mAddEditTaskView: AddEditTaskContract.View

    /**
     * [ArgumentCaptor] 是一个强大的Mockito API，用于捕获参数值，并使用它们对它们执行进一步的操作或断言。
     */
    @Captor
    private lateinit var mGetTaskCallbackCaptor: ArgumentCaptor<TasksDataSource.GetTaskCallback>

    private lateinit var mAddEditTaskPresenter: AddEditTaskPresenter

    @Before
    fun setupMocksAndView() {
        // Mockito通过使用@Mock注释，非常方便地注入模拟。要在测试中注入模拟，需要调用initMocks方法。
        MockitoAnnotations.initMocks(this)

        // 演示者不会更新视图，除非它是活跃的。
        `when`(mAddEditTaskView.isActive).thenReturn(true)
    }

    @Test
    fun saveNewTaskToRepository_showsSuccessMessageUi() {
        // 参考被测class 的实例
        mAddEditTaskPresenter = AddEditTaskPresenter(
                null, mTasksRepository, mAddEditTaskView, true)

        // When the presenter is asked to save a task
        mAddEditTaskPresenter.saveTask("New Task Title", "Some Task Description")

        // Then a task is saved in the repository and the view updated
        verify(mTasksRepository).saveTask(any(Task::class.java))// saved to the model
        verify(mAddEditTaskView).showTasksList() // shown in the UI
    }

}