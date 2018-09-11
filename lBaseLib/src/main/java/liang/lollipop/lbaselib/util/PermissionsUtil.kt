package liang.lollipop.lbaselib.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import java.util.*

/**
 * Created by lollipop on 2018/1/17.
 * @author Lollipop
 * 权限检查的工具类
 */
object PermissionsUtil {

    /**
     * GMS相关权限未罗列,如果需要可以使用 adb shell pm list permissions -g -d 命令查看
     * 据文档说明,同一个权限组中的某个权限被授权,那么同组其他权限也会被授权.
     * 即,权限申请是以组的形式存在.
     * 部分基本权限因不需要申请,所以未声明
     */

    /**
     * 日历权限
     * （日历读写权限）
     */
    val CALENDAR = Manifest.permission_group.CALENDAR

    val READ_CALENDAR = Manifest.permission.READ_CALENDAR
    val WRITE_CALENDAR = Manifest.permission.WRITE_CALENDAR
    //写入设置权限
    val WRITE_SETTINGS = Manifest.permission.WRITE_SETTINGS

    /**
     * 相机使用权限
     * （相机相关，包括闪光灯）
     */
    val CAMERA = Manifest.permission_group.CAMERA

    val CAMERA_ = Manifest.permission.CAMERA

    /**
     * 通讯录权限
     * （读写联系人，获取账户）
     */
    val CONTACTS = Manifest.permission_group.CONTACTS

    val WRITE_CONTACTS = Manifest.permission.WRITE_CONTACTS
    val GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS
    val READ_CONTACTS = Manifest.permission.READ_CONTACTS


    /**
     * 位置权限
     */
    val LOCATION = Manifest.permission_group.LOCATION

    val ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    val ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION


    /**
     * 麦克风权限
     */
    val MICROPHONE = Manifest.permission_group.MICROPHONE

    val RECORD_AUDIO = Manifest.permission.RECORD_AUDIO

    /**
     * 获取手机状态
     * （读取手机状态等）
     */
    val PHONE = Manifest.permission_group.PHONE

    val READ_CALL_LOG = Manifest.permission.READ_CALL_LOG
    val READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE
    val CALL_PHONE = Manifest.permission.CALL_PHONE
    val WRITE_CALL_LOG = Manifest.permission.WRITE_CALL_LOG
    val USE_SIP = Manifest.permission.USE_SIP
    val PROCESS_OUTGOING_CALLS = Manifest.permission.PROCESS_OUTGOING_CALLS
    val ADD_VOICEMAIL = Manifest.permission.ADD_VOICEMAIL

    /**
     * 传感器
     */
    val SENSORS = Manifest.permission_group.SENSORS

    val BODY_SENSORS = Manifest.permission.BODY_SENSORS


    /**
     * 短信权限
     * (读发短信)
     */
    val SMS = Manifest.permission_group.SMS

    val READ_SMS = Manifest.permission.READ_SMS
    val RECEIVE_WAP_PUSH = Manifest.permission.RECEIVE_WAP_PUSH
    val RECEIVE_MMS = Manifest.permission.RECEIVE_MMS
    val RECEIVE_SMS = Manifest.permission.RECEIVE_SMS
    val SEND_SMS = Manifest.permission.SEND_SMS

    /**
     * 储存权限
     * (文件的读取和写入)
     */
    val STORAGE = Manifest.permission_group.STORAGE

    val READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
    val WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE

    /**
     * 获取权限集中需要申请权限的列表
     */
    fun findDeniedPermissions(activitiy: Activity, vararg permissions: String): List<String> {
        return permissions.filter {
            ContextCompat.checkSelfPermission(activitiy, it) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(activitiy, it)
        }
    }

    /**
     * 检查权限,activtiy调用本方法
     */
    fun checkPermissions(activity: Activity, requestCode: Int, onPermissionsPass: OnPermissionsPass?, vararg permissions: String) {
        val needRequestPermissonList = findDeniedPermissions(activity, *permissions)
        if (needRequestPermissonList.isNotEmpty()) {
            ActivityCompat.requestPermissions(activity,
                    needRequestPermissonList.toTypedArray(), requestCode)
        } else {
            onPermissionsPass?.onPermissionsPass()
        }
    }

    fun checkPermissions(activity: Activity, vararg permissions: String): Boolean {
        val needRequestPermissionList = findDeniedPermissions(activity, *permissions)
        return needRequestPermissionList.isEmpty()
    }

    fun checkPermissions(activity: Activity, requestCode: Int, vararg permissions: String) {
        checkPermissions(activity, requestCode, null, *permissions)
    }

    /**
     * 检测是否所有的权限都已经授权
     */
    fun verifyPermissions(grantResults: IntArray): Boolean {
        return grantResults.none { it != PackageManager.PERMISSION_GRANTED }
    }

    /**
     * 检查未授权的权限
     */
    fun verifyPermissions(permissions: Array<String>, grantResults: IntArray): ArrayList<String> {
        return grantResults.indices
                .filter { grantResults[it] != PackageManager.PERMISSION_GRANTED }
                .mapTo(ArrayList()) { permissions[it] }
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    fun startAppSettings(context: Context) {
        val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:" + context.packageName)
        context.startActivity(intent)
    }

    /**
     * 弹出权限申请对话框
     *
     * @param msg 消息内容
     */
    fun popPermissionsDialog(context: Context, msg: String,title:String,yesBtn:String,noBtn:String) {
        AlertDialog.Builder(context).setTitle(title)
                .setMessage(msg)
                .setPositiveButton(yesBtn, { dialog, _ ->
                    startAppSettings(context)
                    dialog.dismiss()
                })
                .setNegativeButton(noBtn, { dialog, _ ->
                    dialog.dismiss()
                })
                .show()

    }

    fun checkPermission(context: Context, permission: String): Boolean {
        var result = false
        if (Build.VERSION.SDK_INT >= 23) {
            result = try {
                val clazz = Class.forName("android.content.Context")
                val method = clazz.getMethod("checkSelfPermission", String::class.java)
                val rest = method.invoke(context, permission) as Int
                (rest == PackageManager.PERMISSION_GRANTED)
            } catch (e: Exception) {
                false
            }

        } else {
            val pm = context.packageManager
            if (pm.checkPermission(permission, context.packageName) == PackageManager.PERMISSION_GRANTED) {
                result = true
            }
        }
        return result
    }

    /**
     * 方便分装做的一个回调,用于在权限直接通过时使用
     */
    interface OnPermissionsPass {
        fun onPermissionsPass()
    }

}