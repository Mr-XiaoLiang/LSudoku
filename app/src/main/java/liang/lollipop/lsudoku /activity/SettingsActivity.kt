package liang.lollipop.lsudoku.activity

import android.graphics.Point
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.toolbar.*
import liang.lollipop.lsudoku.R
import liang.lollipop.lsudoku.fragment.SettingsFragment
import liang.lollipop.lsudoku.skin.LSkinActivity
import liang.lollipop.lsudoku.skin.Skin
import liang.lollipop.lsudoku.util.SudokuHelper
import java.util.*

/**
 * 设置相关的Activity
 * @author Lollipop
 */
class SettingsActivity : LSkinActivity(),SettingsFragment.OnColorChangeCallback {

    private val srcDemoMap: Array<IntArray> = SudokuHelper.generate()

    private val editDemoMap = Array(9) {IntArray(9)}

    private val warningDemoMap = Array(9) {IntArray(9)}

    private val selectDemoPoint = Point()

    init {

        SudokuHelper.copyMap(srcDemoMap,editDemoMap)
        SudokuHelper.emptyTo(srcDemoMap,40)
        val random = Random()
        for(index in 0..5){
            editDemoMap[random.nextInt(9)][random.nextInt(9)] = random.nextInt(9)
        }
        SudokuHelper.check(editDemoMap,warningDemoMap)

        var isBreak = false
        for(row in 0 until srcDemoMap.size){
            if(isBreak){
                break
            }
            for(col in 0 until srcDemoMap[row].size){
                if(isBreak){
                    break
                }
                if(srcDemoMap[row][col] == 0){
                    selectDemoPoint.set(row,col)
                    isBreak = true
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setToolbar(toolbar)
        initView()
    }

    private fun initView(){
    }


    override fun onSkinUpdate(skin: Skin) {

        skinUtil.withFAB(floatingActionButton)
                .withMapView(mapView,srcDemoMap,editDemoMap,warningDemoMap)
        mapView.selected(selectDemoPoint.x,selectDemoPoint.y)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        requestUpdateSkin()
    }

    override fun onColorChange() {
        requestUpdateSkin()
    }

}
