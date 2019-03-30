package liang.lollipop.lsudoku.preference

import android.content.res.ColorStateList
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import liang.lollipop.lsudoku.skin.Skin

/**
 * Created by lollipop on 2018/3/8.
 * @author Lollipop
 * 用于做偏好设置页面的一些主题皮肤设置
 */
class SkinPreferenceUtil {

    private var bodyView: View? = null

    fun setBodyView(view:View?){
        bodyView = view
    }

    fun onSkinChange(skin: Skin){
        if(bodyView != null){
            val titleView: TextView? = bodyView!!.findViewById(android.R.id.title)
            titleView?.setTextColor(skin.colorPrimary)

            val summaryView:TextView? = bodyView!!.findViewById(android.R.id.summary)
            summaryView?.setTextColor(skin.textSecondary)

            val iconView: ImageView? = bodyView!!.findViewById(android.R.id.icon)
            iconView?.imageTintList = ColorStateList.valueOf(skin.colorPrimary)
        }
    }

    fun <T:View> find(id:Int): T? {
        if(bodyView == null || id == 0){
            return null
        }
        return bodyView!!.findViewById(id)
    }

}