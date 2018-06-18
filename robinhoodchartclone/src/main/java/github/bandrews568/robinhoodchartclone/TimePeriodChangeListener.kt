package github.bandrews568.robinhoodchartclone

/**
 * Callback interface used to listen for [TimePeriod] changes on [R.id.radio_group_rcc]
 */
interface TimePeriodChangeListener {

    /**
     * TimePeriod change from a radio button being clicked.
     */
    fun timePeriodChange(timePeriod: TimePeriod)
}