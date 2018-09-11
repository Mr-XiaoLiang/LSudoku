package liang.lollipop.lsudoku.view

import android.content.Context
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import android.widget.FrameLayout
import liang.lollipop.lsudoku.R

/**
 * Created by lollipop on 2018/2/22.
 * @author Lollipop
 * 矩形的FrameLayout
 */
open class SquareFrameLayout(context: Context, attrs: AttributeSet?,
                        @AttrRes defStyleAttr:Int, @StyleRes defStyleRes:Int)
    : FrameLayout(context,attrs,defStyleAttr, defStyleRes){

    constructor(context: Context, attrs: AttributeSet?,
                @AttrRes defStyleAttr:Int):this(context,attrs,defStyleAttr,0)
    constructor(context: Context, attrs: AttributeSet?):this(context,attrs,0)
    constructor(context: Context):this(context,null)

    private var maxWidth = 0

    companion object {

        private const val DEF_MAX_WIDTH = -1

    }

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.SquareFrameLayout)

        maxWidth = array.getDimensionPixelSize(R.styleable.SquareFrameLayout_maxWidth, DEF_MAX_WIDTH)

        array.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var newWidthMeasureSpec = widthMeasureSpec
        var newHeightMeasureSpec = heightMeasureSpec
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        if(widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY){
            //如果已经固定了，那么不再修改
        }else if(widthMode == MeasureSpec.EXACTLY){
            //如果只有宽度固定，那么调整高度
            val heightSize = MeasureSpec.getSize(widthMeasureSpec)//.maxValue()
            newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize,MeasureSpec.EXACTLY)
//            newWidthMeasureSpec = newHeightMeasureSpec
        }else if(heightMode == MeasureSpec.EXACTLY){
            //如果只有高度固定，那么调整宽度
            val widthSize = MeasureSpec.getSize(heightMeasureSpec)//.maxValue()
            newWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize,MeasureSpec.EXACTLY)
//            newHeightMeasureSpec = newWidthMeasureSpec
        }
        super.onMeasure(newWidthMeasureSpec, newHeightMeasureSpec)
    }

    private fun Int.maxValue(): Int{
        if(maxWidth < 0){
            return this
        }
        if( maxWidth < this ){
            return maxWidth
        }

        return this
    }

}