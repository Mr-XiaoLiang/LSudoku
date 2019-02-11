package liang.lollipop.lsudoku.util

import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by lollipop on 2018/2/23.
 * @author Lollipop
 * 数独的帮助类
 */
object SudokuHelper{

    private const val MIN_RESTART_TIME = 500L

    private val random = Random()
    var restartTime:Long = MIN_RESTART_TIME

    /**
     * 当前填入内容是否可用
     */
    private fun isCorret(row: Int, col: Int,map: Array<IntArray>): Boolean {
        return checkRow(row,map) and checkLine(col,map) and checkNine(row, col,map)
    }

    /**
     * 检查行是否符合要求
     *
     * @param row
     * 检查的行号
     * @return true代表符合要求
     */
    private fun checkRow(row: Int,map:Array<IntArray>): Boolean {
        for (j in 0..7) {
            if (map[row][j] == 0) {
                continue
            }
            for (k in j + 1..8) {
                if (map[row][j] == map[row][k]) {
                    return false
                }
            }
        }
        return true
    }

    /**
     * 检查列是否符合要求
     *
     * @param col
     * 检查的列号
     * @return true代表符合要求
     */
    private fun checkLine(col: Int,map:Array<IntArray>): Boolean {
        for (j in 0..7) {
            if (map[j][col] == 0) {
                continue
            }
            for (k in j + 1..8) {
                if (map[j][col] == map[k][col]) {
                    return false
                }
            }
        }
        return true
    }

    /**
     * 检查3X3区域是否符合要求
     *
     * @param row
     * 检查的行号
     * @param col
     * 检查的列号
     * @return true代表符合要求
     */
    private fun checkNine(row: Int, col: Int,map: Array<IntArray>): Boolean {
        // 获得左上角的坐标
        val j = row / 3 * 3
        val k = col / 3 * 3
        // 循环比较
        for (i in 0..7) {
            if (map[j + i / 3][k + i % 3] == 0) {
                continue
            }
            for (m in i + 1..8) {
                if (map[j + i / 3][k + i % 3] == map[j + m / 3][k + m % 3]) {
                    return false
                }
            }
        }
        return true
    }

    /**
     * 复制一个数独地图
     */
    fun copyMap(srcMap:Array<IntArray>,toMap:Array<IntArray>){
        for(row in 0 until toMap.size){
            for(col in 0 until toMap[row].size){
                toMap[row][col] = srcMap[row][col]
            }
        }
    }

    /**
     * 复制一个数组形式的数独地图
     */
    fun copyMap(srcMap:IntArray,toMap:IntArray){
        for(index in 0 until toMap.size){
            toMap[index] = srcMap[index]
        }
    }

    /**
     * 生成一个数独的地图
     */
    fun generate():Array<IntArray>{
        val sudokuMap = Array(9) { IntArray(9) }
        /** 生成随机数字的源数组，随机数字从该数组中产生  */
        val num = IntArray(9,{i -> i})

        var start = System.currentTimeMillis()
        //生成数字
        var row = 0
        while (row < 9){
            //尝试填充的数字次数
            var time = 0
            //填充数字
            var col = 0
            while(col < 9){
                //如果超过了重启时间，则认为本次任务陷入死循环，那么重新启动新的任务
                val end = System.currentTimeMillis()
                if(( end - start ) > restartTime ){
                    println("重启，start:$start,end:$end")
                    start = System.currentTimeMillis()
                    row = 0
                    break
                }

                sudokuMap[row][col] = generateNum(time,num)
                // 如果返回值为0，则代表卡住，退回处理
                // 退回处理的原则是：如果不是第一列，则先倒退到前一列，否则倒退到前一行的最后一列
                if(sudokuMap[row][col] == 0){
                    when {
                        col > 0 -> {
                            // 不是第一列，则倒退一列
                            col--
                        }
                        row > 0 -> {
                            // 不是第一行，则倒退到上一行的最后一列
                            row--
                            col = 8
                        }
                        else -> {
                            //如果是第一行，第一列，那么重新再来
                            time = 0
                        }
                    }
                    continue
                }
                // 填充成功
                if (isCorret( row , col , sudokuMap)) {
                    // 初始化time，为下一次填充做准备
                    time = 0
                } else {
                    // 次数增加1
                    time++
                    // 继续填充当前格
                    continue
                }

                col++
            }
            row++
        }

        return sudokuMap
    }

