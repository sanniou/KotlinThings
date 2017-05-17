package com.yujichang.kotlinthings.statistics

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yujichang.kotlinthings.R
import kotlinx.android.synthetic.main.statistics_frag.*

/**
 *author : jichang
 *time   : 2017/05/17
 *desc   : complete
 *version: 1.0
 */
class StatisticsFragment : Fragment()
        , StatisticsContract.View {
    private lateinit var mPresenter: StatisticsContract.Presenter

    override fun setPresenter(presenter: StatisticsContract.Presenter) {
        mPresenter = presenter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.statistics_frag, container, false)
    }

    override fun onResume() {
        super.onResume()
        mPresenter.start()
    }

    override fun setProgressIndicator(active: Boolean) {
        if (active) {
            statistics.text = getString(R.string.loading)
        } else {
            statistics.text = ""
        }
    }

    override fun showStatistics(numberOfIncompleteTasks: Int, numberOfCompletedTasks: Int) {
        if (numberOfCompletedTasks == 0 && numberOfIncompleteTasks == 0) {
            statistics.text = resources.getString(R.string.statistics_no_tasks)
        } else {
            val displayString = "${resources.getString(R.string.statistics_active_tasks)} " +
                    "${(+numberOfIncompleteTasks)}\n" +
                    "${resources.getString(R.string.statistics_completed_tasks)} " +
                    "$numberOfCompletedTasks"
            statistics.text = displayString
        }
    }

    override fun showLoadingStatisticsError() {
        statistics.text = resources.getString(R.string.statistics_error)
    }

    override val isActive: Boolean
        get() = isAdded
}