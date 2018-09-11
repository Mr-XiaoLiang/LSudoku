package liang.lollipop.lbaselib.util

import android.support.design.widget.AppBarLayout

/**
 * Created by lollipop on 2018/1/2.
 * @author Lollipop
 */
class AppBarStateChangeListener(private val listener: OnAppBarStateChangeListener): AppBarLayout.OnOffsetChangedListener {


    companion object {
        /**展开*/
        val EXPANDED = 1
        /**收起*/
        val COLLAPSED = 2
        /**闲置*/
        val IDLE = 0
    }

    private var state = IDLE

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {

        when {
            verticalOffset == 0 -> {
                if (state != EXPANDED) {
                    onStateChanged(appBarLayout!!, EXPANDED)
                }
                state = EXPANDED
            }
            Math.abs(verticalOffset) >= appBarLayout!!.totalScrollRange -> {
                if (state != COLLAPSED) {
                    onStateChanged(appBarLayout, COLLAPSED)
                }
                state = COLLAPSED
            }
            else -> {
                if (state != IDLE) {
                    onStateChanged(appBarLayout, IDLE)
                }
                state = IDLE
            }
        }
        
    }

    private fun onStateChanged(appBarLayout: AppBarLayout, state: Int){
        listener.onAppBarStateChangeed(appBarLayout,state)
    }

    interface OnAppBarStateChangeListener{
        fun onAppBarStateChangeed(appBarLayout: AppBarLayout, state: Int)
    }

}