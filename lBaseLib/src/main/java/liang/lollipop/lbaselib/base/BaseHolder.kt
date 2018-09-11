package liang.lollipop.lbaselib.base

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.RequestManager
import liang.lollipop.lbaselib.util.LItemTouchHelper

/**
 * Created by lollipop on 2018/1/2.
 * @author Lollipop
 * 基础的Item Holder
 */
abstract class BaseHolder<in T:BaseBean>(itemView: View): RecyclerView.ViewHolder(itemView),View.OnClickListener {

    protected var touch: LItemTouchHelper? = null
    protected var canSwipe = false
    protected var canMove = false
    protected val context:Context = itemView.context
    protected var glide: RequestManager? = null

    init {
        itemView.setOnClickListener(this)
    }

    abstract fun onBind(bean:T)

    fun canSwipe():Boolean{
        return canSwipe
    }

    fun canMove():Boolean{
        return canMove
    }

    override fun onClick(v: View?) {
        if(v!=null && touch!=null){
            touch!!.onItemViewClick(this,v)
        }
    }

    fun setTouchHelper(helper:LItemTouchHelper){
        touch = helper
    }

    fun withGlide(requestManager: RequestManager){
        this.glide = requestManager
    }

}