package com.codepath.apps.restclienttemplate.Helper

import android.content.Context
import android.text.Selection
import android.widget.TextView
import android.view.MotionEvent
import android.text.Spanned
import android.text.Spannable
import android.text.style.ClickableSpan
import android.util.AttributeSet

class LinkifiedTextView(context: Context?, attrs: AttributeSet?) : TextView(context, attrs) {
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val widget = this as TextView
        val text: Any = widget.text
        if (text is Spanned) {
            val buffer = text as Spannable
            val action = event.action
            if (action == MotionEvent.ACTION_UP
                || action == MotionEvent.ACTION_DOWN
            ) {
                var x = event.x.toInt()
                var y = event.y.toInt()
                x -= widget.totalPaddingLeft
                y -= widget.totalPaddingTop
                x += widget.scrollX
                y += widget.scrollY
                val layout = widget.layout
                val line = layout.getLineForVertical(y)
                val off = layout.getOffsetForHorizontal(line, x.toFloat())
                val link = buffer.getSpans(
                    off, off,
                    ClickableSpan::class.java
                )
                if (link.size != 0) {
                    if (action == MotionEvent.ACTION_UP) {
                        link[0].onClick(widget)
                    } else if (action == MotionEvent.ACTION_DOWN) {
                        Selection.setSelection(
                            buffer,
                            buffer.getSpanStart(link[0]),
                            buffer.getSpanEnd(link[0])
                        )
                    }
                    return true
                }
            }
        }
        return false
    }
}