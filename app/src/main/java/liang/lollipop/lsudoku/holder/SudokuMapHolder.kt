package liang.lollipop.lsudoku.holder

import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import liang.lollipop.lbaselib.base.BaseHolder
import liang.lollipop.lsudoku.R
import liang.lollipop.lsudoku.bean.SudokuBean
import liang.lollipop.lsudoku.skin.Skin
import liang.lollipop.lsudoku.util.FontUtil
import liang.lollipop.lsudoku.view.SudokuMapView

/**
 * Created by lollipop on 2018/3/1.
 * @author Lollipop
 * 首页数独记录的Holder
 */
class SudokuMapHolder private constructor(itemView: View): BaseHolder<SudokuBean>(itemView) {

    companion object {

        private const val LAYOUT_ID = R.layout.item_sudoku_map

        fun create(layoutInflater: LayoutInflater,parent:ViewGroup?): SudokuMapHolder{
            return SudokuMapHolder(layoutInflater.inflate(LAYOUT_ID,parent,false))
        }

    }

    val mapView:SudokuMapView = itemView.findViewById(R.id.mapView)
    val levelView:TextView = itemView.findViewById(R.id.levelView)
    private val timeView:TextView = itemView.findViewById(R.id.timeView)
    val lengthView:TextView = itemView.findViewById(R.id.lengthView)

    val cardView:CardView = itemView.findViewById(R.id.cardView)

    init {

        FontUtil.withLevelFont(levelView)
        itemView.setOnClickListener(this)
        mapView.isEnabled = false
        levelView.setOnClickListener(this)

    }

    override fun onBind(bean: SudokuBean) {

        mapView.updateNumColor(bean.srcMap,bean.editMap, bean.symbolMap)
        levelView.text = bean.getLevelName()
        timeView.text = bean.getEndTimeName()
        lengthView.text = bean.getGameTimeName()

    }

    fun onBind(bean: SudokuBean,skin: Skin){

        mapView.onSkinChange(skin)
        levelView.setTextColor(skin.colorAccent)
        timeView.setTextColor(skin.textSecondary)
        lengthView.setTextColor(skin.textPrimary)

        onBind(bean)

    }

}