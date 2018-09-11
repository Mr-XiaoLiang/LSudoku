package liang.lollipop.lsudoku.drawable

import android.graphics.*
import android.graphics.drawable.Drawable

/**
 * Created by lollipop on 2018/2/22.
 * @author Lollipop
 * 数独背景的绘制对象
 */
abstract class SudokuMapDrawable: Drawable() {

    protected var selectedX = -1
    protected var selectedY = -1

    //选中的颜色
    var selectedColor = Color.TRANSPARENT
    //未选中的颜色
    var unselectedColor = Color.TRANSPARENT
    //联想的颜色
    var associateColor = Color.TRANSPARENT
    //边框颜色
    var borderColor = Color.DKGRAY
    //宫格颜色
    var gridBorderColor = Color.LTGRAY
    //大宫格颜色
    var bigGridBorderColor = Color.LTGRAY

    //边框宽度
    var borderWidth = 5F
    //宫格宽度
    var gridBorderWidth = 1F
    //大宫格宽度
    var bigGridBorderWidth = 3F

    var showAssociate = true


    protected val paint = Paint()

    companion object {

        //行数
        const val ROW = 9
        //列数
        const val COL = 9

    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSPARENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    fun refresh(){
        invalidateSelf()
    }

    fun selected(x:Int,y:Int){
        selectedX = x
        selectedY = y
        refresh()
    }

}