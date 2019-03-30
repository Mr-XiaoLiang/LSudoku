package liang.lollipop.lsudoku.util

import android.animation.Animator
import android.animation.ValueAnimator
import android.util.Log
import android.view.View

/**
 * @date: 2019/03/30 14:38
 * @author: lollipop
 * 标注提示面板的动画辅助类
 */
class SymbolPanelAnimationHelper(private val panelView: View, private val panelCard: View, private val panelBg: View):
        ValueAnimator.AnimatorUpdateListener,
        Animator.AnimatorListener{

    companion object {
        private const val DEF_SYMBOL_DURATION = 300L
        private const val OPEN = 1F
        private const val CLOSE = 0F
    }

    private val animator = ValueAnimator().apply {
        addUpdateListener(this@SymbolPanelAnimationHelper)
        addListener(this@SymbolPanelAnimationHelper)
        duration = DEF_SYMBOL_DURATION
    }

    private var startX = 0F
    private var startY = 0F
    private var endX = 0F
    private var endY = 0F
    private var widthWeight = 0F
    private var heightWeight = 0F

    var animationDuration = DEF_SYMBOL_DURATION

    private var animationProgress = 0F

    private var isOpen = false

    fun showSymbolPanel(view: View) {
        isOpen = true
        val locArray = IntArray(2)
        view.getLocationOnScreen(locArray)
        log("startLoc:[${locArray[0]},${locArray[1]}]")
        startX = locArray[0] * 1F - (panelCard.width - view.width) * 0.5F
        startY = locArray[1] * 1F - (panelCard.height - view.height) * 0.5F
        panelCard.getLocationOnScreen(locArray)
        log("endLoc:[${locArray[0]},${locArray[1]}]")
        endX = locArray[0] * 1F
        endY = locArray[1] * 1F
        widthWeight = view.width * 1F / panelCard.width
        heightWeight = view.height  * 1F / panelCard.height
        log("startX:$startX, startY:$startY, endX:$endX, endY:$endY, widthWeight:$widthWeight, heightWeight:$heightWeight")


        animator.cancel()
        animator.setFloatValues(animationProgress, OPEN)
        animator.duration = (animationDuration * (OPEN - animationProgress)).toLong()
        animator.start()
    }

    fun hideSymbolPanel() {
        isOpen = false
        animator.cancel()
        animator.setFloatValues(animationProgress, CLOSE)
        animator.duration = (animationDuration * (animationProgress - CLOSE)).toLong()
        animator.start()
    }

    override fun onAnimationUpdate(animation: ValueAnimator?) {
        if (animation != animator) {
            return
        }
        animationProgress = animation.animatedValue as Float
        panelCard.translationY = (startY - endY) * (1 - animationProgress)
        panelCard.translationX = (startX - endX) * (1 - animationProgress)
        panelCard.alpha = animationProgress
        panelCard.scaleX = widthWeight + (1 - widthWeight) * animationProgress
        panelCard.scaleY = heightWeight + (1 - heightWeight) * animationProgress

        panelBg.alpha = animationProgress
    }

    override fun onAnimationRepeat(animation: Animator?) {
    }

    override fun onAnimationEnd(animation: Animator?) {
        if (!isOpen) {
            panelView.visibility = View.INVISIBLE
            panelCard.translationY = 0F
            panelCard.translationX = 0F
            panelCard.scaleX = 1F
            panelCard.scaleY = 1F
        }
    }

    override fun onAnimationCancel(animation: Animator?) {}

    override fun onAnimationStart(animation: Animator?) {
        if (panelView.visibility != View.VISIBLE) {
            panelView.visibility = View.VISIBLE
        }
    }

    private fun log(value: String) {
        Log.d("Lollipop", "SymbolPanelAnimationHelper: $value")
    }

}