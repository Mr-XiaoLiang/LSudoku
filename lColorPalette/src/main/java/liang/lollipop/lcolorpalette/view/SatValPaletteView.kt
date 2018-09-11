package liang.lollipop.lcolorpalette.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.widget.ImageView
import liang.lollipop.lcolorpalette.drawable.SatValPaletteDrawable

/**
 * Created by lollipop on 2018/1/23.
 * @author Lollipop
 * 饱和度和灰度的选择器
 */
class SatValPaletteView(context: Context, attrs: AttributeSet?, defStyleAttr:Int):
        ImageView(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?):this(context,attrs,0)
    constructor(context: Context):this(context,null)

    private val satValPaletteDrawable = SatValPaletteDrawable()
    private var hsvCallback:HSVCallback? = null

    init {
        setImageDrawable(satValPaletteDrawable)
        satValPaletteDrawable.setSelectRadius(
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 3F, context.resources.displayMetrics))
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if(event == null){
            return super.onTouchEvent(event)
        }

        return when(event.action){

            MotionEvent.ACTION_DOWN,MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                val hsv = satValPaletteDrawable.selectTo(event.x,event.y)
                hsvCallback?.onHSVSelect(hsv, Color.HSVToColor(hsv))
                true
            }

            else -> {
                super.onTouchEvent(event)
            }

        }
    }

    interface HSVCallback{
        fun onHSVSelect(hsv:FloatArray,rgb:Int)
    }

    fun setHSVCallback(hsvCallback: HSVCallback){
        this.hsvCallback = hsvCallback
    }

    fun parser(satValue:Float,valValue:Float){
        val hsv = satValPaletteDrawable.parser(satValue,valValue)
        hsvCallback?.onHSVSelect(hsv, Color.HSVToColor(hsv))
    }

    fun onHueChange(hue:Float){
        satValPaletteDrawable.onHueChange(hue)
        val hsv = satValPaletteDrawable.lastSelected()
        hsvCallback?.onHSVSelect(hsv, Color.HSVToColor(hsv))
    }

}