package liang.lollipop.lsudoku.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.os.Message
import android.support.annotation.CallSuper
import android.support.v7.graphics.drawable.DrawerArrowDrawable
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_sudoku.*
import kotlinx.android.synthetic.main.content_sudoku.*
import kotlinx.android.synthetic.main.include_num_btn.*
import kotlinx.android.synthetic.main.include_symbol_panel.*
import liang.lollipop.lcolorpalette.TintUtil
import liang.lollipop.lsudoku.R
import liang.lollipop.lsudoku.bean.SudokuBean
import liang.lollipop.lsudoku.skin.LSkinActivity
import liang.lollipop.lsudoku.skin.Skin
import liang.lollipop.lsudoku.util.*
import liang.lollipop.lsudoku.view.SudokuMapView

/**
 * 数独的游戏界面
 * @author Lollipop
 */
class SudokuActivity : LSkinActivity(),
        SudokuMapView.OnGridClickListener,
        SudokuController.Callback {

    /**
     * 数独游戏控制器
     */
    private lateinit var sudokuController: SudokuController

    /**
     * 空位数量，即level
     */
    private var levelSize = DEFAULT_EMPTY_SIZE

    /**
     * 是否保存游戏
     */
    private var saveGame = true

    /**
     * 游戏时长
     */
    private var gameLengthTime = 0L
    /**
     * 本次开始的时间，用于累计单次游玩的时间
     */
    private var thisStartTime = 0L

    /**
     * 提示时长累计变量
     */
    private var hintLengthTime = 0L

    /**
     * 提示的按下时间戳
     */
    private var hintPressTime = 0L

    /**
     * 数独数据库操作类，用户保存数独记录
     */
    private lateinit var sudokuDbUtil: SudokuDbUtil.DBOperate

    /**
     * 文本格式化工具
     */
    private val textFormat = TintUtil.textFormat()

    /**
     * 标记面板的动画辅助类
     */
    private var symbolPanelAnimationHelper: SymbolPanelAnimationHelper? = null

    /**
     * 标记面板的逻辑辅助类
     */
    private var symbolHelper: SymbolHelper? = null

    companion object {

        const val ARG_EMPTY_SIZE = "ARG_EMPTY_SIZE"

        private const val WHAT_TIME_UPDATE = 45

        private const val DEFAULT_EMPTY_SIZE = 30

        private const val ONE_SECOND = 1000L

        private const val ONE_MINUTE = ONE_SECOND * 60

        private const val ONE_HOUR = ONE_MINUTE * 60

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sudoku)
        initView()
        levelSize = intent.getIntExtra(ARG_EMPTY_SIZE, DEFAULT_EMPTY_SIZE)
    }

    override fun onStart() {
        super.onStart()
        val lastGame = LSettings.getLastGame(this)
        if (TextUtils.isEmpty(lastGame)) {
            newGame()
        } else {

            sudokuController.parse(lastGame)
            levelSize = sudokuController.getLevel()
            gameLengthTime = LSettings.getGameLengthTime(this)
            hintLengthTime = LSettings.getHintLengthTime(this)

        }
        levelTitleView.text = "L$levelSize"
        handler.sendEmptyMessage(WHAT_TIME_UPDATE)

        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    override fun onStop() {
        super.onStop()
        handler.removeMessages(WHAT_TIME_UPDATE)
        if (saveGame) {
            LSettings.putLastGame(this, sudokuController.toString())
            LSettings.putHintLengthTime(this, hintLengthTime)
        } else {
            LSettings.putLastGame(this, "")
            LSettings.putGameStartTime(this, 0L)
            LSettings.putGameLengthTime(this, 0L)
            LSettings.putHintLengthTime(this, 0L)
        }

    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
        thisStartTime = System.currentTimeMillis()
    }

    override fun onPause() {
        super.onPause()
        val thisStopTime = System.currentTimeMillis()
        gameLengthTime += (thisStopTime - thisStartTime)
        LSettings.putGameLengthTime(this, gameLengthTime)
    }

    override fun onSkinUpdate(skin: Skin) {
        sudokuController.onSkinUpdate(skinUtil)
        titleBar.setBackgroundColor(skin.colorPrimary)
        keybordGroup.setCardBackgroundColor(skin.colorPrimary)
        statusBarView.setBackgroundColor(skin.colorPrimary)
    }

    private fun initView() {
        sudokuDbUtil = SudokuDbUtil.write(this)

        hintView.text = ""

        sudokuController = SudokuController(mapView, this)

        mapView.onSkinChange(skin)

        mapView.onGridClickListener = this
        numBtn1.setOnClickListener(this)
        numBtn2.setOnClickListener(this)
        numBtn3.setOnClickListener(this)
        numBtn4.setOnClickListener(this)
        numBtn5.setOnClickListener(this)
        numBtn6.setOnClickListener(this)
        numBtn7.setOnClickListener(this)
        numBtn8.setOnClickListener(this)
        numBtn9.setOnClickListener(this)
        numBtnClear.setOnClickListener(this)
        nightModeBtn.setOnClickListener(this)
        if (LSettings.isShowPreview(this)) {
            previewBtn.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_UP -> {
                        preview(false)
                        true
                    }

                    MotionEvent.ACTION_DOWN -> {
                        preview(true)
                        true
                    }

                    else -> {
                        super.onTouchEvent(event)
                    }

                }
            }
            previewBtn.visibility = View.VISIBLE
        } else {
            previewBtn.visibility = View.GONE
        }

        if (LSettings.isShowInoutHint(this)) {
            hintView.visibility = View.VISIBLE
        } else {
            hintView.visibility = View.INVISIBLE
        }

        backBtn.setOnClickListener(this)
        val backDrawable = DrawerArrowDrawable(this)
        backDrawable.color = Color.WHITE
        backDrawable.progress = 1F
        backBtn.setImageDrawable(backDrawable)

        statusView.setOnClickListener(this)

        symbolPanelView.setOnClickListener(this)

        symbolPanelAnimationHelper = SymbolPanelAnimationHelper(symbolPanelView,
                symbolPanelCard, symbolPanelBg)
        symbolHelper = SymbolHelper(arrayOf(symbolNumBtn1, symbolNumBtn2, symbolNumBtn3,
                symbolNumBtn4, symbolNumBtn5, symbolNumBtn6,
                symbolNumBtn7, symbolNumBtn8, symbolNumBtn9)) {
                sudokuController.changeSymbol(it)
            }
        symbolHelper?.selectedColor = skin.colorAccent
        symbolHelper?.unselectedColor = skin.colorPrimary
    }

    private fun preview(enable: Boolean) {
        if (enable) {
            hintPressTime = System.currentTimeMillis()
        } else {
            val hintUpTime = System.currentTimeMillis()
            hintLengthTime += (hintUpTime - hintPressTime)
        }
        sudokuController.preview(enable)
    }

    private fun newGame() {
        sudokuController.selected(-1, -1)
        sudokuController.generate(levelSize)
        gameLengthTime = 0
        LSettings.putGameStartTime(this, System.currentTimeMillis())
        thisStartTime = System.currentTimeMillis()
        hintLengthTime = 0
        LSettings.putHintLengthTime(this, 0L)
    }

    private fun restart() {
        sudokuController.restart()
    }

    private fun putNum(num: Int) {
        sudokuController.putNum(num)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        hideSystemUI()
        when (v) {
            backBtn -> {
                onBackPressed()
            }
            numBtn1 -> {
                putNum(1)
            }
            numBtn2 -> {
                putNum(2)
            }
            numBtn3 -> {
                putNum(3)
            }

            numBtn4 -> {
                putNum(4)
            }

            numBtn5 -> {
                putNum(5)
            }

            numBtn6 -> {
                putNum(6)
            }

            numBtn7 -> {
                putNum(7)
            }

            numBtn8 -> {
                putNum(8)
            }

            numBtn9 -> {
                putNum(9)
            }

            numBtnClear -> {
                putNum(0)
            }

            nightModeBtn -> {
                changeNightMode()
            }

            statusView -> {
                alert().setTitle(textFormat.simple(this, R.string.new_map, skin.textPrimary))
                        .setMessage(textFormat.simple(this, R.string.hint_new_map, skin.textPrimary))
                        .setPositiveButton(textFormat.simple(this, R.string.change, skin.colorAccent)) { dialog, _ ->
                            newGame()
                            dialog?.dismiss()
                        }
                        .setNegativeButton(textFormat.simple(this, R.string.no_change, skin.textSecondary)) { dialog, _ -> dialog?.dismiss() }
                        .setNeutralButton(textFormat.simple(this, R.string.again, skin.textSecondary)) { dialog, _ ->
                            restart()
                            dialog?.dismiss()
                        }
                        .show()

            }
            symbolPanelView -> {
                hideSymbolPanel()
            }
        }

    }

    override fun onNightModeChange(isOpen: Boolean) {
        super.onNightModeChange(isOpen)
        nightModeBtn.setImageResource(
                if (isOpen) {
                    R.drawable.ic_night_open_white_24dp
                } else {
                    R.drawable.ic_night_close_white_24dp
                }
        )
    }

    override fun onGridClick(view: View, x: Int, y: Int) {
        hideSystemUI()
        sudokuController.selected(x, y)
        if (sudokuController.canEdit(x, y)) {
            val intArray = sudokuController.hint(x, y).filter { it > 0 }
            val stringBuilder = StringBuilder(getString(R.string.hint))
            for (i in intArray) {
                stringBuilder.append(i)
                stringBuilder.append(" ")
            }
            hintView.text = stringBuilder.toString()
        } else {
            hintView.text = ""
        }
    }

    override fun onGridLongClick(view: View, x: Int, y: Int) {
        hideSystemUI()
        if (!sudokuController.canEdit(x, y)) {
            return
        }
        onGridClick(view, x, y)
        showSymbolPanel(view, x, y)
    }

    override fun onLoad() {
        progressBar.isIndeterminate = true
        progressBar.visibility = View.VISIBLE
    }

    override fun onLoadEnd() {
        progressBar.isIndeterminate = false
        progressBar.visibility = View.INVISIBLE
    }

    override fun onError(e: Exception, code: Int, msg: String) {
        alert(getString(R.string.error), e.localizedMessage + "," + msg)
    }

    override fun onSuccess(srcMap: Array<IntArray>, editMap: Array<IntArray>) {

        //只要完成一局，那么就请求刷新
        setResult(Activity.RESULT_OK)

        saveGameHistory()

        alert().setTitle(getString(R.string.successful))
                .setMessage(getString(R.string.successful_msg))
                .setPositiveButton(R.string.open_new_game) { dialog, _ ->
                    newGame()
                    dialog.dismiss()
                }
                .setNeutralButton(R.string.exit) { dialog, _ ->
                    saveGame = false
                    onBackPressed()
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.try_it) { dialog, _ ->
                    restart()
                    dialog.dismiss()
                }
                .show()
    }

    override fun onWarning(warning: Boolean) {
        statusView.setImageResource(if (warning) {
            R.drawable.ic_sentiment_very_dissatisfied_black_24dp
        } else {
            R.drawable.ic_sentiment_very_satisfied_black_24dp
        })
    }

    override fun onHandler(message: Message) {
        super.onHandler(message)
        when (message.what) {

            WHAT_TIME_UPDATE -> {

                timeView.text = formatTime()
                handler.sendEmptyMessageDelayed(WHAT_TIME_UPDATE, 1000)

            }

        }
    }

    private fun formatTime(): String {
        if (thisStartTime == 0L) {
            return ""
        }
        val time = System.currentTimeMillis() - thisStartTime + gameLengthTime
        val hour = time / ONE_HOUR
        val minute = time % ONE_HOUR / ONE_MINUTE
        val second = time % ONE_MINUTE / ONE_SECOND
        return if (hour > 0) {
            formatNum(hour) + ":" + formatNum(minute)
        } else {
            formatNum(minute) + ":" + formatNum(second)
        }
    }

    private fun saveGameHistory() {
        val bean = SudokuBean()
        val endTime = System.currentTimeMillis()
        bean.endTime = endTime
        bean.startTime = LSettings.getGameStartTime(this)
        bean.timeLength = gameLengthTime + (endTime - thisStartTime)
        bean.map = sudokuController.toString()
        bean.hintTime = hintLengthTime
        bean.level = levelSize

        TaskUtils.addUITask(object : TaskUtils.UICallback<Boolean, InstallDataBean> {
            override fun onSuccess(result: Boolean) {}

            override fun onError(e: Exception, code: Int, msg: String) {
                alert(getString(R.string.save_error), e.message
                        ?: getString(R.string.error_no_description))
            }

            override fun onBackground(args: InstallDataBean?): Boolean {
                args!!.dbUtil.install(args.bean)
                return true
            }

        }, InstallDataBean(bean, sudokuDbUtil))
    }

    private fun formatNum(num: Long): String {
        return if (num < 10) {
            "0$num"
        } else {
            "" + num
        }
    }

    private class InstallDataBean(val bean: SudokuBean, val dbUtil: SudokuDbUtil.DBOperate)

    override fun onWindowInsetsChange(left: Int, top: Int, right: Int, bottom: Int) {
        super.onWindowInsetsChange(left, top, right, bottom)
        val layoutParams = statusBarView.layoutParams as LinearLayout.LayoutParams
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 竖屏
            layoutParams.height = top
        } else {
            layoutParams.height = top
        }
        statusBarView.layoutParams = layoutParams
    }

    private fun showSymbolPanel(view: View, x: Int, y: Int) {
        symbolPanelAnimationHelper?.showSymbolPanel(view)
        symbolHelper?.update(sudokuController.symbol(x, y))
    }

    private fun hideSymbolPanel() {
        symbolPanelAnimationHelper?.hideSymbolPanel()
    }

}
