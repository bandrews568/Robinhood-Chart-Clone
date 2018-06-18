package github.bandrews568.robinhoodchartclone

import android.text.TextPaint
import android.text.style.CharacterStyle


class TextColorSpan: CharacterStyle() {

    var color = ChartTheme.NEUTRAL.color()

    override fun updateDrawState(tp: TextPaint?) {
        tp?.color = color
    }
}