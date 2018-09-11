package liang.lollipop.lbaselib.base

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.annotation.ColorRes
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.util.Pair
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import liang.lollipop.lbaselib.util.LItemTouchCallback
import liang.lollipop.lbaselib.util.LItemTouchHelper
import liang.lollipop.simplerefreshlayout.OnScrollDownListener
import liang.lollipop.simplerefreshlayout.SimpleRefreshLayout
import liang.lollipop.simplerefreshlayout.models.CircleMaterialModel

/**
 * Created by lollipop on 2018/1/2.
 * @author Fragment的基础类
 */
open class BaseFragment:Fragment(),
        SimpleHandler.HandlerCallback,
        SimpleRefreshLayout.OnRefreshListener,
        View.OnClickListener,
        OnScrollDownListener.OnScrollListener,
        LItemTouchCallback.OnItemTouchCallbackListener,
        LItemTouchCallback.OnItemTouchStateChangedListener{



    protected var handler: Handler = SimpleHandler(this)
    protected var rootView: View? = null

    /**Glide图片加载库*/
    protected lateinit var glide: RequestManager

    companion object {

        val SCROLL_STATE_IDLE = RecyclerView.SCROLL_STATE_IDLE
        val SCROLL_STATE_SETTLING = RecyclerView.SCROLL_STATE_SETTLING
        val SCROLL_STATE_DRAGGING = RecyclerView.SCROLL_STATE_DRAGGING

    }

    private fun findRootView(view: View) {
        //获取根节点View，用于弹出SnackBar
        rootView = view
        glide = Glide.with(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findRootView(view)
    }

    override fun onClick(v: View) {

    }

    override fun onHandler(message: Message) {
    }

    override fun onRefresh() {
    }

    fun getTitle(): String {
        return ""
    }

    protected fun initRefreshLayout(refreshLayout: SimpleRefreshLayout,vararg colors: Int) {
        refreshLayout.setRefreshView(CircleMaterialModel(context)).setColorSchemeColors(*colors)
        refreshLayout.setOnRefreshListener(this)
    }

    protected fun getTouchHelper(recyclerView: RecyclerView): LItemTouchHelper {
        val helper = LItemTouchHelper.newInstance(recyclerView, this)
        helper.setStateChangedListener(this)
        return helper
    }

    protected fun startActivity(intent: Intent, vararg pair: Pair<View, String>) {
        if (pair.isEmpty()) {
            super.startActivity(intent)
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent)
        } else {
            val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!, *pair)
            startActivity(intent, optionsCompat.toBundle())
        }
    }

    protected fun withRecyclerView(recyclerView: RecyclerView){
        recyclerView.addOnScrollListener(OnScrollDownListener(recyclerView.layoutManager as LinearLayoutManager,this))
    }

    override fun onMore() {
    }

    override fun onScroll(down: Boolean, newState: Int) {
    }

    override fun onSwiped(adapterPosition: Int) {
    }

    override fun onMove(srcPosition: Int, targetPosition: Int): Boolean {
        return false
    }

    override fun onItemViewClick(holder: RecyclerView.ViewHolder?, v: View) {
    }

    override fun onItemTouchStateChanged(viewHolder: RecyclerView.ViewHolder?, status: Int) {
    }

}