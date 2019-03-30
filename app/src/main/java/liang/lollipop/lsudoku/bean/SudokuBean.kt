package liang.lollipop.lsudoku.bean

import liang.lollipop.lbaselib.base.BaseBean
import liang.lollipop.lsudoku.util.SudokuHelper
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by lollipop on 2018/3/1.
 * @author Lollipop
 */
class SudokuBean: BaseBean() {

    companion object {

        private const val ONE_SECOND = 1000L

        private const val ONE_MINUTE = ONE_SECOND * 60

        private const val ONE_HOUR = ONE_MINUTE * 60

        private val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd-HH:mm:ss", Locale.getDefault())

    }

    //数独的完整数据
    val sudokuMap = Array(9) { IntArray(9) }
    //题目本身的数据
    val srcMap = Array(9) {IntArray(9)}
    //正在编辑的数据
    val editMap = Array(9) {IntArray(9)}
    // 标记用的地图
    val symbolMap = Array(9) { IntArray(9) }

    private var mapValue = ""

    var id = ""
    var map:String
        get() = mapValue
        set(value){
            mapValue = value
            SudokuHelper.parse(map,sudokuMap,srcMap,editMap, symbolMap)
        }
    var level = 0
    var startTime = 0L
    var endTime = 0L
    var hintTime = 0L
    var timeLength = 0L

    fun getLevelName(): String{
        return "L$level"
    }

    fun getStartTimeName():String{
        return formatDate(startTime)
    }

    fun getEndTimeName():String{
        return formatDate(endTime)
    }

    fun getHintTimeName():String{
        return formatTime(hintTime)
    }

    fun getGameTimeName():String{
        return formatTime(timeLength)
    }

    private fun formatTime(time:Long): String{
        val hour = time / ONE_HOUR
        val minute = time % ONE_HOUR / ONE_MINUTE
        val second = time % ONE_MINUTE / ONE_SECOND
        val millisecond = time % ONE_SECOND
        return formatNum(hour)+":"+formatNum(minute)+":"+formatNum(second)+"."+formatNum(millisecond)
    }

    private fun formatDate(timeValue:Long): String{
        return simpleDateFormat.format(Date(timeValue))
    }

    private fun formatNum(num: Long): String{
        return if(num < 10){
            "0"+num
        }else{
            ""+num
        }
    }

}