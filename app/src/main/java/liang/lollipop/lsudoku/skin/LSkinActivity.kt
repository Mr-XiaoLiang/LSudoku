package liang.lollipop.lsudoku.skin

import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import liang.lollipop.lbaselib.base.BaseActivity
import liang.lollipop.lsudoku.LApplication
import liang.lollipop.lsudoku.R
import liang.lollipop.lsudoku.dialog.NightModeLevelDialog
import liang.lollipop.lsudoku.util.LSettings
import liang.lollipop.lsudoku.view.NightModeView

/**
 * Created by lollipop on 2018/3/7.
 * @author Lollipop
 * 我的主题Activity
 */
abstract class LSkinActivity: BaseActivity(),NightModeLevelDialog.OnLevelChangeCallback {

    private var lastSkinCode = 0

    private val skinUpdateCallbackList = ArrayList<SkinUpdateCallback>()

    private var toolbar:Toolbar? = null

    protected var isSkinToolbar = true
    protected var isSkinSystemUI = true

    private var nightView: NightModeView? = null

    protected var skin:Skin = LApplication.appSkin
    private set

    protected val skinUtil = SkinUtil(skin)

    /**
     * 更新皮肤
     */
    fun updateSkin(skin: Skin){
        this.skin = skin
        skinUtil.setSkin(skin)
        if(isSkinToolbar && toolbar != null){
            val toolbarParent = toolbar!!.parent
            if(toolbarParent is AppBarLayout){
                toolbarParent.setBackgroundColor(skin.colorPrimary)
            }else{
                toolbar!!.setBackgroundColor(skin.colorPrimary)
            }
        }
        if(isSkinSystemUI){
            statusBarColor = skin.colorPrimaryDark
            navigationBarColor = skin.colorPrimaryDark
        }
        onSkinUpdate(skin)
        skinUpdateCallbackList.map { it.onSkinUpdate(skin) }
    }

    override fun setToolbar(toolbar: Toolbar) {
        super.setToolbar(toolbar)
        this.toolbar = toolbar
    }

    protected var statusBarColor:Int
        set(value) {
            window.statusBarColor = value
        }
        get() {
            return window.statusBarColor
        }

    protected var navigationBarColor:Int
        set(value) {
            window.navigationBarColor = value
        }
        get() {
            return window.navigationBarColor
        }

    abstract fun onSkinUpdate(skin: Skin)

    override fun onStart() {
        super.onStart()
        if(lastSkinCode != LApplication.appSkin.hashCode()){
            updateSkin(LApplication.appSkin)
            lastSkinCode = LApplication.appSkin.hashCode()
        }
        val isNight = isNightMode()
        if(isNight){
            openNightMode()
        }else{
            closeNightMode()
        }
        onNightModeChange(isNight)
    }

    protected fun requestUpdateSkin(){
        (application as LApplication).onSkinChange()
    }

    interface SkinUpdateCallback{
        fun onSkinUpdate(skin: Skin)
    }

    fun addSkinUpdateCallback(skinUpdateCallback: SkinUpdateCallback){
        skinUpdateCallbackList.add(skinUpdateCallback)
    }

    fun removeSkinUpdateCallback(skinUpdateCallback: SkinUpdateCallback){
        skinUpdateCallbackList.remove(skinUpdateCallback)
    }

    protected fun changeNightMode(){
        val openType = isNightMode()
        if(openType){
            //开启状态，那么关闭它
            closeNightMode()
        }else{
            //关闭状态，那么开启它
            openNightMode()
            NightModeLevelDialog().show(supportFragmentManager)
        }
        LSettings.putNightMode(this,!openType)
        onNightModeChange(!openType)
    }

    private fun closeNightMode(){
        if(nightView == null){
            return
        }
        nightView?.visibility = View.GONE
    }

    private fun openNightMode(){
        if(nightView == null){
            nightView = findViewById(R.id.nightModeView)
        }
        if(nightView == null){
            val group = layoutInflater.inflate(R.layout.night_view,window.decorView as ViewGroup)
            nightView = group.findViewById(R.id.nightModeView)
        }
        nightView?.visibility = View.VISIBLE
        nightView?.level = LSettings.getNightModeLevel(this)
    }

    protected fun isNightMode(): Boolean{
        return LSettings.isNightMode(this)
    }

    protected open fun onNightModeChange(isOpen:Boolean){

    }

    override fun onLevelChange(level: Int) {
        nightView?.level = level
    }

    override fun onLevelConfirm(level: Int) {
        nightView?.level = level
    }

    override fun onStatusBarHeightChange(height: Int) {
        super.onStatusBarHeightChange(height)
        val layoutParams = toolbar?.layoutParams
        if (layoutParams != null) {
            when(layoutParams) {
                is CoordinatorLayout.LayoutParams -> {
                    layoutParams.topMargin = height
                    toolbar?.layoutParams = layoutParams
                }
                is CollapsingToolbarLayout.LayoutParams -> {
                    layoutParams.topMargin = height
                    toolbar?.layoutParams = layoutParams
                }
                is AppBarLayout.LayoutParams -> {
                    layoutParams.topMargin = height
                    toolbar?.layoutParams = layoutParams
                }
                is FrameLayout.LayoutParams -> {
                    layoutParams.topMargin = height
                    toolbar?.layoutParams = layoutParams
                }
                is LinearLayout.LayoutParams -> {
                    layoutParams.topMargin = height
                    toolbar?.layoutParams = layoutParams
                }
            }
        }
    }

}