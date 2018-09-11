package liang.lollipop.lcolorpalette

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.media.Image
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import liang.lollipop.lcolorpalette.view.CirclePointView
import liang.lollipop.lcolorpalette.view.HuePaletteView
import liang.lollipop.lcolorpalette.view.SatValPaletteView

/**
 * Created by lollipop on 2018/1/23.
 * 颜色选择板的Dialog
 * @author Lollipop
 */
class ColorPickerDialog private constructor(context: Context,private val lastColor:Int,
                                            private val onColorSelectedListener:OnColorSelectedListener)
    : Dialog(context), HuePaletteView.HueCallback, SatValPaletteView.HSVCallback,
    View.OnClickListener{

    private lateinit var huePaletteView: HuePaletteView
    private lateinit var satValPaletteView: SatValPaletteView
    private lateinit var colorValueView: TextView
    private lateinit var lastColorView: CirclePointView
    private lateinit var newColorView: CirclePointView
    private lateinit var colorDoneBtn: ImageView
    private lateinit var colorArrowView: ImageView

    private var selectedColor = lastColor

    companion object {

        fun init(colorPrimary: Int, colorAccent: Int){
            Constants.colorAccent = colorAccent
            Constants.colorPrimary = colorPrimary
        }

        fun create(context: Context,lastColor: Int,
                   onColorSelectedListener: OnColorSelectedListener):ColorPickerDialog{
            return ColorPickerDialog(context,lastColor,onColorSelectedListener)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 去除屏幕title
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_color_picker)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setGravity(Gravity.CENTER)
        initView()
    }

    private fun initView(){

        huePaletteView = findViewById(R.id.huePalette)
        satValPaletteView = findViewById(R.id.satValPalette)
        colorValueView = findViewById(R.id.colorValue)
        lastColorView = findViewById(R.id.lastColor)
        newColorView = findViewById(R.id.newColor)
        colorDoneBtn = findViewById(R.id.colorDone)
        colorArrowView = findViewById(R.id.arrowView)

        TintUtil.tintDrawable(context,R.drawable.ic_done_white_24dp)
                .mutate()
                .setColor(Constants.colorAccent)
                .withImageSrc(colorDoneBtn)
        TintUtil.tintDrawable(context,R.drawable.ic_arrow_forward_black_24dp)
                .mutate()
                .setColor(Constants.colorPrimary)
                .withImageSrc(colorArrowView)

        newColorView.setOnClickListener(this)
        colorDoneBtn.setOnClickListener(this)
        huePaletteView.setHueCallback(this)
        satValPaletteView.setHSVCallback(this)

        lastColorView.setStatusColor(lastColor)
        newColorView.setStatusColor(lastColor)

        val typeFace = Typeface.createFromAsset(context.assets,"fonts/number.otf")
        colorValueView.typeface = typeFace
        colorValueView.text = colorValue(lastColor)

        val hsv = FloatArray(3)
        Color.colorToHSV(lastColor,hsv)
        huePaletteView.parser(hsv[0])
        satValPaletteView.parser(hsv[1],hsv[2])
    }

    override fun onHueSelect(hue: Int) {
        satValPaletteView.onHueChange(hue.toFloat())
    }

    override fun onHSVSelect(hsv: FloatArray, rgb: Int) {
        colorValueView.text = colorValue(rgb)
        newColorView.setStatusColor(rgb)
        selectedColor = rgb
    }

    private fun colorValue(color:Int):String{
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return toHex(red)+toHex(green)+toHex(blue)
    }

    private fun toHex(value:Int):String{
        return Integer.toHexString(value)
                .let {
                    if(value < 0x10){
                        "0$it"
                    }else{
                        it
                    }
                }.toUpperCase()
    }

    override fun onClick(v: View?) {

        when(v){

            newColorView -> {
                onColorSelectedListener.onColorSelected(this,selectedColor)
            }

        }

    }

    interface OnColorSelectedListener{
        fun onColorSelected(dialog: ColorPickerDialog,color:Int)
    }

}
