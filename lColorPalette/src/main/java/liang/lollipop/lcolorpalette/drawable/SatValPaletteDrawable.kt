package liang.lollipop.lcolorpalette.drawable

import android.graphics.*
import android.graphics.drawable.Drawable

/**
 * Created by lollipop on 2018/1/22.
 * 饱和度，灰度调色板
 * @author Lollipop
 */
class SatValPaletteDrawable:Drawable() {

    private val paint = Paint()
    private val pointPaint = Paint()
    private var hue = 0F
    private var valShader:BitmapShader? = null
    private var selectRadius = 5F
    private var valBitmap:Bitmap? = null

    private var satValue = 0.5F
    private var valValue = 0.5F

    init {
        paint.isDither = true
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = Color.RED
        pointPaint.isAntiAlias = true
        pointPaint.isDither = true
        pointPaint.strokeWidth = 2F
        pointPaint.style = Paint.Style.STROKE
    }

    override fun draw(canvas: Canvas?) {

        if(canvas == null){
            return
        }

        canvas.drawRect(bounds,paint)

        drawPoint(canvas)

    }

    private fun drawPoint(canvas: Canvas){
        val loc = getPointLoc()

        pointPaint.color = Color.WHITE
        canvas.drawCircle(loc[0],loc[1],selectRadius,pointPaint)

        pointPaint.color = Color.BLACK
        val oval = RectF(loc[0]-selectRadius,loc[1]-selectRadius,
                loc[0]+selectRadius,loc[1]+selectRadius)
        canvas.drawArc(oval, 0F,90F,false,pointPaint)
        canvas.drawArc(oval, 180F,90F,false,pointPaint)
    }

    override fun onBoundsChange(bounds: Rect?) {
        super.onBoundsChange(bounds)
        if(bounds == null){
            return
        }

        createNewValShader()

        onHueChange(hue)

    }

    /**
     * 写法说明：
     * 因为设备开启了硬件加速以后，
     * ComposeShader无法合并两个相同类型的Shader（两个LinearGradient）
     * 因此，这里讲变化不大的明度Shader转换为了BitmapShader
     * 并且在View尺寸发生变化的时候，回收Bitmap并且重新创建渲染
     */
    private fun createNewValShader(){

        if(bounds.width() < 1 || bounds.height() <1){
            return
        }

        if(valBitmap!=null){
            valBitmap?.recycle()
            valBitmap = null
        }

        val valLinearGradient =  LinearGradient(bounds.left.toFloat(),bounds.top.toFloat(),
                bounds.left.toFloat(),bounds.bottom.toFloat(),0xFFFFFFFF.toInt(),0xFF000000.toInt(),
                Shader.TileMode.CLAMP)
        paint.shader = valLinearGradient
        valBitmap = Bitmap.createBitmap(bounds.width(),bounds.height(),Bitmap.Config.ARGB_8888)
        val canvas = Canvas(valBitmap)
        canvas.drawRect(bounds,paint)
        valShader = BitmapShader(valBitmap,Shader.TileMode.CLAMP,Shader.TileMode.CLAMP)
    }

    fun onHueChange(hue:Float){
        this.hue = hue
        val rgbColor = Color.HSVToColor(floatArrayOf(hue,1F,1F))
        val satShader = LinearGradient(bounds.left.toFloat(),bounds.top.toFloat(),
                bounds.right.toFloat(),bounds.top.toFloat(),0xFFFFFFFF.toInt(),rgbColor,
                Shader.TileMode.CLAMP)

        if(valShader == null){
            createNewValShader()
        }

        if(valShader != null){
            val composeShader = ComposeShader(valShader,satShader,PorterDuff.Mode.MULTIPLY)

            paint.shader = composeShader
        }

        invalidateSelf()
    }

    fun lastSelected():FloatArray{
        invalidateSelf()
        return floatArrayOf(hue,satValue,valValue)
    }

    fun selectTo(x:Float,y:Float):FloatArray{
        satValue = x / bounds.width()
        valValue = 1 - y / bounds.height()

        if(satValue < 0){
            satValue = 0F
        }

        if(satValue > 1){
            satValue = 1F
        }

        if(valValue < 0){
            valValue = 0F
        }

        if(valValue > 1){
            valValue = 1F
        }

        invalidateSelf()

        return floatArrayOf(hue,satValue,valValue)
    }

    private fun getPointLoc():FloatArray{

        val x = satValue * bounds.width()
        val y = (1 - valValue) * bounds.height()

        return floatArrayOf(x,y)
    }

    fun parser(satValue:Float,valValue:Float):FloatArray{
        this.satValue = satValue
        this.valValue = valValue
        return lastSelected()
    }

    fun setSelectRadius(radius:Float){
        this.selectRadius = radius
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