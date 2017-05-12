package com.yujichang.kotlinthings.taskdetail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.yujichang.kotlinthings.R

class TaskDetailActivity : AppCompatActivity() {
   companion object {
        val EXTRA_TASK_ID = "TASK_ID"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.taskdetail_act)
    }
}
