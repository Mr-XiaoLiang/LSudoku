package liang.lollipop.lcolorpalette.view

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.annotation.Nullable
import android.util.AttributeSet
import android.widget.TextView
import liang.lollipop.lcolorpalette.drawable.CircleBgDrawable

/**
 * Created by lollipop on 2017/12/21.
 * 状态的显示View
 * @author Lollipop
 */
class CirclePointView(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr:Int )
    :TextView(context,attrs,defStyleAttr) {

    constructor(context: Context, @Nullable attrs: AttributeSet?):this(context,attrs,0)
    constructor(context: Context):this(context,null)

    private val backgroundDrawable = CircleBgDrawable()

    init {
        if(background is ColorDrawable){
            backgroundDrawable.setColor((background as ColorDrawable).color)
        }
        background = backgroundDrawable
    }

    fun setStatusColor(color:Int){
        backgroundDrawable.setColor(color)
    }

    override fun setBackgroundColor(color: Int) {
        setStatusColor(color)
    }

}