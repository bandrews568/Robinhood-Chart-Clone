package github.bandrews568.robinhoodchartclone

import android.content.Context
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.text.SpanWatcher
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.CharacterStyle
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.robinhood.spark.SparkView
import com.robinhood.ticker.TickerUtils
import kotlinx.android.synthetic.main.robinhood_chart_clone.view.*
import kotlin.properties.Delegates

/**
 * TODO:
 *  - Ability to show/hide a TextView with an error message
 *  - Baseline for the SparkView
 *  - TickerView and SparkView xml attributes
 */
class RobinhoodChartClone @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {

    /**
     * Extension property that returns a list of every [RadioButton] inside a [RadioGroup]
     */
    private val RadioGroup.radioButtons
        get() = (0 until childCount).mapNotNull { getChildAt(it) as? RadioButton }

    private var sparkViewAdapter: SparkViewAdapter = SparkViewAdapter()

    var timePeriodChangeListener: TimePeriodChangeListener? = null

    var timePeriodSelected: TimePeriod = TimePeriod.DAY
        private set

    private var chartTheme: ChartTheme by Delegates.observable(ChartTheme.NEUTRAL) { _, old, new -> if (old != new) changeTheme() }

    private val textColorSpan = TextColorSpan()

    private var priceDifferenceSpannable: SpannableStringBuilder = SpannableStringBuilder()

    init {
        LayoutInflater.from(context).inflate(R.layout.robinhood_chart_clone, this, true)
        initAttrs(context, attrs, defStyleAttr)

        /*
         * Spannable.Factory is used to avoid the extra CharSequence object allocation each time
         * the text or spans are updated.
         *
         * Source: https://medium.com/google-developers/underspanding-spans-1b91008b97e4
         */
        text_price_difference_spannable_rcc.setSpannableFactory(object : Spannable.Factory() {
            override fun newSpannable(source: CharSequence?): Spannable = source as Spannable
        })
        text_price_difference_spannable_rcc.setText(priceDifferenceSpannable, TextView.BufferType.EDITABLE)

        ticker_rcc.textSize = 100f
        ticker_rcc.setCharacterList(TickerUtils.getDefaultNumberList())

        spark_view_rcc.scrubListener = SparkView.OnScrubListener { updatePriceDifferenceSpannable(it as? DataPoint) }
        spark_view_rcc.isScrubEnabled = true
        spark_view_rcc.adapter = sparkViewAdapter

        radio_group_rcc.setOnCheckedChangeListener { _, checkedId ->
            timePeriodSelected = when (checkedId) {
                R.id.rb_1d_rcc -> TimePeriod.DAY
                R.id.rb_1w_rcc -> TimePeriod.WEEK
                R.id.rb_1m_rcc -> TimePeriod.MONTH
                R.id.rb_3m_rcc -> TimePeriod.THREE_MONTH
                R.id.rb_1y_rcc -> TimePeriod.YEAR
                R.id.rb_all_rcc -> TimePeriod.ALL
                else -> throw IllegalArgumentException("Unknown radio button id: $checkedId")
            }
            timePeriodChangeListener?.timePeriodChange(timePeriodSelected)
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        updateChartEnabled(enabled)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val arr = context.obtainStyledAttributes(attrs, R.styleable.RobinhoodChartClone, defStyleAttr, 0)
        val attrChartTheme = arr.getInt(R.styleable.RobinhoodChartClone_chart_theme, 0)

        chartTheme = when (attrChartTheme) {
            0 -> ChartTheme.POSITIVE
            1 -> ChartTheme.NEGATIVE
            2 -> ChartTheme.NEUTRAL
            else -> throw RuntimeException("Invalid ChartTheme: $attrChartTheme")
        }

        arr.recycle()
    }

    private fun changeTheme() {
        spark_view_rcc.lineColor = chartTheme.color()

        radio_group_rcc.radioButtons.forEach {
            it.setBackgroundResource(chartTheme.backgroundDrawable())
            it.setTextColor(chartTheme.colorStateList(context))
        }
    }

    private fun updateChartEnabled(enabled: Boolean) {
        radio_group_rcc.radioButtons.forEach { it.isEnabled = enabled }

        val themeColor = if (enabled) chartTheme.color() else ChartTheme.NEUTRAL.color()
        val textColor = if (enabled) Color.BLACK else ChartTheme.NEUTRAL.color()

        spark_view_rcc.isScrubEnabled = enabled
        spark_view_rcc.lineColor = themeColor

        ticker_rcc.textColor = textColor

        with(priceDifferenceSpannable) {
            if (isNotEmpty()) {
                val spanEnd = getSpanEnd(textColorSpan)
                textColorSpan.color = themeColor
                setSpan(textColorSpan, 0, spanEnd, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)

                text_price_difference_spannable_rcc.text = this
                text_price_difference_spannable_rcc.setTextColor(textColor)
                text_price_difference_spannable_rcc.invalidate()
            }
        }
    }

    /**
     * A null value is passed when the user has stopped scrubbing or there was an attempt
     * to scrub when [SparkViewAdapter.dataPoints] is empty.
     */
    private fun updatePriceDifferenceSpannable(dataPoint: DataPoint?) {
        val basePrice = sparkViewAdapter.firstPrice
        val currentPrice = dataPoint?.price ?: sparkViewAdapter.lastPrice
        val date = dataPoint?.timestamp?.toDateString(timePeriodSelected) ?: timePeriodSelected.description()

        val priceDifference = priceDifference(currentPrice, basePrice)

        textColorSpan.color = if (priceDifference.isPositive) ChartTheme.POSITIVE.color() else ChartTheme.NEGATIVE.color()

        ticker_rcc.text = currentPrice.toCurrencyString()

        priceDifferenceSpannable.apply {
            val priceTextEndIndex = if (isNotEmpty()) getSpanEnd(textColorSpan) else length

            // Replace price text
            replace(0, priceTextEndIndex, priceDifference.formattedString)
            setSpan(textColorSpan, 0, priceDifference.formattedString.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)

            // Replace date text
            replace(priceDifference.formattedString.length, this.length, " $date")
        }.also {
            text_price_difference_spannable_rcc.text = it
            text_price_difference_spannable_rcc.invalidate()
        }
    }

    /**
     * Resets the chart theme back to [ChartTheme.NEUTRAL], clears [SparkViewAdapter.dataPoints], and
     * sets all text to their respective default values.
     */
    fun reset() {
        sparkViewAdapter.clearDataPoints()
        chartTheme = ChartTheme.NEUTRAL
        spark_view_rcc.invalidate()

        ticker_rcc.text = "$0.00"

        priceDifferenceSpannable.clear()
        text_price_difference_spannable_rcc.text = priceDifferenceSpannable
        text_price_difference_spannable_rcc.invalidate()
    }

    /**
     * Update the [SparkViewAdapter.dataPoints], update all text with data from the new
     * data points.
     * @param dataPoints dataPoints to completely replace the existing data points
     */
    fun updateDataPoints(dataPoints: List<DataPoint>)  {
        sparkViewAdapter.updateDataPoints(dataPoints)
        // Determine chart theme from the new data points
        chartTheme = if (sparkViewAdapter.lastPrice > sparkViewAdapter.firstPrice) ChartTheme.POSITIVE else ChartTheme.NEGATIVE
        spark_view_rcc.invalidate()
        updatePriceDifferenceSpannable(null)
    }
}