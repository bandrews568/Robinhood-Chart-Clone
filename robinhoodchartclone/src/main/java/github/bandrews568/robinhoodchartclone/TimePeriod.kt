package github.bandrews568.robinhoodchartclone

/**
 * Used in conjunction with the radio buttons to represent a time period.
 *
 * TODO - Determine if this could be useful: https://developer.android.com/reference/java/time/Period
 */
enum class TimePeriod {
    DAY,
    WEEK,
    MONTH,
    THREE_MONTH,
    YEAR,
    ALL;

    fun datePattern(): String {
        return when (this) {
            TimePeriod.DAY -> "h:mm a z"
            TimePeriod.WEEK -> "h:mm a MMM d"
            TimePeriod.MONTH, TimePeriod.THREE_MONTH, TimePeriod.YEAR, TimePeriod.ALL -> "MMM d, yyyy"
        }
    }

    fun description(): String {
        return when (this) {
            TimePeriod.DAY -> "Past day"
            TimePeriod.WEEK -> "Past week"
            TimePeriod.MONTH -> "Past month"
            TimePeriod.THREE_MONTH -> "Past 3 months"
            TimePeriod.YEAR -> "Past year"
            TimePeriod.ALL -> "All time"
        }
    }
}