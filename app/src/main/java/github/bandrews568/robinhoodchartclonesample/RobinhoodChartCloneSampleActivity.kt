package github.bandrews568.robinhoodchartclonesample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import github.bandrews568.robinhoodchartclone.DataPoint
import github.bandrews568.robinhoodchartclone.TimePeriod
import github.bandrews568.robinhoodchartclone.TimePeriodChangeListener
import kotlinx.android.synthetic.main.activity_robinhood_chart_clone_sample.*
import java.util.*

class RobinhoodChartCloneSampleActivity : AppCompatActivity(), TimePeriodChangeListener {


    private val random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_robinhood_chart_clone_sample)
    }

    override fun onStart() {
        super.onStart()

        robinhood_chart_clone_sample.timePeriodChangeListener = this.also {  timePeriodChange(TimePeriod.DAY) }

        switch_chart_enabled.setOnCheckedChangeListener { _, isChecked ->
            robinhood_chart_clone_sample.isEnabled = isChecked
            btn_refresh.isEnabled = isChecked
            btn_reset.isEnabled = isChecked
        }

        btn_refresh.setOnClickListener { timePeriodChange(robinhood_chart_clone_sample.timePeriodSelected) }

        btn_reset.setOnClickListener { if (robinhood_chart_clone_sample.isEnabled) robinhood_chart_clone_sample.reset() }
    }

    override fun timePeriodChange(timePeriod: TimePeriod) = robinhood_chart_clone_sample.updateDataPoints(sparkViewDummyData(timePeriod))

    private fun sparkViewDummyData(timePeriod: TimePeriod): List<DataPoint> {
        // GMT: Friday, June 1, 2018 12:00:00 AM
        var epochTime = 1527804000L

        val timeIncrease = when (timePeriod) {
            TimePeriod.DAY -> 1800 // Thirty minutes
            TimePeriod.WEEK -> 14400 // Four hours
            TimePeriod.MONTH, TimePeriod.THREE_MONTH -> 86400 // One day
            TimePeriod.YEAR, TimePeriod.ALL -> 604800 // Week
        }

        // Total amount of data points to generate for the TimePeriod
        val total = when (timePeriod) {
            TimePeriod.DAY -> 48 // One day in thirty minute intervals
            TimePeriod.WEEK -> 42 // One week in four hour intervals
            TimePeriod.MONTH, TimePeriod.THREE_MONTH -> 30 // One month in day intervals
            TimePeriod.YEAR, TimePeriod.ALL -> 52 // One year in week intervals
        }

        return (0 until total).map {
            epochTime += timeIncrease
            DataPoint(random.nextFloat() * 5, epochTime)
        }
    }
}
