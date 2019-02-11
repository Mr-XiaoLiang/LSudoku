package liang.lollipop.lsudoku.drawable

import android.graphics.*
import android.graphics.drawable.Drawable

/**
 * Created by lollipop on 2018/3/10.
 * @author Lollipop
 * 夜间模式的Drawable
 */
class NightModeDrawable: Drawable() {

    private val paint = Paint()

    init {

        paint.color = Color.BLACK

    }

    override fun draw(canvas: Canvas?) {
        canvas?.drawRect(bounds,paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
        invalidateSelf()
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }

}