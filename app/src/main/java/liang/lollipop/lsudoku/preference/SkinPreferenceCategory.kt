package liang.lollipop.lsudoku.preference

import android.content.Context
import android.preference.PreferenceCategory
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import liang.lollipop.lsudoku.skin.Skin

/**
 * Created by lollipop on 2018/3/8.
 * @author Lollipop
 * 一个支持主题的分组group
 */
class SkinPreferenceCategory(context: Context, attrs: AttributeSet?,
                             @AttrRes defStyleAttr:Int, @StyleRes defStyleRes:Int)
    : PreferenceCategory(context,attrs,defStyleAttr, defStyleRes) {

    constructor(context: Context, attrs: AttributeSet?,
                @AttrRes defStyleAttr:Int):this(context,attrs,defStyleAttr,0)
    constructor(context: Context, attrs: AttributeSet?):this(context,attrs,0)
    constructor(context: Context):this(context,null)

    private val skinPreferenceUtil = SkinPreferenceUtil()

    override fun onCreateView(parent: ViewGroup?): View {
        val view = super.onCreateView(parent)
//        if(view != null){
//            //暂时没有找到原因，因为自定义的Preference的Title字体会很大，所以这里手动进行矫正
//            val titleView:TextView? = view.findViewById(android.R.id.title)
//            titleView?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
//            val summaryView:TextView? = view.findViewById(android.R.id.summary)
//            summaryView?.visibility = View.GONE
//        }
        return view
    }

    override fun onBindView(view: View?) {
        super.onBindView(view)
//        skinPreferenceUtil.setBodyView(view)
    }

    fun onSkinChange(skin: Skin){
        skinPreferenceUtil.onSkinChange(skin)
    }

}