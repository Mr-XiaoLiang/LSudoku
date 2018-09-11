package liang.lollipop.lbaselib.util

import android.content.Context
import android.view.inputmethod.InputMethodManager


/**
 * Created by lollipop on 2018/1/26.
 * @author Lollipop
 * 键盘工具
 */
object KeyboardUtil {


    fun isShown(context: Context):Boolean{
        return getInputMethodManager(context).isActive//isOpen若返回true，则表示输入法打开
    }

    fun hide(context: Context){
        if(isShown(context)){
            getInputMethodManager(context).changeInputStatus()
        }
    }

    fun show(context: Context){
        if(!isShown(context)){
            getInputMethodManager(context).changeInputStatus()
        }
    }

    fun getInputMethodManager(context: Context): InputMethodManager{
        return context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    private fun InputMethodManager.changeInputStatus(){
        toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS)
    }

}