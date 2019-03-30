package liang.lollipop.lsudoku.util

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executors

/**
 * Created by lollipop on 2018/2/23.
 * @author lollipop
 * 任务工具类
 */
object TaskUtils {

    private val threadPool = Executors.newCachedThreadPool()
    private var sHandler:Handler? = null

    /**
     * 获取线程来执行任务
     * @param run 任务对象
     */
    fun runAs(run: Runnable) {
        threadPool.execute(run)
    }

    open class Task<RST, in ARG>(private val callBack: CallBack<RST, ARG>,private val args: ARG) : Runnable {

        override fun run() {
            try {
                callBack.success(callBack.processing(args))
            } catch (e: Exception) {
                callBack.error(e, 0, e.message?:"")
            }
        }
    }

    class UITask<RST, in ARG> (private val callBack: UICallback<RST, ARG>,private val args: ARG?) : Runnable{

        private val handler = getMainHandler()

        override fun run() {
            try {
                val result = callBack.onBackground(args)
                handler.post { callBack.onSuccess(result) }
            } catch (e: Exception) {
                handler.post { callBack.onError(e, 0, e.message?:"") }
            }
        }

    }

    interface UICallback<RST, in ARG> {
        fun onSuccess(result: RST)
        fun onError(e: Exception, code: Int, msg: String)
        fun onBackground(args: ARG?): RST
    }

    interface CallBack<RST, in ARG> {
        fun success(result: RST)
        fun error(e: Exception, code: Int, msg: String)
        fun processing(args: ARG): RST
    }

    fun <RST, ARG> addTask(task: Task<RST, ARG>) {
        runAs(task)
    }

    fun <RST, ARG> addTask(callBack: CallBack<RST,ARG>, args: ARG) {
        runAs(Task(callBack, args))
    }

    fun <RST, ARG> addUITask(callBack: UICallback<RST,ARG>, args: ARG? = null) {
        runAs(UITask(callBack, args))
    }

    abstract class CallBackOnUI <RST, in ARG> private constructor(protected val handler: Handler) : CallBack<RST, ARG> {

        constructor():this(getMainHandler())

        override fun success(result: RST) {
            handler.post({ onUISuccess(result) })
        }

        override fun error(e: Exception, code: Int, msg: String) {
            handler.post({ onUIError(e, code, msg) })
        }

        override fun processing(args: ARG): RST {
            return onBackground(args)
        }

        abstract fun onUISuccess(result: RST)
        abstract fun onUIError(e: Exception, code: Int, msg: String)
        abstract fun onBackground(args: ARG): RST

    }

    private fun getMainHandler(): Handler {
        synchronized(TaskUtils::class.java) {
            if (sHandler == null) {
                sHandler = Handler(Looper.getMainLooper())
            }
            return sHandler!!
        }
    }


}