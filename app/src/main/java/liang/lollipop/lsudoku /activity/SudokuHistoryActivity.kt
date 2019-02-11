package liang.lollipop.lsudoku.activity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_sudoku_history.*
import liang.lollipop.lsudoku.R
import liang.lollipop.lsudoku.bean.SudokuBean
import liang.lollipop.lsudoku.skin.LSkinActivity
import liang.lollipop.lsudoku.skin.Skin
import liang.lollipop.lsudoku.util.FontUtil
import liang.lollipop.lsudoku.util.SudokuDbUtil
import liang.lollipop.lsudoku.util.TaskUtils

/**
 * @author Lollipop
 * 数独历史的查看页面
 */
class SudokuHistoryActivity : LSkinActivity() {

    private val historyBean = SudokuBean()

    private var historyId:String = ""

    companion object {

        const val ARG_HISTORY_ID = "ARG_HISTORY_ID"

        const val RESULT_AGAIN = 88
        const val RESULT_DATA = "RESULT_DATA"

//        const val TRANSITION_MAP = "MAP_VIEW"
        const val TRANSITION_CARD = "CARD_VIEW"
//        const val TRANSITION_LEVEL = "LEVEL_VIEW"
//        const val TRANSITION_TIME = "TIME_VIEW"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sudoku_history)


        initView()
        onRefresh()
    }

    override fun onSkinUpdate(skin: Skin) {

        skinUtil.withMapView(mapView,historyBean.srcMap,historyBean.editMap)
        logoText.setTextColor(skin.colorPrimary)
        leftLineView.setBackgroundColor(skin.colorAccent)
        rightLineView.setBackgroundColor(skin.colorAccent)

        gameLevelView.setTextColor(skin.colorAccent)
        startTimeView.setTextColor(skin.textSecondary)
        hintTimeView.setTextColor(skin.textSecondary)
        gameLengthView.setTextColor(skin.textPrimary)

        closeBtn.setTextColor(skin.textSecondary)
        againBtn.setTextColor(skin.colorAccent)
    }

    private fun initView(){
        historyId = intent.getStringExtra(ARG_HISTORY_ID)

        FontUtil.withLogoFont(logoText)
        FontUtil.withLevelFont(gameLevelView)

        againBtn.setOnClickListener(this)
        closeBtn.setOnClickListener(this)
        backgroundView.setOnClickListener(this)
    }

    override fun onRefresh() {
        super.onRefresh()
        TaskUtils.addUITask(object : TaskUtils.UICallback<Boolean, Context>{
            override fun onSuccess(result: Boolean) {
                putData()
            }

            override fun onError(e: Exception, code: Int, msg: String) {
                errorView.text = ( msg + e.localizedMessage )
            }

            override fun onBackground(args: Context?): Boolean {
                SudokuDbUtil.read(args!!).selectById(historyId,historyBean).close()
                return true
            }

        },this)
    }

    private fun putData(){

        mapView.updateNumColor(historyBean.srcMap,historyBean.editMap)
        startTimeView.text = String.format(getString(R.string.game_time),historyBean.getStartTimeName(),historyBean.getEndTimeName())
        hintTimeView.text = String.format(getString(R.string.hint_time),historyBean.getHintTimeName())
        gameLengthView.text = historyBean.getGameTimeName()
        gameLevelView.text = historyBean.getLevelName()

    }

    override fun onClick(v: View?) {
        super.onClick(v)

        when(v){

            backgroundView,closeBtn -> {
                super.onBackPressed()
            }

            againBtn -> {
                if(historyBean.map != ""){
                    val resultData = Intent()
                    resultData.putExtra(RESULT_DATA,historyBean)
                    setResult(RESULT_AGAIN,resultData)
                }
                super.onBackPressed()
            }

        }

    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        super.onBackPressed()
    }

}
