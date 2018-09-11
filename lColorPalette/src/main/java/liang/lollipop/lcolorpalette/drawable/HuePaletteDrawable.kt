package liang.lollipop.lcolorpalette.drawable

import android.graphics.*
import android.graphics.drawable.Drawable

/**
 * Created by lollipop on 2018/1/22.
 * 色相调色板
 */
class HuePaletteDrawable: Drawable() {

    private val paint = Paint()
    private val hueColor = IntArray(361)
    private val linePaint = Paint()
    private var selectHue = 0

    init {

        paint.isAntiAlias = true
        paint.isDither = true
        linePaint.isAntiAlias = true
        linePaint.isDither = true
        linePaint.color = Color.WHITE

        for((count, i) in ((hueColor.size-1) downTo 0).withIndex()){
            hueColor[count] = Color.HSVToColor(floatArrayOf(i.toFloat(), 1f, 1f))
        }

    }

    override fun draw(canvas: Canvas?) {

        if(canvas==null){
            return
        }

        canvas.drawRect(bounds,paint)

        val selectY = getSelectY()
        canvas.drawLine(bounds.left.toFloat(),selectY,
                bounds.right.toFloat(),selectY,linePaint)

    }

    override fun onBoundsChange(bounds: Rect?) {
        super.onBoundsChange(bounds)
        if(bounds==null){
            return
        }
        val hueShader = LinearGradient(
                bounds.left.toFloat(),bounds.top.toFloat(),
                bounds.left.toFloat(),bounds.bottom.toFloat(),
                hueColor,null,Shader.TileMode.CLAMP)

        paint.shader = hueShader
        invalidateSelf()
    }

    fun selectTo(y:Float):Int{
        //计算高度对应的色相值序号
        val values = y / bounds.height() * 361 + 0.5
        //得到色相值
        selectHue = hueColor.size-values.toInt()
        if(selectHue<0){
            selectHue = 0
        }
        if(selectHue > 360){
            selectHue = 360
        }
        //发起重绘
        invalidateSelf()
        return selectHue
    }

    fun parser(hue:Float){
        this.selectHue = (hue + 0.5F).toInt()
        invalidateSelf()
    }

    private fun getSelectY():Float{
        return (1 - 1.0F * selectHue / hueColor.size)  * bounds.height()
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun setColorFilter(filter: ColorFilter?) {
        paint.colorFilter = filter
    }
}