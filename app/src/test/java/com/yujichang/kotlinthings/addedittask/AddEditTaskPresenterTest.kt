package com.yujichang.kotlinthings.addedittask

import com.yujichang.kotlinthings.data.Task
import com.yujichang.kotlinthings.data.source.TasksDataSource
import com.yujichang.kotlinthings.data.source.TasksRepository
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Matchers.anyString
import org.mockito.Matchers.eq
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
    fun createPresenter_setsThePresenterToView() {
        // Get a reference to the class under test
        mAddEditTaskPresenter = AddEditTaskPresenter(
                null, mTasksRepository, mAddEditTaskView, true)

        // Then the presenter is set to the view
        verify(mAddEditTaskView).setPresenter(mAddEditTaskPresenter)
    }

    @Test
    fun saveNewTaskToRepository_showsSuccessMessageUi() {
        // 参考被测class 的实例
        mAddEditTaskPresenter = AddEditTaskPresenter(
                null, mTasksRepository, mAddEditTaskView, true)

        // When the presenter is asked to save a task
        mAddEditTaskPresenter.saveTask("New Task Title", "Some Task Description")

        // Then a task is saved in the repository and the view updated
        verify(mTasksRepository).saveTask(Task("New Task Title", "Some Task Description", anyString(), false))// saved to the model
        verify(mAddEditTaskView).showTasksList() // shown in the UI
    }

    @Test
    fun saveTask_emptyTaskShowsErrorUi() {
        // Get a reference to the class under test
        mAddEditTaskPresenter = AddEditTaskPresenter(
                null, mTasksRepository, mAddEditTaskView, true)

        // When the presenter is asked to save an empty task
        mAddEditTaskPresenter.saveTask("", "")

        // Then an empty not error is shown in the UI
        verify(mAddEditTaskView).showEmptyTaskError()
    }


    @Test
    fun saveExistingTaskToRepository_showsSuccessMessageUi() {
        // Get a reference to the class under test
        mAddEditTaskPresenter = AddEditTaskPresenter(
                "1", mTasksRepository, mAddEditTaskView, true)

        // When the presenter is asked to save an existing task
        mAddEditTaskPresenter.saveTask("Existing Task Title", "Some Task Description")

        // Then a task is saved in the repository and the view updated
        verify(mTasksRepository).saveTask(Task("Existing Task Title", "Some Task Description", anyString())) // saved to the model
        verify(mAddEditTaskView).showTasksList() // shown in the UI
    }

    /**
     * 测试失败
     */
    @Test
    fun populateTask_callsRepoAndUpdatesView() {
        val testTask = Task("TITLE", "DESCRIPTION")
        // Get a reference to the class under test
        mAddEditTaskPresenter = AddEditTaskPresenter(testTask.id,
                mTasksRepository, mAddEditTaskView, true)

        // When the presenter is asked to populate an existing task
        mAddEditTaskPresenter.populateTask()

        // Then the task repository is queried and the view updated
        verify(mTasksRepository).getTask(eq(testTask.id), mGetTaskCallbackCaptor.capture())//capture() return null 辣鸡啊
        assertThat(mAddEditTaskPresenter.isDataMissing, `is`<Boolean>(true))

        // Simulate callback
        mGetTaskCallbackCaptor.value.onTaskLoaded(testTask)

        verify(mAddEditTaskView).setTitle(testTask.title)
        verify(mAddEditTaskView).setDescription(testTask.description)
        assertThat(mAddEditTaskPresenter.isDataMissing, `is`<Boolean>(false))
    }


}