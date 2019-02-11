package liang.lollipop.lsudoku.view

import android.content.Context
import android.graphics.Color
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.support.v7.graphics.drawable.DrawerArrowDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView

/**
 * Created by lollipop on 2018/2/27.
 * @author Lollipop
 * 方形的图片按钮
 */
class LImageButton(context: Context, attrs: AttributeSet?,
                   @AttrRes defStyleAttr:Int, @StyleRes defStyleRes:Int)
    : FrameLayout(context,attrs,defStyleAttr, defStyleRes) {

    constructor(context: Context, attrs: AttributeSet?,
                @AttrRes defStyleAttr:Int):this(context,attrs,defStyleAttr,0)
    constructor(context: Context, attrs: AttributeSet?):this(context,attrs,0)
    constructor(context: Context):this(context,null)

    private val imageView:ImageView = ImageView(context,attrs, defStyleAttr, defStyleRes)

    init {
        addView(imageView)
        val backDrawable = DrawerArrowDrawable(context)
        backDrawable.color = Color.BLACK
        backDrawable.progress = 1F
        imageView.setImageDrawable(backDrawable)
        val size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,24F,context.resources.displayMetrics).toInt()
        imageView.minimumHeight = size
        imageView.minimumWidth = size
        imageView.maxHeight = size
        imageView.maxWidth = size
        (imageView.layoutParams as FrameLayout.LayoutParams).gravity = Gravity.CENTER
    }



}