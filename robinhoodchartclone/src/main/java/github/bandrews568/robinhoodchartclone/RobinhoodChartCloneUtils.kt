package github.bandrews568.robinhoodchartclone


import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.sign


data class PriceDifference(val whole: Float, val percentage: Float) {
    var isPositive: Boolean = whole.sign == 1.0f
    val formattedString: String by lazy { "%s (%.2f%%)".format(whole.toCurrencyString(), percentage) }
}

data class DataPoint(val price: Float, val timestamp: Long)

fun priceDifference(currentPrice: Float, basePrice: Float): PriceDifference {
    if (currentPrice == 0.0f || basePrice == 0.0f) return PriceDifference(0f, 0f)

    val difference = currentPrice - basePrice
    val percent = difference / basePrice * 100
    return PriceDifference(difference, percent)
}

fun Long.toDateString(timePeriod: TimePeriod): String = SimpleDateFormat(timePeriod.datePattern(), Locale.US).format(this * 1000L)

fun Float.toCurrencyString(): String = NumberFormat.getCurrencyInstance().format(this)
