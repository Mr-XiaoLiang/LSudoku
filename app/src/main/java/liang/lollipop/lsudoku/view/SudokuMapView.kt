package liang.lollipop.lsudoku.view

import android.content.Context
import android.graphics.Color
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import android.view.View
import liang.lollipop.lsudoku.drawable.GridMapDrawable
import liang.lollipop.lsudoku.drawable.SudokuMapDrawable
import liang.lollipop.lsudoku.skin.Skin
import liang.lollipop.lsudoku.util.FontUtil

/**
 * Created by lollipop on 2018/2/22.
 * @author Lollipop
 * 数独的地图View
 */
class SudokuMapView(context: Context, attrs: AttributeSet?,
                    @AttrRes defStyleAttr:Int, @StyleRes defStyleRes:Int)
    : SquareFrameLayout(context,attrs,defStyleAttr, defStyleRes),
        View.OnClickListener,
        View.OnLongClickListener {

    constructor(context: Context, attrs: AttributeSet?,
                @AttrRes defStyleAttr:Int):this(context,attrs,defStyleAttr,0)
    constructor(context: Context, attrs: AttributeSet?):this(context,attrs,0)
    constructor(context: Context):this(context,null)

    private val numViews = Array(ROW * COL) { AutoSizeTextView(context, attrs, defStyleAttr) }
    var onGridClickListener: OnGridClickListener? = null
    private var srcMap:Array<IntArray>? = null
    private var warningColor = Color.RED and 0xAAFFFFFF.toInt()
    private var srcColor = Color.BLACK
    private var editColor = Color.GRAY
    private var showColorHint = true
    private val associateColor:Int = (Color.RED and 0x80FFFFFF.toInt())
    private val selectedColor:Int = (Color.BLUE and 0xAAFFFFFF.toInt())

    private val backgroundDrawable = GridMapDrawable()

    companion object {

        //行数
        private const val ROW = 9
        //列数
        private const val COL = 9

    }

    init {
        val fontUtil = FontUtil.withNumberFont(context)
        for(view in numViews){
            addView(view)
            fontUtil.andWith(view)
            view.setOnClickListener(this)
            view.setOnLongClickListener(this)

            if(isInEditMode){
                view.text = "A"
                view.setTextColor(Color.BLACK)
            }
        }

        backgroundDrawable.associateColor = associateColor
        backgroundDrawable.selectedColor = selectedColor
        background = backgroundDrawable

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val childWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(measuredWidth / COL, View.MeasureSpec.EXACTLY)
        val childHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(measuredHeight / ROW, View.MeasureSpec.EXACTLY)
        for(view in numViews){
            view.measure(childWidthMeasureSpec,childHeightMeasureSpec)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val count = numViews.size
        var childLeft = 0F
        var childTop = 0F
        val gridWidth = width.toFloat() / COL
        val gridHeight = height.toFloat() / ROW
        for(index in 0 until count ){
            if(index % COL == 0){
                childLeft = 0F
                childTop = index / COL * gridHeight
            }
            getChildAt(index).layout(childLeft.toInt(), childTop.toInt(), (childLeft + gridWidth).toInt(), (childTop + gridHeight).toInt())

            childLeft += gridWidth
        }
    }

    fun resetFontSize(size:Float){
        for(view in numViews){
            view.fontSize = size
        }
    }

    fun setFontColor(color:Int){
        for(view in numViews){
            view.setTextColor(color)
        }
    }

    fun setFontColor(x:Int,y:Int,color:Int){
        getChild(x,y).setTextColor(color)
    }

    fun setText(x:Int,y:Int,value:CharSequence){
        getChild(x,y).text = value
    }

    fun setGridBackgroundColor(x:Int,y:Int,color:Int){
        getChild(x,y).setBackgroundColor(color)
    }

    private fun getChild(x:Int,y:Int): AutoSizeTextView {
        return numViews[y * COL + x]
    }

    override fun onClick(v: View?) {
        val index = numViews.indexOf(v)
        if(index < 0){
            return
        }

        val x = index % COL
        val y = index / COL

        onGridClickListener?.onGridClick(numViews[index],x,y)

    }

    override fun onLongClick(v: View?): Boolean {
        val index = numViews.indexOf(v)
        if(index < 0){
            return false
        }

        val x = index % COL
        val y = index / COL

        onGridClickListener?.onGridLongClick(numViews[index],x,y)
        return true
    }

    interface OnGridClickListener{
        fun onGridClick(view:View,x:Int,y:Int)
        fun onGridLongClick(view:View,x:Int,y:Int)
    }

    fun updateNumColor(srcMap:Array<IntArray>,map:Array<IntArray>, symbol: Array<IntArray>){

        this.srcMap = srcMap

        for(row in 0 until map.size){
            for(col in 0 until map[row].size){
                val view = getChild(row,col)
                view.text = if(map[row][col] > 0){ "${map[row][col]}" } else {""}
                view.setTextColor(if(srcMap[row][col] > 0){ srcColor } else { editColor })
                view.changeSymbolStatus(symbol[row][col])
            }
        }

    }

    fun warning(srcMap: Array<IntArray>, editMap:Array<IntArray>, warningMap:Array<IntArray>, symbol: Array<IntArray>){

        updateNumColor(srcMap,editMap, symbol)

        if(!showColorHint){
            return
        }
        for(row in 0 until warningMap.size){
            (0 until warningMap[row].size)
                    .filter { warningMap[row][it] > 0 }
                    .forEach { getChild(row, it).setTextColor(warningColor) }
        }
    }

    private var isShowWarning: Boolean
        set(value) {
            showColorHint = value
        }
        get() = showColorHint

    private var isShowAssociate: Boolean
        set(value) {
            backgroundDrawable.showAssociate = value
        }
        get() = backgroundDrawable.showAssociate

    fun selected(x:Int,y:Int){
        if(x >= 0 && y >= 0 && srcMap!=null && srcMap!![x][y] == 0){
            backgroundDrawable.selected(x,y)
        }else{
            backgroundDrawable.selected(-1,-1)
        }
    }

    fun onSkinChange(skin: Skin){
        srcColor = skin.srcColor
        warningColor = skin.warningColor
        editColor = skin.editColor
        backgroundDrawable.associateColor = skin.associateColor
        backgroundDrawable.selectedColor = skin.selectedColor
        backgroundDrawable.borderColor = skin.borderColor
        backgroundDrawable.gridBorderColor = skin.gridColor
        backgroundDrawable.bigGridBorderColor = skin.bigGridColor
        isShowWarning = skin.warningHint
        isShowAssociate = skin.associateHint
//        backgroundDrawable.invalidateSelf()
        symbolColor(skin.symbolColor)
        invalidate()
    }

    private fun symbolColor(color: Int) {
        for (view in numViews) {
            view.symbolColor(color)
        }
    }

    fun symbolSizeWeight(weight: Float) {
        for (view in numViews) {
            view.symbolSizeWeight(weight)
        }
    }

    fun onSymbolChange(value: Int, x: Int, y: Int) {
        numViews[y * 9 + x].changeSymbolStatus(value)
    }

    fun onSymbolChangea(values: Array<IntArray>) {
        for (x in 0 until values.size) {
            for (y in 0 until values[x].size) {
                onSymbolChange(values[x][y], x, y)
            }
        }
    }

}