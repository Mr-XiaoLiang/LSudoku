package liang.lollipop.lsudoku.util

import android.os.Environment

/**
 * Created by lollipop on 2018/1/31.
 * @author Lollipop
 * 常量类
 */
object Constant {

    fun getLogPath(): String{
        return getESDir("log")
    }

    private fun getESDRoot():String{
        return Environment.getExternalStorageDirectory().absolutePath + "/LSudoku/"
    }

    private fun getESDir(name:String):String{
        return getESDRoot() +name
    }

}