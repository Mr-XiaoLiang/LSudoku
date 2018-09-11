package liang.lollipop.lcolorpalette.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageView
import liang.lollipop.lcolorpalette.drawable.HuePaletteDrawable

/**
 * Created by lollipop on 2018/1/22.
 * @author Lollipop
 * 色相显示的View
 */
class HuePaletteView(context: Context, attrs: AttributeSet?, defStyleAttr:Int):
        ImageView(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?):this(context,attrs,0)
    constructor(context: Context):this(context,null)

    private var hueCallback:HueCallback? = null
    private val hueDrawable = HuePaletteDrawable()

    init {
        setImageDrawable(hueDrawable)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if(event == null){
            return super.onTouchEvent(event)
        }

        return when(event.action){

            MotionEvent.ACTION_DOWN,MotionEvent.ACTION_MOVE,MotionEvent.ACTION_UP -> {
                val hue = hueDrawable.selectTo(event.y)
                hueCallback?.onHueSelect(hue)
                true
            }

            else -> {
                super.onTouchEvent(event)
            }

        }
    }

    fun parser(hue: Float){
        hueDrawable.parser(hue)
        hueCallback?.onHueSelect(hue.toInt())
    }

    fun setHueCallback(hueCallback: HueCallback){
        this.hueCallback = hueCallback
    }

    interface HueCallback{
        fun onHueSelect(hue:Int)
    }

}