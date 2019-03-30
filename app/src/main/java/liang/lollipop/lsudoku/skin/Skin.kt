package liang.lollipop.lsudoku.skin

import liang.lollipop.lbaselib.base.BaseBean

/**
 * Created by lollipop on 2018/3/7.
 * @author Lollipop
 * 皮肤颜色的Bean
 */
open class Skin: BaseBean() {

    /** 主题色 **/
    var colorPrimary = 0
    /** 深色的主题色 **/
    var colorPrimaryDark = 0
    /** 浅色的主题色 **/
    var colorPrimaryLight = 0
    /** 撞色 **/
    var colorAccent = 0
    /** 分割线颜色 **/
    var colorDivider = 0
    /** 文本主体颜色，标题色 **/
    var textPrimary = 0
    /** 文本次要颜色，内容色 **/
    var textSecondary = 0
    /** 错误提示的颜色 **/
    var warningColor = 0
    /** 题目的颜色 **/
    var srcColor = 0
    /** 可编辑的颜色 **/
    var editColor = 0
    /** 关联项的颜色 **/
    var associateColor = 0
    /** 选择项的颜色 **/
    var selectedColor = 0
    /** 边框颜色 **/
    var borderColor = 0
    /** 格子颜色 **/
    var gridColor = 0
    /** 大格子颜色 **/
    var bigGridColor = 0
    /** 标志颜色 **/
    var symbolColor = 0
    /** 关联提示开关 **/
    var associateHint = true
    /** 错误提示开关 **/
    var warningHint = true

    override fun hashCode(): Int {
        //以一种特别的方式将几个数字组合起来，作为当前数据的hashCode
        return (colorPrimary + colorPrimaryDark +  colorPrimaryLight
                + colorAccent +  colorDivider + textPrimary
                +  textSecondary + warningColor + srcColor
                + editColor + associateColor + selectedColor
                + borderColor + gridColor + bigGridColor + symbolColor
                + associateHint.toInt() + warningHint.toInt())
    }

    override fun equals(other: Any?): Boolean {
        if(other == null){
            return false
        }
        return this.hashCode() == other.hashCode()
    }

    fun Boolean.toInt(): Int{
        return if(this){
            1
        }else{
            0
        }
    }

    fun copy(skin: Skin){
        this.colorAccent = skin.colorAccent
        this.colorPrimaryDark = skin.colorPrimaryDark
        this.colorPrimaryLight = skin.colorPrimaryLight
        this.colorDivider = skin.colorDivider
        this.textPrimary = skin.textPrimary
        this.textSecondary = skin.textSecondary
        this.warningColor = skin.warningColor
        this.srcColor = skin.srcColor
        this.editColor = skin.editColor
        this.associateColor = skin.associateColor
        this.selectedColor = skin.selectedColor
        this.borderColor = skin.borderColor
        this.gridColor = skin.gridColor
        this.bigGridColor = skin.bigGridColor
        this.symbolColor = skin.symbolColor
        this.associateHint = skin.associateHint
        this.warningHint = skin.warningHint
    }

    override fun toString(): String {
        return "colorPrimary:$colorPrimary,colorPrimaryDark:$colorPrimaryDark,colorPrimaryLight:$colorPrimaryLight,colorAccent:$colorAccent," +
                "colorDivider:$colorDivider,textPrimary:$textPrimary,textSecondary:$textSecondary,warningColor:$warningColor,srcColor:$srcColor" +
                ",editColor:$editColor,associateColor:$associateColor,selectedColor:$selectedColor," +
                "borderColor:$borderColor,gridColor:$gridColor,bigGridColor:$bigGridColor,symbolColor:$symbolColor,associateHint:$associateHint,warningHint:$warningHint"
    }

}