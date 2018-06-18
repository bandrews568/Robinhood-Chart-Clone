package github.bandrews568.robinhoodchartclone

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.support.v4.content.ContextCompat

enum class ChartTheme {
    POSITIVE,
    NEGATIVE,
    NEUTRAL;

    fun color(): Int {
        return when (this) {
            POSITIVE -> Color.parseColor("#21ce99")
            NEGATIVE -> Color.parseColor("#f45531")
            NEUTRAL -> Color.parseColor("#cbcbcd")
        }
    }

    fun backgroundDrawable(): Int {
        return when (this) {
            POSITIVE -> R.drawable.rcc_radio_button_background_positive
            NEGATIVE -> R.drawable.rcc_radio_button_background_negative
            NEUTRAL -> R.drawable.rcc_radio_button_background_neutral
        }
    }

    fun colorStateList(context: Context): ColorStateList? {
        return when (this) {
            POSITIVE -> ContextCompat.getColorStateList(context, R.color.rcc_positive_color_state)
            NEGATIVE -> ContextCompat.getColorStateList(context, R.color.rcc_negative_color_state)
            NEUTRAL -> ContextCompat.getColorStateList(context, R.color.rcc_neutral_color_state)
        }
    }
}