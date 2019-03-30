package liang.lollipop.lsudoku.activity

import android.animation.Animator
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.util.Pair
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.toolbar.*
import liang.lollipop.lbaselib.util.PermissionsUtil
import liang.lollipop.lcolorpalette.TintUtil
import liang.lollipop.lsudoku.R
import liang.lollipop.lsudoku.adapter.SudokuListAdapter
import liang.lollipop.lsudoku.bean.SudokuBean
import liang.lollipop.lsudoku.holder.SudokuMapHolder
import liang.lollipop.lsudoku.skin.LSkinActivity
import liang.lollipop.lsudoku.skin.Skin
import liang.lollipop.lsudoku.util.LSettings
import liang.lollipop.lsudoku.util.SudokuDbUtil
import liang.lollipop.lsudoku.util.SudokuHelper
import liang.lollipop.lsudoku.util.TaskUtils

/**
 * 主页
 * @author Lollipop
 */
class MainActivity : LSkinActivity(), SudokuListAdapter.SkinCallback {

    /**
     * 是否已经打开级别控制器
     */
    private var isLevelBarOpen = false
    /**
     * 数据集合
     */
    private val data:ArrayList<SudokuBean> = ArrayList()
    /**
     * 列表适配器
     */
    private lateinit var adapter: SudokuListAdapter
    /**
     * 数独数据库操作类
     */
    private lateinit var sudokuDbUtil:SudokuDbUtil.DBOperate
    /**
     * 文本格式化工具
     */
    private val textFormat = TintUtil.textFormat()

    companion object {

        private const val FIRST_ALERT_PERMISSIONS = "FIRST_ALERT_PERMISSIONS"

        private const val REQUEST_PERMISSIONS = 3

        private const val REQUEST_GAME = 66
        private const val REQUEST_HISTORY = 67

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setToolbar(toolbar)
        isShowBack = false
        initView()
        if(LSettings.get(this,FIRST_ALERT_PERMISSIONS,true)){
            LSettings.put(this,FIRST_ALERT_PERMISSIONS,false)
            alert().setTitle(R.string.title_request_permossions)
                    .setMessage(R.string.message_request_permossions)
                    .setPositiveButton(R.string.open) { dialog, _ ->
                        PermissionsUtil.checkPermissions(this@MainActivity,REQUEST_PERMISSIONS,PermissionsUtil.WRITE_EXTERNAL_STORAGE)
                        dialog.dismiss()
                    }
                    .setNegativeButton(R.string.no) { dialog, _ -> dialog.dismiss() }
                    .show()

        }
    }

