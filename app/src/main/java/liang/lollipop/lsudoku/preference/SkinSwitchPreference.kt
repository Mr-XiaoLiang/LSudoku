package liang.lollipop.lsudoku.preference

import android.content.Context
import android.preference.SwitchPreference
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import android.view.View
import android.widget.Switch
import liang.lollipop.lsudoku.skin.Skin

/**
 * Created by lollipop on 2018/3/8.
 * @author Lollipop
 * 支持主题的SwitchPreference
 */
class SkinSwitchPreference(context: Context, attrs: AttributeSet?,
                           @AttrRes defStyleAttr:Int, @StyleRes defStyleRes:Int)
    : SwitchPreference(context,attrs,defStyleAttr, defStyleRes) {

    constructor(context: Context, attrs: AttributeSet?,
                @AttrRes defStyleAttr:Int):this(context,attrs,defStyleAttr,0)
    constructor(context: Context, attrs: AttributeSet?):this(context,attrs,0)
    constructor(context: Context):this(context,null)

    private val skinPreferenceUtil = SkinPreferenceUtil()

    override fun onBindView(view: View?) {
        super.onBindView(view)
        skinPreferenceUtil.setBodyView(view)
    }

    fun onSkinChange(skin: Skin){
        skinPreferenceUtil.onSkinChange(skin)
        val switchView = skinPreferenceUtil.find<View>(android.R.id.switch_widget)
        if(switchView != null && switchView is Switch){
//            TintUtil.tintDrawable(switchView.thumbDrawable).setColor(switchView.thumbTintList).tint()
//            TintUtil.tintDrawable(switchView.thumbDrawable).setColor(skin.colorAccent).tint()
        }
    }



}