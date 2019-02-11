package liang.lollipop.lsudoku.view

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import liang.lollipop.lsudoku.R

/**
 * Created by lollipop on 2018/2/22.
 * @author lollipop
 * 自动字体大小的TextView
 */
class AutoSizeTextView(context: Context, attrs: AttributeSet?, defStyleAttr:Int)
    : AppCompatTextView(context,attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?):this(context,attrs,0)
    constructor(context: Context):this(context,null)

    var fontSize = DEFAULT_FONT_SIZE

    companion object {

        private const val DEFAULT_FONT_SIZE = 0.7F

    }

    init {

        gravity = Gravity.CENTER

        val array = context.obtainStyledAttributes(attrs, R.styleable.AutoSizeTextView)

        fontSize = array.getFloat(R.styleable.AutoSizeTextView_fontSize, DEFAULT_FONT_SIZE)

        array.recycle()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        if(widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY){
            //如果已经固定了，那么调整字体大小，否则不执行
            val size = Math.min(MeasureSpec.getSize(widthMeasureSpec),MeasureSpec.getSize(heightMeasureSpec)) * fontSize
            setTextSize(TypedValue.COMPLEX_UNIT_PX,size)
        }
    }

    fun resetFontSize(size:Float){
        fontSize = size
        setTextSize(TypedValue.COMPLEX_UNIT_PX,Math.min(width,height) * fontSize)
    }

}