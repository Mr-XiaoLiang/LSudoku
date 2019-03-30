package liang.lollipop.lsudoku.drawable

import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Log
import liang.lollipop.lsudoku.util.SudokuController

/**
 * @date: 2019/02/25 21:23
 * @author: lollipop
 * 显示标记内容的Drawable
 */
class SymbolDrawable : Drawable() {

    private val paint = Paint().apply {
        isAntiAlias = true
        isDither = true
    }

    var weightSize = 0.2F
        set(value) {
            field = value
            update()
        }

    var color: Int
        set(value) {
            paint.color = value
            invalidateSelf()
        }
        get() = paint.color

    private var pointSize = 0F

    private var gridSize = 0F

    private val pointStatus = Array(9) { false }

    fun changeStatus(value: Array<Boolean>) {
        val count = Math.min(value.size, pointStatus.size)
        for (index in 0 until count) {
            pointStatus[index] = value[index]
        }
        invalidateSelf()
    }

    fun changeStatus(key: Int) {
        for (index in 0 until pointStatus.size) {
            pointStatus[index] = key.and(SudokuController.Symbol.num1 shl index) > 0
        }
        invalidateSelf()
    }

    private fun update() {
        if (bounds.isEmpty) {
            return
        }
        gridSize = Math.min(bounds.width(), bounds.height()) / 3F
        pointSize = gridSize * weightSize
        invalidateSelf()
    }

    override fun draw(canvas: Canvas) {
        val left = (bounds.width() - gridSize * 3) / 2 + bounds.left
        val top = (bounds.height() - gridSize * 3) / 2 + bounds.top
        for (index in 0 until pointStatus.size) {
            val isDraw = pointStatus[index]
            if (!isDraw) {
                continue
            }
            val x = (index % 3 + 0.5F) * gridSize + left
            val y = (index / 3 + 0.5F) * gridSize + top
            canvas.drawCircle(x, y, pointSize, paint)
        }
        log("bounds: [${bounds.left}, ${bounds.top}, ${bounds.right}, ${bounds.bottom}]")
        log("draw: [${pointStatus[0]}, ${pointStatus[1]}, ${pointStatus[2]}, " +
                "${pointStatus[3]}, ${pointStatus[4]}, ${pointStatus[5]}, " +
                "${pointStatus[6]}, ${pointStatus[7]}, ${pointStatus[8]}]")
    }

    override fun onBoundsChange(bounds: Rect?) {
        super.onBoundsChange(bounds)
        update()
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity() = PixelFormat.TRANSPARENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    private fun log(value: String) {
        Log.d("Lollipop", "SymbolDrawable: $value")
    }
}