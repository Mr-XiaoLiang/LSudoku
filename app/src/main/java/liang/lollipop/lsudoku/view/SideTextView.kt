package liang.lollipop.lsudoku.view

import android.content.Context
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import android.widget.TextView

/**
 * Created by lollipop on 2018/3/5.
 * @author Lollipop
 * 侧面立着的TextView
 */
class SideTextView(context: Context, attrs: AttributeSet?,
                   @AttrRes defStyleAttr:Int, @StyleRes defStyleRes:Int)
    : TextView(context,attrs,defStyleAttr, defStyleRes) {

    constructor(context: Context, attrs: AttributeSet?,
                @AttrRes defStyleAttr:Int):this(context,attrs,defStyleAttr,0)
    constructor(context: Context, attrs: AttributeSet?):this(context,attrs,0)
    constructor(context: Context):this(context,null)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(heightMeasureSpec,widthMeasureSpec)
        val rota = rotation.toInt()
        if(rota == 90 || rota == 270){
            setMeasuredDimension(height,width)
        }
    }

}