package github.bandrews568.robinhoodchartclone

import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.math.roundToInt

class RobinhoodChartCloneUtilsTest {

    @Test
    fun priceAndPercentIsCorrect() {
        val (increaseWhole , percentageIncrease) = calculatePriceDifference(40f, 33f)
        assertEquals(7f, increaseWhole)
        // Round to int just for testing purposes
        assertEquals(21, percentageIncrease.roundToInt())

        val (zeroValue, percentNotZero) = calculatePriceDifference(0f, 34f)
        assertEquals(0f, zeroValue)
        assertEquals(0, percentNotZero.roundToInt())

        val (wholeDecrease , percentageDecrease) = calculatePriceDifference(30f, 40f)
        assertEquals(-10f, wholeDecrease)
        assertEquals(-25, percentageDecrease.roundToInt())

        val (bothZeroWhole, bothZeroPercentage) = calculatePriceDifference(0f, 0f)
        assertEquals(0f, bothZeroWhole)
        assertEquals(0, bothZeroPercentage.roundToInt())
    }

    @Test
    fun correctPricePercentageFormattedText() {
        assertEquals("$1.00 (2.90%)", formatPriceDifferenceText(35.4534f, 34.45343f))
        assertEquals("$7.00 (21.21%)", formatPriceDifferenceText(40f, 33f))
        assertEquals("($10.00) (-25.00%)", formatPriceDifferenceText(30f, 40f))
    }

    @Test
    fun toCurrencyCorrectFormatUSD() {
        assertEquals("$34.56", 34.56f.toCurrencyString())
        assertEquals("$12.54", 12.5444f.toCurrencyString())
        assertEquals("$0.08", 0.08f.toCurrencyString())
        assertEquals("$0.00", 0.0006f.toCurrencyString())
    }

    @Test
    fun correctDateStringFormat() {
        // Thursday, May 17, 2018 12:00:00 PM GMT-04:00
        val epochTimestamp: Long = 1526572800

        // Day pattern
        assertEquals("12:00 PM EDT", epochTimestamp.toDateString(TimePeriod.DAY))
        // Week pattern
        assertEquals("12:00 PM May 17", epochTimestamp.toDateString(TimePeriod.WEEK))
        // Month pattern
        assertEquals("May 17, 2018", epochTimestamp.toDateString(TimePeriod.MONTH))
    }
}