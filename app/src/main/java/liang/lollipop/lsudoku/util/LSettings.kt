package liang.lollipop.lsudoku.util

import android.content.Context
import android.support.v4.content.ContextCompat
import liang.lollipop.lbaselib.util.PreferencesUtil
import liang.lollipop.lsudoku.R
import liang.lollipop.lsudoku.skin.Skin

/**
 * Created by lollipop on 2018/2/27.
 * @author Lollipop
 * 设置相关
 */
object LSettings{

    private const val KEY_LAST_GAME = "KEY_LAST_GAME"
    private const val KEY_GAME_START_TIME = "KEY_GAME_START_TIME"
    private const val KEY_GAME_LENGTH_TIME = "KEY_GAME_LENGTH_TIME"
    private const val KEY_HINT_LENGTH_TIME = "KEY_HINT_LENGTH_TIME"

    private const val KEY_NIGHT_MODE = "KEY_NIGHT_MODE"
    private const val KEY_NIGHT_MODE_LEVEL = "KEY_NIGHT_MODE_LEVEL"

    fun isNightMode(context: Context): Boolean{
        return get(context, KEY_NIGHT_MODE,false)
    }

    fun putNightMode(context: Context,boolean: Boolean){
        put(context, KEY_NIGHT_MODE,boolean)
    }

    fun getNightModeLevel(context: Context):Int{
        return get(context, KEY_NIGHT_MODE_LEVEL,110)
    }

    fun putNightModeLevel(context: Context,level:Int){
        put(context, KEY_NIGHT_MODE_LEVEL,level)
    }

    fun putLastGame(context: Context,value:String){
        put(context, KEY_LAST_GAME,value)
    }

    fun getLastGame(context: Context): String{
        return get(context, KEY_LAST_GAME,"")
    }

    fun putGameStartTime(context: Context,time:Long){
        put(context,KEY_GAME_START_TIME,time)
    }

    fun getGameStartTime(context: Context):Long{
        return get(context,KEY_GAME_START_TIME,0L)
    }

    fun putGameLengthTime(context: Context,time:Long){
        put(context,KEY_GAME_LENGTH_TIME,time)
    }

    fun getGameLengthTime(context: Context):Long{
        return get(context,KEY_GAME_LENGTH_TIME,0L)
    }

    fun putHintLengthTime(context: Context,time:Long){
        put(context,KEY_HINT_LENGTH_TIME,time)
    }

    fun getHintLengthTime(context: Context):Long{
        return get(context,KEY_HINT_LENGTH_TIME,0L)
    }

    fun isShowPreview(context: Context): Boolean{
        return get(context,R.string.key_answer,true)
    }

    fun isShowInoutHint(context: Context): Boolean{
        return get(context,R.string.key_input_hint,true)
    }

    fun <T> put(context: Context,key:String,value: T){
        PreferencesUtil.put(context,key,value)
    }

    inline fun <reified T> get(context: Context,key:String,value: T): T{
        return PreferencesUtil.get(context,key,value)
    }

    inline fun <reified T> get(context: Context,key:Int,value: T): T{
        return PreferencesUtil.get(context,context.getString(key),value)
    }

    fun updateSkin(skin: Skin,context: Context){
        skin.colorAccent = getColorSettings(context, R.string.key_color_accent,R.color.colorAccent)
        skin.colorDivider = getColorSettings(context, R.string.key_color_divider,R.color.colorDivider)
        skin.colorPrimary = getColorSettings(context, R.string.key_color_primary,R.color.colorPrimary)
        skin.colorPrimaryDark = getColorSettings(context, R.string.key_color_primary_dark,R.color.colorPrimaryDark)
        skin.colorPrimaryLight = getColorSettings(context, R.string.key_color_primary_light,R.color.colorPrimaryLight)
        skin.textPrimary = getColorSettings(context, R.string.key_text_primary,R.color.textPrimary)
        skin.textSecondary = getColorSettings(context, R.string.key_text_secondary,R.color.textSecondary)

        skin.selectedColor = getColorSettings(context, R.string.key_map_selected_color,R.color.selectedColor)
        skin.srcColor = getColorSettings(context, R.string.key_map_src_color,R.color.textPrimary)
        skin.editColor = getColorSettings(context, R.string.key_map_edit_color,R.color.textSecondary)
        skin.warningColor = getColorSettings(context, R.string.key_map_warning_color,R.color.warningColor)
        skin.associateColor = getColorSettings(context, R.string.key_map_associate_color,R.color.associateColor)

        skin.borderColor = getColorSettings(context, R.string.key_map_border_color,R.color.borderColor)
        skin.gridColor = getColorSettings(context, R.string.key_map_grid_color,R.color.gridColor)
        skin.bigGridColor = getColorSettings(context, R.string.key_map_big_grid_color,R.color.bigGridColor)
        skin.symbolColor = getColorSettings(context, R.string.key_map_symbol_color,R.color.symbolColor)

        skin.associateHint = get(context,R.string.key_related_hint,true)
        skin.warningHint = get(context,R.string.key_failing,true)
    }

    private fun getColorSettings(context: Context,name:Int,def:Int): Int{
        return get(context,context.getString(name),ContextCompat.getColor(context,def))
    }

}