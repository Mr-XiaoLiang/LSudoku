package liang.lollipop.lsudoku.util

import android.graphics.Point
import android.util.Log
import liang.lollipop.lsudoku.skin.SkinUtil
import liang.lollipop.lsudoku.view.SudokuMapView

/**
 * Created by lollipop on 2018/2/23.
 * @author Lollipop
 * 数独的相关逻辑处理控制器
 */
class SudokuController(
        private val mapView: SudokuMapView,
        private val callback: Callback) {

    //数独的完整数据
    private val sudokuMap = Array(9) { IntArray(9) }
    //题目本身的数据
    private val srcMap = Array(9) { IntArray(9) }
    //正在编辑的数据
    private val editMap = Array(9) { IntArray(9) }
    //提醒用的集合，用于标记并高亮错误输入
    private val warningMap = Array(9) { IntArray(9) { 0 } }
    //选中的位置
    private val selectedLoc = Point(-1, -1)
    // 标记用的地图
    private val symbolMap = Array(9) { IntArray(9) }

    object Symbol {
        const val num1 = 0x1
        const val num2 = 0x1 shl 1
        const val num3 = 0x1 shl 2
        const val num4 = 0x1 shl 3
        const val num5 = 0x1 shl 4
        const val num6 = 0x1 shl 5
        const val num7 = 0x1 shl 6
        const val num8 = 0x1 shl 7
        const val num9 = 0x1 shl 8
    }

    fun symbol(x: Int, y: Int): Array<Boolean> {
        val key = symbolMap[x][y]
        return arrayOf(
                key.and(Symbol.num1) > 0,
                key.and(Symbol.num2) > 0,
                key.and(Symbol.num3) > 0,
                key.and(Symbol.num4) > 0,
                key.and(Symbol.num5) > 0,
                key.and(Symbol.num6) > 0,
                key.and(Symbol.num7) > 0,
                key.and(Symbol.num8) > 0,
                key.and(Symbol.num9) > 0
        )
    }

    /**
     * 生成，生成一个地图题目，传入参数为空白格数量
     * 方法将会自动刷新地图
     */
    fun generate(emptySize: Int) {
        callback.onLoad()
        TaskUtils.addUITask(object : TaskUtils.UICallback<Array<IntArray>, Int> {
            override fun onSuccess(result: Array<IntArray>) {
                mapView.updateNumColor(srcMap, editMap, symbolMap)
                callback.onLoadEnd()
            }

            override fun onError(e: Exception, code: Int, msg: String) {
                callback.onError(e, code, msg)
            }

            override fun onBackground(args: Int?): Array<IntArray> {
                //生成地图
                val map = SudokuHelper.generate()
                //保存地图
                SudokuHelper.copyMap(map, sudokuMap)
                //复制一份到原始地图中
                SudokuHelper.copyMap(map, srcMap)
                //对原始地图置空，作为题目存根
                SudokuHelper.emptyTo(srcMap, args!!)
                //将题目备份，作为编辑地图
                SudokuHelper.copyMap(srcMap, editMap)
                // 清空标注
                SudokuHelper.cleanMap(symbolMap)
                return map
            }
        }, emptySize)
    }

    /**
     * 检查，检查地图整体错误内容，并且反馈成功或错误结果到回调函数
     */
    private fun check() {
        callback.onLoad()
        TaskUtils.addUITask(object : TaskUtils.UICallback<Boolean, Any> {
            override fun onSuccess(result: Boolean) {
                mapView.warning(srcMap, editMap, warningMap, symbolMap)
                callback.onLoadEnd()
                //如果没有错误，检查是否填写完整，如果完整，认为完成
                if (result) {
                    if (SudokuHelper.isFull(editMap)) {
                        callback.onSuccess(srcMap, editMap)
                    }
                    callback.onWarning(false)
                } else {
                    callback.onWarning(true)
                }
            }

            override fun onError(e: Exception, code: Int, msg: String) {
                callback.onError(e, code, msg)
            }

            override fun onBackground(args: Any?): Boolean {
                return SudokuHelper.check(editMap, warningMap)
            }
        }, null)
    }

    /**
     * 放置，放置一个数字到指定单元格，同时将自动触发检查机制
     */
    fun putNum(num: Int) {
        if (selectedLoc.x < 0 || selectedLoc.y < 0 || !canEdit(selectedLoc.x, selectedLoc.y)) {
            return
        }
        editMap[selectedLoc.x][selectedLoc.y] = num
        check()
    }

    fun changeSymbol(status: Array<Boolean>) {
        var i = 0
        for (index in 0 until status.size) {
            if (status[index]) {
                i = i.or(0x1 shl index)
            }
        }
        symbolMap[selectedLoc.x][selectedLoc.y] = i
        mapView.onSymbolChange(i, selectedLoc.x, selectedLoc.y)
    }

    /**
     * 提示，得到指定单元格的可填入提示的数组
     */
    fun hint(x: Int, y: Int): IntArray {
        return SudokuHelper.hint(editMap, x, y)
    }

    /**
     * 可修改，检查当前单元格是否可以修改
     */
    fun canEdit(x: Int, y: Int): Boolean {
        if (x < 0 || y < 0 || x >= 9 || y >= 9) {
            return false
        }
        return srcMap[x][y] < 1
    }

    /**
     * 选中一个位置
     */
    fun selected(x: Int, y: Int) {
        if (canEdit(x, y)) {
            mapView.selected(x, y)
            selectedLoc.set(x, y)
        } else {
            mapView.selected(-1, -1)
            selectedLoc.set(-1, -1)
        }
    }

    /**
     * 重新开始，但是并不更新题目
     */
    fun restart() {
        SudokuHelper.copyMap(srcMap, editMap)
        mapView.updateNumColor(srcMap, editMap, symbolMap)
        selected(-1, -1)
    }

    /**
     * 预览
     */
    fun preview(enable: Boolean) {
        if (enable) {
            mapView.updateNumColor(srcMap, sudokuMap, symbolMap)
        } else {
            check()
        }
    }

    override fun toString(): String {
        return SudokuHelper.serialization(sudokuMap, srcMap, editMap, symbolMap)
    }

    fun parse(string: String) {
        SudokuHelper.parse(string, sudokuMap, srcMap, editMap, symbolMap)
        mapView.updateNumColor(srcMap, editMap, symbolMap)
    }

    fun getLevel(): Int {
        var size = 0
        for (row in srcMap) {
            row.filter { it == 0 }.forEach { _ -> size++ }
        }
        return size
    }

    fun onSkinUpdate(skinUtil: SkinUtil) {
        skinUtil.withMapView(mapView, srcMap, editMap, warningMap, symbolMap)
    }

    /**
     * 回调函数，用于回调各个状态
     * 包括：
     *  加载中
     *  加载完成
     *  出现错误
     *  完成
     *  填入警告
     */
    interface Callback {
        //正在加载处理
        fun onLoad()

        //加载及处理完成
        fun onLoadEnd()

        //出现错误
        fun onError(e: Exception, code: Int, msg: String)

        //游戏结束
        fun onSuccess(srcMap: Array<IntArray>, editMap: Array<IntArray>)

        //有错误
        fun onWarning(warning: Boolean)
    }

    private fun log(value: String) {
        Log.d("Lollipop", "SudokuController: $value")
    }
}