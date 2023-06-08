package pl.edu.pjwstk.s20265.wishlist.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet

class PhotoView(context: Context, attrs: AttributeSet) :
    androidx.appcompat.widget.AppCompatImageView(context, attrs) {

    var text: String = ""
        set(value) {
            field = value
            invalidate()
        }
    var photo: Bitmap? = null
        set(value) {
            field = value
            invalidate()
        }

    private val paintFill = TextPaint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val paintStroke = TextPaint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        drawPhoto(canvas)

        val lines = text.split("\n")

        val textSize = width / 4.0f
        paintFill.textSize = textSize
        paintStroke.apply {
            this.textSize = textSize
            this.strokeWidth = textSize / 28.0f
        }

        val textHeight = paintFill.fontMetrics.descent - paintFill.fontMetrics.ascent
        var lineY = textHeight
        for (line in lines) {
            val textWidth = paintFill.measureText(line)
            canvas.drawText(line, (width - textWidth) / 2f, lineY, paintFill)
            canvas.drawText(line, (width - textWidth) / 2f, lineY, paintStroke)
            lineY += textHeight
        }
    }

    private fun drawPhoto(canvas: Canvas) = photo?.let {
        val rect = Rect(0, 0, width, height)
        canvas.drawBitmap(it, null, rect, paintFill)
        foreground?.alpha = 0 // hide placeholder
    }
}