    /**
     * 产生1-9之间的随机数字 规则：生成的随机数字放置在数组8-time下标的位置，随着time的增加，已经尝试过的数字将不会在取到
     * 说明：即第一次次是从所有数字中随机，第二次时从前八个数字中随机，依次类推， 这样既保证随机，也不会再重复取已经不符合要求的数字，提高程序的效率
     * 这个规则是本算法的核心
     *
     * @param time 填充的次数，0代表第一次填充
     * @return
     */
    private fun generateNum(time: Int,num:IntArray): Int {
        // 第一次尝试时，初始化随机数字源数组
        if (time == 0) {
            for (i in 0..8) {
                num[i] = i + 1
            }
        }
        // 第10次填充，表明该位置已经卡住，则返回0，由主程序处理退回
        if (time == 9) {
            return 0
        }
        // 不是第一次填充
        // 生成随机数字，该数字是数组的下标，取数组num中该下标对应的数字为随机数字
        //      int ranNum = (int) (Math.random() * (9 - time));//j2se
        val ranNum = random.nextInt(9 - time)//j2me
        // 把数字放置在数组倒数第time个位置，
        val temp = num[8 - time]
        num[8 - time] = num[ranNum]
        num[ranNum] = temp
        // 返回数字
        return num[8 - time]
    }

    /**
     * 生成一个一维数组的数独图
     */
    fun generateArray():IntArray{
        val sudokuMap = generate()
        val sudokuArray = IntArray(81)
        for(row in 0 until sudokuMap.size){
            for(col in 0 until sudokuMap[row].size){
                sudokuArray[row*9+col] = sudokuMap[row][col]
            }
        }
        return sudokuArray
    }

    /**
     * 将完整的数独图改为镂空
     */
    fun emptyTo(map:Array<IntArray>,emptySize:Int){

        emptyWithDelete(map,emptySize)
    }

    /**
     * 以删除形式获得题目
     */
    fun emptyWithDelete(map:Array<IntArray>,emptySize:Int){

        do{
            var size = 0
            for (row in map){
                row.filter { it < 1 }
                        .forEach { size++ }
            }
            if(size >= emptySize){
                return
            }

            for(i in 0 until (emptySize - size)){

                val row = random.nextInt(9)
                val col = random.nextInt(9)
                map[row][col] = 0

            }

        }while (true)
    }

    /**
     * 以摘取的形式获得题目
     * 经过测试，效率远远比不上删除方式，因此弃用
     */
    @Deprecated("Inefficient")
    fun emptyWithGet(map:Array<IntArray>,emptySize:Int){

        var success = false
        val resultMap = Array(9) {IntArray(9)}

        do{
            var size = 0
            for (row in resultMap){
                row.filter { it < 1 }
                        .forEach { size++ }
            }

            for(i in 0 until (size - emptySize)){

                val row = random.nextInt(9)
                val col = random.nextInt(9)
                resultMap[row][col] = map[row][col]

            }


            size = 0
            for (row in resultMap){
                row.filter { it < 1 }
                        .forEach { size++ }
            }
            if(size <= emptySize){
                success = true
            }
        }while (!success)

        copyMap(resultMap,map)
    }

    /**
     * 检查是否是唯一解，如果不是唯一解，那么将返回false
     */
    fun checkSole(map: Array<IntArray>, inputArray: IntArray): Boolean {

        val testMap = Array(9){IntArray(9)}
        copyMap(map, testMap)


        return true
    }

    /**
     * 检查并且得到出错位置的地图
     * 返回值为是否正确
     */
    fun check(srcMap: Array<IntArray>, outMap: Array<IntArray>): Boolean{

        var result = true

        //完全置空
        for(row in 0 until outMap.size){
            for(col in 0 until outMap[row].size){
                outMap[row][col] = 0
            }
        }

        //开始检查
        for (index in 0..8){

            for (j in 0..7) {
                for (k in (j + 1)..8) {

                    //检查列
                    if (srcMap[index][j] != 0 && srcMap[index][j] == srcMap[index][k]) {
                        outMap[index][j] = 1
                        outMap[index][k] = 1
                        result = false
                    }

                    //检查行
                    if (srcMap[j][index] != 0 && srcMap[j][index] == srcMap[k][index]) {
                        outMap[j][index] = 1
                        outMap[k][index] = 1
                        result = false
                    }

                }

            }

            //检查九宫格
            // 获得左上角的坐标
            val j = index / 3 * 3
            val k = index % 3 * 3
            // 循环比较
            for (i in 0..7) {
                if (srcMap[j + i / 3][k + i % 3] == 0) {
                    continue
                }
                for (m in i + 1..8) {
                    if (srcMap[j + i / 3][k + i % 3] == srcMap[j + m / 3][k + m % 3]) {
                        outMap[j + i / 3][k + i % 3] = 1
                        outMap[j + m / 3][k + m % 3] = 1
                        result = false
                    }
                }
            }

        }

        return result

    }

