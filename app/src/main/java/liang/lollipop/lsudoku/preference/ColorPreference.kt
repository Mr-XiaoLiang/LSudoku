package liang.lollipop.lsudoku.preference

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.preference.DialogPreference
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import liang.lollipop.lcolorpalette.ColorPickerDialog
import liang.lollipop.lcolorpalette.drawable.CircleBgDrawable
import liang.lollipop.lsudoku.R
import liang.lollipop.lsudoku.skin.Skin


/**
 * Created by lollipop on 2018/3/7.
 * @author Lollipop
 * 颜色选择的偏好设置
 */
class ColorPreference(context: Context, attrs: AttributeSet?,
                      @AttrRes defStyleAttr:Int, @StyleRes defStyleRes:Int)
    : DialogPreference(context,attrs,defStyleAttr, defStyleRes),ColorPickerDialog.OnColorSelectedListener {

    constructor(context: Context, attrs: AttributeSet?,
                @AttrRes defStyleAttr:Int):this(context,attrs,defStyleAttr,0)
    constructor(context: Context, attrs: AttributeSet?):this(context,attrs,0)
    constructor(context: Context):this(context,null)

    private var defaultColor = Color.BLACK
    private val colorDrawable = CircleBgDrawable()
    private var valueView:TextView? = null

    private val skinPreferenceUtil = SkinPreferenceUtil()

    companion object {

        private const val COLOR_WIDGET = R.layout.preference_color

    }

    init {

        widgetLayoutResource = COLOR_WIDGET
        val a:TypedArray = context.obtainStyledAttributes(
                attrs, R.styleable.ColorPreference, defStyleAttr, defStyleRes)
        defaultColor = a.getColor(R.styleable.ColorPreference_defaultColor, Color.GRAY)
        a.recycle()

    }

    override fun onCreateView(parent: ViewGroup?): View {
        val view = super.onCreateView(parent)
        //暂时没有找到原因，因为自定义的Preference的Title字体会很大，所以这里手动进行矫正
        val titleView:TextView? = view.findViewById(android.R.id.title)
        titleView?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)

        val imageView:ImageView = view.findViewById(R.id.preference_color_show)
        imageView.setImageDrawable(colorDrawable)
        valueView = view.findViewById<View>(R.id.preference_color_value) as TextView

        return view
    }

    override fun onBindView(view: View) {
        super.onBindView(view)
        onColorChange()
    }

    fun onSkinChange(skin: Skin){
        skinPreferenceUtil.onSkinChange(skin)
        valueView?.setTextColor(skin.textPrimary)
    }

    override fun onClick() {
        ColorPickerDialog
                .create(context,getColor(),this)
                .show()
    }

    private fun getColor(): Int {
        val color = getPersistedInt(defaultColor)
        return if(color == 0){defaultColor}else{color}
    }

    private fun onColorChange() {
        val color = getColor()
        colorDrawable.setColor(color)
        valueView?.text = getColorValue(color)
    }

    private fun getColorValue(color: Int): String {
        if (color == 0) {
            return "#000000"
        }
        val red = Color.red(color).formatNum()
        val green = Color.green(color).formatNum()
        val blue = Color.blue(color).formatNum()
        return "#$red$green$blue"
    }

    private fun Int.formatNum(): String{
        val numStr = Integer.toHexString(this).toUpperCase()
        return if(this < 0x10){ "0$numStr" }else{ numStr }
    }

    override fun onColorSelected(dialog: ColorPickerDialog, color: Int) {
        if(callChangeListener(color)){
            persistInt(color)
            onColorChange()
        }
        dialog.dismiss()
    }

}