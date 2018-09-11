package liang.lollipop.lsudoku.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import liang.lollipop.lbaselib.base.LSimpleAdapter
import liang.lollipop.lbaselib.util.LItemTouchHelper
import liang.lollipop.lsudoku.bean.SudokuBean
import liang.lollipop.lsudoku.holder.SudokuMapHolder
import liang.lollipop.lsudoku.skin.Skin

/**
 * Created by lollipop on 2018/3/1.
 * @author Lollipop
 * 数独列表的适配器
 */
class SudokuListAdapter(data:ArrayList<SudokuBean>,
                        private val skinCallback: SkinCallback,
                        private val layoutInflater: LayoutInflater,
                        private val helper: LItemTouchHelper):
        LSimpleAdapter<SudokuMapHolder,SudokuBean>(data) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SudokuMapHolder {
        return SudokuMapHolder.create(layoutInflater,parent).apply { setTouchHelper(helper) }
    }


    override fun onBindViewHolder(holder: SudokuMapHolder, position: Int) {
        holder.onBind(data[position],skinCallback.getHolderSkin())
    }

    interface SkinCallback{

        fun getHolderSkin(): Skin

    }

}