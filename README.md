# Robinhood Chart Clone
Combines the [TickerView](https://github.com/robinhood/ticker) & [SparkView](https://github.com/robinhood/spark) libraries to mimic the functionality of the sparkline chart from the Robinhood stock trading application.

(Images here)

## Usage

Add a `RobinhoodChartClone` to your view:
```xml
<github.bandrews568.robinhoodchartclone.RobinhoodChartClone
        android:id="@+id/robinhood_chart_clone"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        
        <!-- Choices: positive, negative, neutral -->
        app:chart_theme="neutral" />
```

Displaying data is very simple:
  - Make a list with your data represented as `DataPoint` objects
  - call `updateDataPoints(myListOfDataPoints)` with that list
  
*`updateDataPoints(dataPoints: List<DataPoint>)` is currently the only way to update the data points*   
```kotlin
val dataPoints: List<DataPoint> = listOf(DataPoint(34.34, 1527804000L), DataPoint(24.34, 15278044000L))
robinhood_chart_clone.updateDataPoints(dataPoints)
```

Determine what data to show by implementing the `TimePeriodChangeListener` for a callback when a radio button is checked:
```kotlin
class RobinhoodChartCloneSampleActivity : AppCompatActivity(), TimePeriodChangeListener {
  
  override fun onStart() {
      super.onStart()
      robinhood_chart_clone_sample.timePeriodChangeListener = this
  }
 
  override fun timePeriodChange(timePeriod: TimePeriod) {
      // Use the TimePeriod to determine the data points to display
  }
}
```

Reset the chart back to its default state:
```kotlin
robinhood_chart_clone.reset()
```

## Installation
Coming soon

## Libraries used

- [TickerView](https://github.com/robinhood/ticker)
- [SparkView](https://github.com/robinhood/spark)

## Credits
[Robinhood](https://www.robinhood.com/)

## License
[MIT](https://choosealicense.com/licenses/mit/)
