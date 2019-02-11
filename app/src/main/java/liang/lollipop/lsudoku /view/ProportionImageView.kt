package liang.lollipop.lsudoku.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import liang.lollipop.lsudoku.R

/**
 * Created by lollipop on 2018/1/16.
 * @author Lollipop
 * 按比例计算的ImageView
 */
class ProportionImageView(context: Context, attrs: AttributeSet?, defStyleAttr:Int, defStyleRes:Int)
    : ImageView(context,attrs, defStyleAttr,defStyleRes) {

    private var widthWeight = -1
    private var heightWeight = -1

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr:Int):this(context,attrs,defStyleAttr,0)

    constructor(context: Context, attrs: AttributeSet?):this(context,attrs,0)

    constructor(context: Context):this(context,null)

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.ProportionImageView)

        widthWeight = array.getInt(R.styleable.ProportionImageView_widthWeight, -1)
        heightWeight = array.getInt(R.styleable.ProportionImageView_heightWeight,-1)

        array.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var newWidthMeasureSpec = widthMeasureSpec
        var newHeightMeasureSpec = heightMeasureSpec
        if(widthWeight > 0 && heightWeight > 0){
            val widthMode = MeasureSpec.getMode(widthMeasureSpec)
            val heightMode = MeasureSpec.getMode(heightMeasureSpec)
            if(widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY){
                //如果已经固定了，那么不再修改
            }else if(widthMode == MeasureSpec.EXACTLY){
                //如果只有宽度固定，那么调整高度
                val widthSize = MeasureSpec.getSize(widthMeasureSpec)
                val heightSize = (1.0*widthSize/widthWeight*heightWeight).toInt()
                newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize,MeasureSpec.EXACTLY)
            }else if(heightMode == MeasureSpec.EXACTLY){
                //如果只有高度固定，那么调整宽度
                val heightSize = MeasureSpec.getSize(heightMeasureSpec)
                val widthSize = (1.0*heightSize/heightWeight*widthWeight).toInt()
                newWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize,MeasureSpec.EXACTLY)
            }
            setMeasuredDimension(newWidthMeasureSpec,newHeightMeasureSpec)
            return
        }
        super.onMeasure(newWidthMeasureSpec, newHeightMeasureSpec)
    }

}