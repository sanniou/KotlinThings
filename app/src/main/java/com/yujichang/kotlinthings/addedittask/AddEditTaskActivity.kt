package com.yujichang.kotlinthings.addedittask

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.yujichang.kotlinthings.R

class AddEditTaskActivity : AppCompatActivity() {

    companion object {

        val REQUEST_ADD_TASK = 1

        val SHOULD_LOAD_DATA_FROM_REPO_KEY = "SHOULD_LOAD_DATA_FROM_REPO_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addtask_act)
    }
}
