package liang.lollipop.lbaselib.base

import android.os.Handler
import android.os.Message

/**
 * Created by lollipop on 2018/1/2.
 * @author Lollipop
 * 简易的Handler实现类
 */
class SimpleHandler():Handler() {

    private var callback: HandlerCallback? = null

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        if (callback != null) {
            callback!!.onHandler(msg)
        }
    }

    constructor(callback: HandlerCallback):this() {
        this.callback = callback
    }

    fun setCallback(callback: HandlerCallback) {
        this.callback = callback
    }

    interface HandlerCallback {
        fun onHandler(message: Message)
    }


}