package liang.lollipop.lsudoku.drawable

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

/**
 * Created by lollipop on 2018/3/13.
 * @author Lollipop
 * 方形格子的棋盘绘制器
 */
class GridMapDrawable: SudokuMapDrawable() {

    override fun draw(canvas: Canvas?) {
        if(canvas==null){
            return
        }
        drawOver(canvas)
        drawBorder(canvas)
    }

    private fun drawOver(canvas: Canvas){

        if(selectedX < 0 || selectedY < 0){
            return
        }

        paint.style = Paint.Style.FILL
        paint.strokeWidth = 0F

        val gridWidth = bounds.width().toFloat() / COL
        val gridHeight = bounds.height().toFloat() / ROW

        val selectedLeft = selectedX * gridWidth
        val selectedTop = selectedY * gridHeight
        paint.color = selectedColor
        canvas.drawRect(selectedLeft,selectedTop,selectedLeft+gridWidth,selectedTop+gridHeight,paint)

        if(!showAssociate){
            return
        }

        for(x in 0 until COL){
            for(y in 0 until ROW){

                if(x == selectedX && y == selectedY){
                    //跳过选中项颜色
                    continue
                }

                if(x == selectedX || y == selectedY || (y / 3 == selectedY / 3 && x / 3 == selectedX / 3)){

                    paint.color = associateColor
                    val left = x * gridWidth
                    val top = y * gridHeight
                    canvas.drawRect(left,top,left+gridWidth,top+gridHeight,paint)

                }else if(unselectedColor != Color.TRANSPARENT){

                    paint.color = unselectedColor
                    val left = x * gridWidth
                    val top = y * gridHeight
                    canvas.drawRect(left,top,left+gridWidth,top+gridHeight,paint)

                }

            }
        }

    }

    private fun drawBorder(canvas: Canvas){
        paint.style = Paint.Style.STROKE

        //绘制小格子
        paint.color = gridBorderColor
        paint.strokeWidth = gridBorderWidth
        val gridWidth = bounds.width().toFloat() / COL
        val gridHeight = bounds.height().toFloat() / ROW
        for(index in 1 until  ROW){
            val x = index * gridWidth - gridBorderWidth / 2
            val y = index * gridHeight - gridBorderWidth / 2
            canvas.drawLine(bounds.left.toFloat(),y,bounds.right.toFloat(),y,paint)
            canvas.drawLine(x,bounds.top.toFloat(),x,bounds.bottom.toFloat(),paint)
        }

        //绘制大格子
        paint.color = bigGridBorderColor
        paint.strokeWidth = bigGridBorderWidth
        val bigGridWidth = bounds.width().toFloat() / 3
        val bigGridHeight = bounds.height().toFloat() / 3
        for(index in 1 until 3){
            val x = index * bigGridWidth - bigGridBorderWidth / 2
            val y = index * bigGridHeight - bigGridBorderWidth / 2
            canvas.drawLine(bounds.left.toFloat(),y,bounds.right.toFloat(),y,paint)
            canvas.drawLine(x,bounds.top.toFloat(),x,bounds.bottom.toFloat(),paint)
        }

        paint.color = borderColor
        paint.strokeWidth = borderWidth
        canvas.drawRect(bounds,paint)
    }

}