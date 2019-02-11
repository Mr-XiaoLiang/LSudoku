package liang.lollipop.lsudoku.util

import android.content.Context
import android.graphics.Typeface
import android.widget.TextView

/**
 * Created by lollipop on 2018/3/3.
 * @author Lollipop
 * 字体工具类
 */
class FontUtil(private val typeface: Typeface) {

    companion object {

        fun getLevelFont(context: Context): Typeface{
            return Typeface.createFromAsset(context.assets,"fonts/Tangerine-Bold.ttf")
        }

        fun getLogoFont(context: Context): Typeface{
            return Typeface.createFromAsset(context.assets,"fonts/Italianno-Regular.ttf")
        }

        fun getNumberFont(context: Context): Typeface{
            return Typeface.createFromAsset(context.assets,"fonts/number.otf")
        }

        fun withLevelFont(textView: TextView): FontUtil{
            val context = textView.context
            return FontUtil(getLevelFont(context)).andWith(textView)
        }

        fun withLevelFont(context: Context): FontUtil{
            return FontUtil(getLevelFont(context))
        }

        fun withLogoFont(textView: TextView): FontUtil{
            val context = textView.context
            return FontUtil(getLogoFont(context)).andWith(textView)
        }

        fun withLogoFont(context: Context): FontUtil{
            return FontUtil(getLogoFont(context))
        }

        fun withNumberFont(textView: TextView): FontUtil{
            val context = textView.context
            return FontUtil(getNumberFont(context)).andWith(textView)
        }

        fun withNumberFont(context: Context): FontUtil{
            return FontUtil(getNumberFont(context))
        }

    }

    fun andWith(textView: TextView): FontUtil{
        textView.typeface = typeface
        return this
    }

}

