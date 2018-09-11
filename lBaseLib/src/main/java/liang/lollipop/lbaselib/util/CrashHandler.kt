package liang.lollipop.lbaselib.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by lollipop on 2018/1/17.
 * @author Lollipop
 * 崩溃异常手机Handler
 */
class CrashHandler private constructor(): Thread.UncaughtExceptionHandler {

    companion object {

        private val TAG = "CrashHandler"

        fun get(): CrashHandler{
            return Inner.crashHandler
        }

        fun init(context: Context,logDir:String): CrashHandler {
            return get().apply { initSelf(context,logDir) }
        }

    }

    private object Inner{
        val crashHandler = CrashHandler()
    }

    //系统默认的UncaughtException处理类
    private lateinit var defaultHandler: Thread.UncaughtExceptionHandler
    //程序的Context对象
    private lateinit var context: Context
    //用来存储设备信息和异常信息
    private val infos = HashMap<String, String>()
    //用于格式化日期,作为日志文件名的一部分
    private val formatter = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINESE)
    //默认在根目录文件夹
    private var logDir:String = Environment.getExternalStorageDirectory().absolutePath

    fun initSelf(context: Context,logDir:String){
        this.context = context
        if(!TextUtils.isEmpty(logDir)){
            this.logDir = logDir
        }
        this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(thread: Thread?, ex: Throwable?) {
        if(ex==null){
            defaultHandler.uncaughtException(thread,ex)
            return
        }
        Log.d("error", ex.message + ";" + ex.localizedMessage)
        if (!handleException(ex)) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            defaultHandler.uncaughtException(thread, ex)
        } else {
            try {
                Thread.sleep(3000)
            } catch (e: InterruptedException) {
                Log.e(TAG, "error : ", e)
            }
        }
    }

    private fun outputTextFile(value: String): String {
        return outputTextFile(value, formatter.format(Date()))
    }

    private fun outputTextFile(value: String, name: String): String {
        return try {
            val file = File(logDir,name + ".txt")
            val path = File(logDir)
            if (!path.exists()) {
                path.mkdirs()
            }
            val outStream = FileOutputStream(file, true)
            val writer = OutputStreamWriter(outStream, "gbk")
            writer.write(value)
            writer.flush()
            writer.close()//记得关闭
            file.absolutePath
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private fun handleException(ex: Throwable?): Boolean {
        if (ex == null) {
            return false
        }
        //保存日志文件
        saveCrashInfo2File(context, ex)
        //使用Toast来显示异常信息
        object : Thread() {
            override fun run() {
                Looper.prepare()
                android.os.Process.killProcess(android.os.Process.myPid())
                Looper.loop()
            }
        }.start()
        Log.e("error", ex.message)
        return true
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    private fun collectDeviceInfo(ctx: Context): HashMap<String, String> {
        val deviceInfo = HashMap<String, String>()
        try {
            val pm = ctx.packageManager
            val pi = pm.getPackageInfo(ctx.packageName, PackageManager.GET_ACTIVITIES)
            if (pi != null) {
                val versionName = if (pi.versionName == null) "null" else pi.versionName
                val versionCode = pi.versionCode.toString() + ""
                deviceInfo.put("versionName", versionName)
                deviceInfo.put("versionCode", versionCode)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            //            Log.e(TAG, "an error occured when collect package info", e);
        }

        val fields = Build::class.java.declaredFields
        for (field in fields) {
            try {
                field.isAccessible = true
                deviceInfo.put(field.name, field.get(null).toString())
            } catch (e: Exception) {
            }

        }
        return deviceInfo
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private fun saveCrashInfo2File(context: Context, ex: Throwable): String {

        val stringBuffer = StringBuffer()
        val deviceInfo = collectDeviceInfo(context)
        for ((key, value) in deviceInfo) {
            stringBuffer.append(key + "=" + value + "\n")
        }

        val writer = StringWriter()
        val printWriter = PrintWriter(writer)
        ex.printStackTrace(printWriter)
        var cause: Throwable? = ex.cause
        while (cause != null) {
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }
        printWriter.close()
        val result = writer.toString()
        stringBuffer.append(result)
        return outputTextFile(stringBuffer.toString())
    }

}