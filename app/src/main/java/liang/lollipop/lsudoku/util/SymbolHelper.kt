package liang.lollipop.lsudoku.util

import android.graphics.Color
import android.view.View
import android.widget.TextView

/**
 * @date: 2019/03/30 15:41
 * @author: lollipop
 * 标识面板辅助类
 */
class SymbolHelper(private val numBtns: Array<TextView>, private val onChange: (Array<Boolean>) -> Unit): View.OnClickListener {

    var selectedColor = Color.BLACK
    var unselectedColor = Color.GRAY

    private val statusArray = Array(numBtns.size) { false }

    init {
        numBtns.forEach {
            it.setOnClickListener(this)
        }
    }

    fun update(status: Array<Boolean>) {
        val max = Math.min(numBtns.size, status.size)
        for (index in 0 until max) {
            changeStyle(numBtns[index], status[index])
            statusArray[index] = status[index]
        }
    }

    private fun changeStyle(view: TextView, status: Boolean) {
        view.setTextColor(if (status) { selectedColor } else { unselectedColor })
    }

    override fun onClick(v: View?) {
        val index = numBtns.indexOf(v)
        if (index < 0) {
            return
        }
        statusArray[index] = !statusArray[index]
        changeStyle(numBtns[index], statusArray[index])
        onChange(statusArray)
    }

}