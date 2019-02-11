package liang.lollipop.lsudoku.skin

import android.content.res.ColorStateList
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.CardView
import liang.lollipop.lsudoku.view.SudokuMapView

/**
 * Created by lollipop on 2018/3/9.
 * @author Lollipop
 */
class SkinUtil(private var skin:Skin) {

    fun setSkin(skin: Skin){
        this.skin = skin
    }

    fun withFAB(floatingActionButton: FloatingActionButton): SkinUtil{
        floatingActionButton.backgroundTintList = ColorStateList.valueOf(skin.colorAccent)
        return this
    }

    fun whitCard(cardView: CardView): SkinUtil{
        cardView.setCardBackgroundColor(skin.colorAccent)
        return this
    }

    fun withMapView(mapView: SudokuMapView,srcMap:Array<IntArray>,editMap:Array<IntArray>,warningMap:Array<IntArray>? = null): SkinUtil{
        mapView.onSkinChange(skin)
        if(warningMap != null){
            mapView.warning(srcMap, editMap, warningMap)
        }else{
            mapView.updateNumColor(srcMap,editMap)
        }
        return this
    }

}