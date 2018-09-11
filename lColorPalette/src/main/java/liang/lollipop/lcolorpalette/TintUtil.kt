package liang.lollipop.lcolorpalette

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v4.graphics.drawable.DrawableCompat
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by lollipop on 2018/2/2.
 * @author Lollipop
 * Tint用的工具类
 */
object TintUtil {

    fun tintText(view:TextView,vararg beans: TintBean){
        TintTextBuilder.with(view).addAll(*beans).tint()
    }

    fun tintWith(view: TextView): TintTextBuilder {
        return TintTextBuilder.with(view)
    }

    class TintBean(private val str:CharSequence,private val color:Int){

        fun value(): CharSequence{
            return str
        }

        fun color(): Int{
            return color
        }

        fun length():Int{
            return str.length
        }

    }

    fun tintDrawable(drawable: Drawable): TintDrawableBuilder {
        return TintDrawableBuilder(drawable)
    }

    fun tintDrawable(context: Context,resId:Int): TintDrawableBuilder {
        return TintDrawableBuilder.whitResId(context, resId)
    }

    fun tintDrawable(context: Context,bitmap: Bitmap): TintDrawableBuilder {
        return TintDrawableBuilder.whitBitmap(context, bitmap)
    }

    fun textFormat(): TintTextFormat{
        return TintTextFormat()
    }

    class TintTextFormat{

        private val tintBranArray = ArrayList<TintBean>()

        fun add(bean: TintBean): TintTextFormat {
            tintBranArray.add(bean)
            return this
        }

        fun add(value:CharSequence,color: Int): TintTextFormat {
            return add(TintBean(value,color))
        }

        fun add(value:String,color: Int): TintTextFormat {
            return add(TintBean(value,color))
        }

        fun add(context: Context,valueId:Int,color: Int): TintTextFormat {
            return add(TintBean(context.getString(valueId),color))
        }

        fun addAll(vararg beans: TintBean): TintTextFormat {
            tintBranArray.addAll(beans)
            return this
        }

        fun clear(): TintTextFormat{
            tintBranArray.clear()
            return this
        }

        fun simple(context: Context,valueId:Int,color: Int): CharSequence{
            clear()
            add(context,valueId,color)
            return tint()
        }

        fun simple(value:String,color: Int): CharSequence{
            clear()
            add(value,color)
            return tint()
        }

        fun tint(): CharSequence{
            if(tintBranArray.isEmpty()){
                return ""
            }
            val strBuilder = StringBuilder()
            for(str in tintBranArray){
                strBuilder.append(str.value())
            }
            val spannable = SpannableStringBuilder(strBuilder.toString())
            var index = 0
            for(color in tintBranArray){
                if(color.length() < 1){
                    continue
                }
                spannable.setSpan(ForegroundColorSpan(color.color()),index,index+color.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                index += color.length()
            }

            return spannable
        }

    }

    class TintTextBuilder private constructor(private val view: TextView){

        private val tintTextFormat = TintTextFormat()

        companion object {
            fun with(view: TextView): TintTextBuilder {
                return TintTextBuilder(view)
            }
        }

        fun add(bean: TintBean): TintTextBuilder {
            tintTextFormat.add(bean)
            return this
        }

        fun add(value:CharSequence,color: Int): TintTextBuilder {
            return add(TintBean(value,color))
        }

        fun add(value:String,color: Int): TintTextBuilder {
            return add(TintBean(value,color))
        }

        fun addAll(vararg beans: TintBean): TintTextBuilder {
            tintTextFormat.addAll(*beans)
            return this
        }

        fun clear(): TintTextBuilder{
            tintTextFormat.clear()
            return this
        }

        fun tint(){
            view.text = tintTextFormat.tint()
        }

    }

    /**
     * 对资源进行渲染的工具类
     */
    class TintDrawableBuilder(private val drawable: Drawable){

        private var colors: ColorStateList = ColorStateList.valueOf(Color.BLACK)

        companion object {

            fun whitResId(context: Context,resId:Int): TintDrawableBuilder {
                val wrappedDrawable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    context.resources.getDrawable(resId, context.theme)
                } else {
                    context.resources.getDrawable(resId)
                }
                return TintDrawableBuilder(wrappedDrawable)
            }

            fun whitBitmap(context: Context,bitmap: Bitmap): TintDrawableBuilder {
                val wrappedDrawable = BitmapDrawable(context.resources, bitmap)
                return TintDrawableBuilder(wrappedDrawable)
            }

        }

        fun mutate(): TintDrawableBuilder {
            drawable.mutate()
            return this
        }

        fun setColor(color: Int): TintDrawableBuilder {
            colors = ColorStateList.valueOf(color)
            return this
        }

        fun setColor(color: ColorStateList): TintDrawableBuilder {
            colors = color
            return this
        }

        fun tint(): Drawable{
            DrawableCompat.setTintList(drawable, colors)
            return drawable
        }

        fun withBackground(view: View): TintDrawableBuilder{
            view.background = tint()
            return this
        }

        fun withImageSrc(view: ImageView): TintDrawableBuilder{
            view.setImageDrawable(drawable)
            return this
        }

    }

}