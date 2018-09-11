package liang.lollipop.lcolorpalette.drawable

import android.graphics.*
import android.graphics.drawable.Drawable


/**
 * Created by lollipop on 2017/12/21.
 * @author Lollipop
 * 圆形背景的绘制器
 */
class CircleBgDrawable:Drawable() {

    private val paint = Paint()
    private var bound: RectF? = null
    private var biggestCorners = true
    private var corners = 0F

    init{
        paint.isAntiAlias = true
        paint.isDither = true
        paint.color = 0xFFFF3C16.toInt()
        paint.style = Paint.Style.FILL
        paint.strokeJoin = Paint.Join.MITER
        paint.strokeCap = Paint.Cap.BUTT
    }

    override fun draw(canvas: Canvas) {
        canvas.drawRoundRect(bound, corners, corners, paint)
    }

    override fun setAlpha(i: Int) {
        paint.alpha = i
        invalidateSelf()
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
        invalidateSelf()
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        this.bound = RectF(bounds.left.toFloat(), bounds.top.toFloat(), bounds.right.toFloat(), bounds.bottom.toFloat())
        if (biggestCorners) {
            corners = Math.min(bounds.centerX().toFloat(), bounds.centerY().toFloat())
        }
    }

    fun setColor(color: Int) {
        paint.color = color
        invalidateSelf()
    }

}