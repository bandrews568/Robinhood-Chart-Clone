package github.bandrews568.robinhoodchartclone

import com.robinhood.spark.SparkAdapter


class SparkViewAdapter(private var dataPoints: List<DataPoint> = emptyList()) : SparkAdapter() {

    // First price in the list that will be used to calculate the price/percent difference
    var firstPrice: Float = 0.0f
        get() = dataPoints.firstOrNull()?.price ?: 0.0f
        private set

    // Last price in the list that will be used to calculate the price/percent difference
    var lastPrice: Float = 0.0f
        get() = dataPoints.lastOrNull()?.price ?: 0.0f
        private set

    override fun getCount() = dataPoints.size

    override fun getItem(index: Int): DataPoint = dataPoints[index]

    override fun getY(index: Int) = dataPoints[index].price

    internal fun clearDataPoints() {
        dataPoints = emptyList()
        notifyDataSetChanged()
    }

    internal fun updateDataPoints(dataPoints: List<DataPoint>) {
        this.dataPoints = dataPoints
        notifyDataSetChanged()
    }
}