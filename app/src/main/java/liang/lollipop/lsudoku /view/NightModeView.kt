package liang.lollipop.lsudoku.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import liang.lollipop.lsudoku.drawable.NightModeDrawable
import liang.lollipop.lsudoku.util.LSettings

/**
 * Created by lollipop on 2018/3/10.
 * @author Lollipop
 * 夜间模式的View
 */
class NightModeView(context: Context, attrs: AttributeSet?, defStyleAttr:Int, defStyleRes:Int)
    : View(context,attrs, defStyleAttr,defStyleRes) {

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr:Int):this(context,attrs,defStyleAttr,0)

    constructor(context: Context, attrs: AttributeSet?):this(context,attrs,0)

    constructor(context: Context):this(context,null)

    private val nightModeDrawable = NightModeDrawable()

    var level:Int
    set(value) {
        nightModeDrawable.alpha = value
    }
    get() {
        return nightModeDrawable.alpha
    }

    init {

        background = nightModeDrawable
        level = LSettings.getNightModeLevel(context)

        if(isInEditMode){
            level = 50
        }

    }



}