package pl.edu.pjwstk.s20265.wishlist.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import pl.edu.pjwstk.s20265.wishlist.R

class PhotoView(context: Context, attrs: AttributeSet) :
    androidx.appcompat.widget.AppCompatImageView(context, attrs) {

    var text: String = ""
    var photo: Bitmap? = null
        set(value) {
            field = value
            invalidate()
        }
    private val defaultPaint = Paint()

    override fun onDraw(canvas: Canvas) {
        drawPhoto(canvas)
        canvas.drawText(text, width / 2f, height / 2f, defaultPaint)
    }

    private fun drawPhoto(canvas: Canvas) = photo?.let {
        val rect = Rect(0, 0, width, height)
        canvas.drawBitmap(it, null, rect, defaultPaint)
        foreground?.alpha = 0 // hide placeholder
    }
}