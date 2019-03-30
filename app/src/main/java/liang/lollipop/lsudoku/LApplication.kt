package liang.lollipop.lsudoku

import android.app.Activity
import android.app.Application
import android.os.Bundle
import liang.lollipop.lsudoku.util.Constant
import liang.lollipop.lbaselib.util.CrashHandler
import liang.lollipop.lsudoku.skin.LSkinActivity
import liang.lollipop.lsudoku.skin.Skin
import liang.lollipop.lsudoku.util.LSettings
import java.util.ArrayList

/**
 * Created by lollipop on 2018/3/2.
 * @author Lollipop
 * 应用上下文
 */
class LApplication: Application(),Application.ActivityLifecycleCallbacks {

    companion object {

        val appSkin:Skin = Skin()

    }

    private val activities = ArrayList<Activity>()

    override fun onCreate() {
        super.onCreate()

        CrashHandler.init(this, Constant.getLogPath())

        registerActivityLifecycleCallbacks(this)

        onSkinChange()
    }

    override fun onActivityPaused(activity: Activity?) {
    }

    override fun onActivityResumed(activity: Activity?) {
    }

    override fun onActivityStarted(activity: Activity?) {
    }

    override fun onActivityDestroyed(activity: Activity?) {
        if(activity != null){
            activities.remove(activity)
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity?) {
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        if(activity != null){
            activities.add(activity)
        }
    }

    private fun requestUpdateSkin(){
        for(activity in activities){
            if(activity is LSkinActivity){
                activity.updateSkin(appSkin)
            }
        }
    }

    fun onSkinChange(){
        LSettings.updateSkin(appSkin,this)
        requestUpdateSkin()
    }

}