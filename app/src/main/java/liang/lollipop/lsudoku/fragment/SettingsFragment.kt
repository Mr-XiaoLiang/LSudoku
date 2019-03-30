package liang.lollipop.lsudoku.fragment

import android.content.Context
import android.os.Bundle
import android.os.Message
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceGroup
import liang.lollipop.lbaselib.base.SimpleHandler
import liang.lollipop.lsudoku.R
import liang.lollipop.lsudoku.skin.LSkinActivity
import liang.lollipop.lsudoku.skin.Skin

/**
 * Created by lollipop on 2018/3/6.
 * @author Lollipop
 * 设置的Fragment
 */
class SettingsFragment: PreferenceFragment(), Preference.OnPreferenceChangeListener,SimpleHandler.HandlerCallback,LSkinActivity.SkinUpdateCallback {

    private var onColorChangeCallback: OnColorChangeCallback? = null

    private val handler = SimpleHandler(this)

    companion object {

        private const val WHAT_ON_COLOR_CHANGE = 123

    }

//    private val skinPreferenceUtil = SkinPreferenceUtil()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference_settings)
        val preferenceList = getPreferenceList(preferenceScreen)
        for (pre in preferenceList){
            pre.onPreferenceChangeListener = this
        }
    }

    override fun onPreferenceChange(preference: Preference?, value: Any?): Boolean{

        handler.sendEmptyMessageDelayed(WHAT_ON_COLOR_CHANGE,50)

        return true
    }

    override fun onHandler(message: Message) {
        when(message.what){

            WHAT_ON_COLOR_CHANGE -> {

                onColorChangeCallback?.onColorChange()

            }

        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if( context != null && context is OnColorChangeCallback ){
            onColorChangeCallback = context
        }
        if(context is LSkinActivity){
            context.addSkinUpdateCallback(this)
        }
    }

    override fun onDetach() {
        super.onDetach()
        onColorChangeCallback = null
    }

    override fun onSkinUpdate(skin: Skin) {
//        if(preferenceScreen != null){
//            skinPreferences(preferenceScreen,skin)
//        }

    }

//    private fun skinPreferences(preferenceGroup: PreferenceGroup,skin: Skin){
//        val listView = view.findViewById<ListView>(android.R.id.list)
//        val count = listView.adapter.count
//        val preferenceList = getPreferenceList(preferenceGroup)
//        for(index in 0 until count ){
//            val preference = preferenceList[index]
//            when(preference){
//
//                is PreferenceCategory -> {
//
//                    skinPreferenceUtil.setBodyView(listView.getChildAt(index))
//                    skinPreferenceUtil.onSkinChange(skin)
//
//                }
//
//                is ColorPreference -> {
//
//                    preference.onSkinChange(skin)
//
//                }
//
//                is SwitchPreference -> {
//
//                    skinPreferenceUtil.setBodyView(listView.getChildAt(index))
//                    skinPreferenceUtil.onSkinChange(skin)
//
//                }
//
//            }
//        }
//    }

    private fun getPreferenceList(group: PreferenceGroup): List<Preference>{

        val preferenceList = ArrayList<Preference>()

        preferenceList.add(group)

        val childCound = group.preferenceCount
        for(index in 0 until childCound){

            val child = group.getPreference(index)
            if(child is PreferenceGroup){
                preferenceList.addAll(getPreferenceList(child))
            }else{
                preferenceList.add(child)
            }
        }

        return preferenceList

    }

    interface OnColorChangeCallback{
        fun onColorChange()
    }

}