    private fun initView(){
        sudokuDbUtil = SudokuDbUtil.write(this)

        floatingActionButton.setOnClickListener(this)
        lastGameButton.setOnClickListener(this)

        levelSeekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                levelTextView.text = "${getLevel(progress)}"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

        })
        levelSeekBar.progress = 0

        initRefreshLayout(
                refreshLayout,
                ContextCompat.getColor(this,R.color.colorPrimary),
                ContextCompat.getColor(this,R.color.colorAccent))

        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //横屏
            initRecyclerViewLandscape()
        }else{
            //不是横屏模式，那么就当做竖屏来显示
            initRecyclerViewPortrait()
        }

    }

    override fun onSkinUpdate(skin: Skin) {
        skinUtil.withFAB(floatingActionButton).withFAB(lastGameButton).whitCard(levelBody)

        adapter.notifyDataSetChanged()
    }

    /**
     * 初始化横屏模式的RV
     */
    private fun initRecyclerViewLandscape(){
        initRecyclerView(StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL))
    }

    /**
     * 初始化竖屏模式的RV
     */
    private fun initRecyclerViewPortrait(){
        initRecyclerView(StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL))
    }

    private fun initRecyclerView(layoutManager: StaggeredGridLayoutManager){
        recyclerView.layoutManager = layoutManager
        adapter = SudokuListAdapter(data,this,layoutInflater,getTouchHelper(recyclerView))
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onStart() {
        super.onStart()
        if(data.isEmpty()){
            onRefresh()
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        TaskUtils.addUITask(object :TaskUtils.UICallback<Boolean,GetDataBean>{
            override fun onSuccess(result: Boolean) {
                adapter.notifyDataSetChanged()
                refreshLayout.isRefreshing = false
            }

            override fun onError(e: Exception, code: Int, msg: String) {
                alert(msg,e.message?:getString(R.string.error_no_description))
                refreshLayout.isRefreshing = false
            }

            override fun onBackground(args: GetDataBean?): Boolean {
                args!!.dbUtil.selectAll(args.list)
                return true
            }

        },GetDataBean(data,sudokuDbUtil))
    }

    private class GetDataBean(val list: ArrayList<SudokuBean>,val dbUtil: SudokuDbUtil.DBOperate)

    private fun getLevel():Int{
        return getLevel(levelSeekBar.progress)
    }

    private fun getLevel(pro:Int):Int {
        return pro + 10
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v){

            floatingActionButton -> {

                if(isLevelBarOpen){
                    if(!TextUtils.isEmpty(LSettings.getLastGame(this))){
                        alert().setTitle(textFormat.simple(this,R.string.title_new_game,skin.textPrimary))
                                .setMessage(textFormat.simple(this,R.string.hint_new_game,skin.textSecondary))
                                .setPositiveButton(textFormat.simple(this,R.string.new_game,skin.colorAccent)) { dialog, _ ->
                                    newSudoku()
                                    dialog.dismiss()
                                }
                                .setNegativeButton(textFormat.simple(this,R.string.last_game,skin.textSecondary)) { dialog, _ ->
                                    lastSudoku()
                                    dialog.dismiss()
                                }
                                .setOnDismissListener {
                                    closeFloatingPop()
                                }
                                .show()
                    }else{
                        newSudoku()
                    }
                }else{
                    openFloatingPop()
                }
            }

            lastGameButton -> {
                lastSudoku()
                closeFloatingPop()
            }

        }
    }

    private fun newSudoku(){
        LSettings.putLastGame(this,"")
        lastSudoku()
    }

    private fun lastSudoku(){
        val newIntent = Intent(this, SudokuActivity::class.java)
        newIntent.putExtra(SudokuActivity.ARG_EMPTY_SIZE,getLevel())
        startActivityForResult(newIntent,REQUEST_GAME)
    }

    private fun closeFloatingPop(){
        isLevelBarOpen = false
        val group = gameGroup
        val centerX = group.width-floatingActionButton.width/2
        val centerY = group.height-floatingActionButton.width/2
        val startRadius = getDiam(group.width,group.height)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            val reveal = ViewAnimationUtils.createCircularReveal(group, centerX, centerY, startRadius,0F)

            reveal.addListener(object: Animator.AnimatorListener{
                override fun onAnimationRepeat(p0: Animator?) {}

                override fun onAnimationEnd(p0: Animator?) {
                    group.visibility = View.INVISIBLE
                }

                override fun onAnimationCancel(p0: Animator?) {}

                override fun onAnimationStart(p0: Animator?) {}

            })

            reveal.start()
        }else{
            group.visibility = View.INVISIBLE
        }
    }

    private fun openFloatingPop(){
        isLevelBarOpen = true
        val group = gameGroup
        val centerX = group.width-floatingActionButton.width/2
        val centerY = group.height-floatingActionButton.width/2
        val endRadius = getDiam(group.width,group.height)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            val reveal = ViewAnimationUtils.createCircularReveal(group, centerX, centerY, 0F,endRadius)

            reveal.addListener(object:Animator.AnimatorListener{
                override fun onAnimationRepeat(p0: Animator?) {}

                override fun onAnimationEnd(p0: Animator?) {}

                override fun onAnimationCancel(p0: Animator?) {}

                override fun onAnimationStart(p0: Animator?) {
                    group.visibility = View.VISIBLE
                }

            })

            reveal.start()
        }else{
            group.visibility = View.VISIBLE
        }
    }

    override fun onPause() {
        super.onPause()
        closeFloatingPop()
    }

    private fun getDiam(width:Int,height:Int):Float{
        return Math.sqrt(1.0*width*width+height*height).toFloat()
    }

    override fun onDestroy() {
        super.onDestroy()
        sudokuDbUtil.close()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){

            REQUEST_GAME -> if(resultCode == Activity.RESULT_OK){

                onRefresh()

            }

            REQUEST_HISTORY -> if(resultCode == SudokuHistoryActivity.RESULT_AGAIN){

                if(data != null){
                    val historyBean = data.getSerializableExtra(SudokuHistoryActivity.RESULT_DATA)
                    if(historyBean != null && historyBean is SudokuBean){

                        again(historyBean)

                    }
                }

            }

        }
    }

    override fun onItemViewClick(holder: RecyclerView.ViewHolder?, v: View) {
        super.onItemViewClick(holder, v)
        closeFloatingPop()
        if(holder == null){
            return
        }

        if( holder is SudokuMapHolder){

            openHistory(holder)

        }

    }

    override fun onBackPressed() {
        if(isLevelBarOpen){
            closeFloatingPop()
        }else{
            super.onBackPressed()
        }
    }

    fun again(history: SudokuBean) {
        LSettings.putLastGame(this,SudokuHelper.clearEdit(history.map))
        LSettings.putGameLengthTime(this,0L)
        LSettings.putHintLengthTime(this,0L)
        LSettings.putGameStartTime(this,System.currentTimeMillis())
        lastSudoku()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){

            R.id.menuSettings ->{

                startActivity(Intent(this,SettingsActivity::class.java))

            }

            R.id.menuNightMode -> {

                changeNightMode()

            }

        }

        return super.onOptionsItemSelected(item)
    }

    override fun getHolderSkin(): Skin {
        return skin
    }

    override fun onResume() {
        super.onResume()
        //解锁屏幕旋转功能
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    override fun onNightModeChange(isOpen: Boolean) {
        super.onNightModeChange(isOpen)
        invalidateOptionsMenu()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if(menu == null){
            return super.onPrepareOptionsMenu(menu)
        }

        val iconId = if(isNightMode()){
            R.drawable.ic_night_open_white_24dp
        }else{
            R.drawable.ic_night_close_white_24dp
        }

        menu.findItem(R.id.menuNightMode).setIcon(iconId)

        return true
    }

    private fun openHistory(holder: SudokuMapHolder){

        val bean = data[holder.adapterPosition]
        val pairArray:Array<Pair<View,String>> = arrayOf(Pair.create(holder.cardView as View,SudokuHistoryActivity.TRANSITION_CARD))
        val newIntent = Intent(this,SudokuHistoryActivity::class.java)
        newIntent.putExtra(SudokuHistoryActivity.ARG_HISTORY_ID,bean.id)
        startActivityForResult(newIntent, REQUEST_HISTORY, *pairArray)

        //固定当前屏幕方向，防止屏幕旋转
        requestedOrientation = if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

    }

}