    /**
     * 单纯的检查是否出错
     * 返回值为是否正确
     */
    fun check(srcMap: Array<IntArray>): Boolean{

        //开始检查
        for (index in 0..8){

            for (j in 0..7) {
                for (k in (j + 1)..8) {
                    //检查列
                    if (srcMap[index][j] != 0 && srcMap[index][j] == srcMap[index][k]) {
                        return false
                    }
                    //检查行
                    if (srcMap[j][index] != 0 && srcMap[j][index] == srcMap[k][index]) {
                        return false
                    }
                }
            }

            //检查九宫格
            // 获得左上角的坐标
            val j = index / 3 * 3
            val k = index % 3 * 3
            // 循环比较
            for (i in 0..7) {
                if (srcMap[j + i / 3][k + i % 3] == 0) {
                    continue
                }
                for (m in i + 1..8) {
                    if (srcMap[j + i / 3][k + i % 3] == srcMap[j + m / 3][k + m % 3]) {
                        return false
                    }
                }
            }

        }

        return true

    }

    /**
     * 是否填写完整的检查方法
     */
    fun isFull(map: Array<IntArray>):Boolean{
        for (row in map){
            row.filter { it < 1 }
                    .forEach { return false }
        }
        return true
    }

    /**
     * 提示的方法，用于获取指定位置的可用数字
     */
    fun hint(map: Array<IntArray>,row: Int,col: Int):IntArray{

        val result = IntArray(9,{i -> i+1})

        // 获得左上角的坐标
        val j = row / 3 * 3
        val k = col / 3 * 3

        for(index in 0..8){
            if(map[index][col] > 0){
                result[map[index][col]-1] = 0
            }
            if(map[row][index] > 0){
                result[map[row][index]-1] = 0
            }

            if (map[j + index / 3][k + index % 3] > 0) {
                result[map[j + index / 3][k + index % 3]-1] = 0
            }

        }

        return result

    }

    fun serialization(sudokuMap:Array<IntArray>, srcMap: Array<IntArray>, editMap: Array<IntArray>): String{
        return merge(mapToString(sudokuMap),mapToString(srcMap),mapToString(editMap))
    }

    fun parse(string: String,sudokuMap:Array<IntArray>, srcMap: Array<IntArray>, editMap: Array<IntArray>){
        val strArray = split(string)
        stringToMap(strArray[0],sudokuMap)
        stringToMap(strArray[1],srcMap)
        stringToMap(strArray[2],editMap)
    }

    fun clearEdit(map:String):String{

        val strArray = split(map)
        val resultList = Array(strArray.size,{ i -> strArray[i] })
        resultList[2] = strArray[1]
        return merge(*resultList)

    }

    private fun split(map:String): List<String>{
        return map.split("|")
    }

    private fun merge(vararg maps:String):String{
        if(maps.isEmpty()){
           return ""
        }
        val stringBuilder = StringBuilder(maps[0])
        for(index in 1 until maps.size){
            stringBuilder.append("|")
            stringBuilder.append(maps[index])
        }
        return stringBuilder.toString()
    }

    private fun stringToMap(str:String):Array<IntArray>{
        val map = Array(9) { IntArray(9) }
        stringToMap(str,map)
        return map
    }

    private fun stringToMap(str:String,map:Array<IntArray>){
        val charArray = str.toCharArray()
        if(charArray.size<81){
            throw RuntimeException("data error")
        }
        for (index in 0 until charArray.size){
            map[index/9][index%9] = Integer.parseInt(charArray[index].toString())
        }
    }

    private fun mapToString(array:Array<IntArray>):String{
        val stringBuilder = StringBuilder()
        for(row in array){
            for (col in row){
                stringBuilder.append(col)
            }
        }
        return stringBuilder.toString()
    }

}