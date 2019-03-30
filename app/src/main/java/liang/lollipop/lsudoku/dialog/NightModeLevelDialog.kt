package liang.lollipop.lsudoku.dialog

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import kotlinx.android.synthetic.main.dialog_night_mode_level.*
import liang.lollipop.lsudoku.R
import liang.lollipop.lsudoku.util.LSettings


/**
 * Created by lollipop on 2018/3/9.
 * @author Lollipop
 * 夜间模式的亮度调节Dialog
 */
class NightModeLevelDialog: DialogFragment(),SeekBar.OnSeekBarChangeListener {

    private var onLevelChangeCallback:OnLevelChangeCallback? = null

    private var level = 0

    companion object {

        private const val LAYOUT_ID = R.layout.dialog_night_mode_level

        private const val TAG = "NightModeLevelDialog"

        private const val MIN_LEVEL = 20

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(LAYOUT_ID,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        seekBar.setOnSeekBarChangeListener(this)

        var l = level - MIN_LEVEL
        if(l < 0){
            l = 0
        }
        if(l > 180){
            l = 180
        }
        seekBar.progress = l
        onLevelChangeCallback?.onLevelChange(level)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if(fromUser){
            level = progress + MIN_LEVEL
            onLevelChangeCallback?.onLevelChange(level)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStart() {
        super.onStart()
        val window = dialog.window
        val windowParams = window!!.attributes
        windowParams.dimAmount = 0.0f

        window.attributes = windowParams
    }

    fun show(fragmentManager: FragmentManager){
        super.show(fragmentManager,TAG)
    }

    interface OnLevelChangeCallback{
        fun onLevelChange(level:Int)
        fun onLevelConfirm(level: Int)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        onLevelChangeCallback?.onLevelConfirm(level)
        if(context != null){
            LSettings.putNightModeLevel(context!!,level)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context == null){
            return
        }
        if(context is OnLevelChangeCallback){
            onLevelChangeCallback = context
        }
        level = LSettings.getNightModeLevel(context)
    }

}