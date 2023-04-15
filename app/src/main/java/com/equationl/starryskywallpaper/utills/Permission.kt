package com.equationl.starryskywallpaper.utills

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.CountDownTimer
import android.os.Environment
import android.provider.Settings
import android.text.TextUtils
import android.widget.Toast
import com.equationl.starryskywallpaper.R
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.lang.reflect.InvocationTargetException
import java.util.Properties


/**
 *
 * 跳转到权限设置页面
 *
 * copy from : https://github.com/getActivity/XXPermissions
 *
 * 只适配了 MIUI 的权限，其他懒得适配了，毕竟似乎也只有 MIUI 把设置动态壁纸也搞了权限
 *
 * */
object Permission {
    private const val TAG = "el, Permission"


    /** 小米手机管家 App 包名  */
    private const val MIUI_MOBILE_MANAGER_APP_PACKAGE_NAME = "com.miui.securitycenter"

    private const val SUB_INTENT_KEY = "sub_intent_key"

    private const val ROM_NAME_MIUI = "ro.miui.ui.version.name"

    private const val WAIT_TIME = 2000L

    var countDownTimer: CountDownTimer? = null

    var hasPermission = false

    fun clickSetWallPaper(context: Context) {
        countDownTimer = object : CountDownTimer(WAIT_TIME, 100) {
            override fun onTick(millisUntilFinished: Long) { }

            override fun onFinish() {
                if (!hasPermission) {
                    Toast.makeText(
                        context,
                        R.string.main_setting_permission_tip,
                        Toast.LENGTH_LONG
                    ).show()
                    context.startActivity(getPermissionPageIntent(context))
                }
            }
        }
        countDownTimer?.start()
    }

    private fun getPermissionPageIntent(context: Context): Intent? {
        // 打开小米的权限设置
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && isMiui() && isMiuiOptimization()) {
            val appPermEditorActionIntent = Intent()
                .setAction("miui.intent.action.APP_PERM_EDITOR")
                .putExtra("extra_pkgname", context.packageName)
            val xiaoMiMobileManagerAppIntent: Intent? =
                getXiaoMiMobileManagerAppIntent(context)
            var intent: Intent? = null
            if (areActivityIntent(context, appPermEditorActionIntent)) {
                intent = appPermEditorActionIntent
            }
            if (areActivityIntent(context, xiaoMiMobileManagerAppIntent)) {
                intent = addSubIntentToMainIntent(intent, xiaoMiMobileManagerAppIntent)
            }
            return intent
        }
        else { // 直接打开应用详情
            return getApplicationDetailsIntent(context)
        }
    }

    private fun isMiuiOptimization(): Boolean {
        try {
            val clazz = Class.forName("android.os.SystemProperties")
            val getMethod = clazz.getMethod(
                "get",
                String::class.java,
                String::class.java
            )
            val ctsValue = getMethod.invoke(clazz, "ro.miui.cts", "").toString()
            val getBooleanMethod = clazz.getMethod(
                "getBoolean",
                String::class.java,
                Boolean::class.javaPrimitiveType
            )
            return java.lang.Boolean.parseBoolean(
                getBooleanMethod.invoke(
                    clazz, "persist.sys.miui_optimization",
                    "1" != ctsValue
                ).toString()
            )
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return true
    }

    private fun isMiui(): Boolean {
        return !TextUtils.isEmpty(getPropertyName(ROM_NAME_MIUI))
    }

    private fun getPropertyName(propertyName: String): String? {
        var result: String? = ""
        if (!TextUtils.isEmpty(propertyName)) {
            result = getSystemProperty(propertyName)
        }
        return result
    }

    private fun getSystemProperty(name: String): String? {
        var prop: String? = getSystemPropertyByShell(name)
        if (!TextUtils.isEmpty(prop)) {
            return prop
        }
        prop = getSystemPropertyByStream(name)
        if (!TextUtils.isEmpty(prop)) {
            return prop
        }
        return if (Build.VERSION.SDK_INT < 28) {
            getSystemPropertyByReflect(name)
        } else prop
    }

    private fun getSystemPropertyByShell(propName: String): String? {
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop $propName")
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            val ret = input.readLine()
            if (ret != null) {
                return ret
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return ""
    }

    private fun getSystemPropertyByStream(key: String): String? {
        try {
            val prop = Properties()
            val `is` = FileInputStream(
                File(Environment.getRootDirectory(), "build.prop")
            )
            prop.load(`is`)
            return prop.getProperty(key, "")
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ""
    }

    @SuppressLint("PrivateApi")
    private fun getSystemPropertyByReflect(key: String): String? {
        try {
            val clz = Class.forName("android.os.SystemProperties")
            val getMethod = clz.getMethod(
                "get",
                String::class.java,
                String::class.java
            )
            return getMethod.invoke(clz, key, "") as String
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return ""
    }

    private fun getApplicationDetailsIntent(context: Context): Intent? {
        var intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = getPackageNameUri(context)
        if (areActivityIntent(context, intent)) {
            return intent
        }
        intent = Intent(Settings.ACTION_APPLICATION_SETTINGS)
        if (areActivityIntent(context, intent)) {
            return intent
        }
        intent = Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)
        return intent
    }

    private fun getPackageNameUri(context: Context?): Uri? {
        return Uri.parse("package:" + context?.packageName)
    }

    private fun getXiaoMiMobileManagerAppIntent(context: Context): Intent? {
        val intent =
            context.packageManager.getLaunchIntentForPackage(MIUI_MOBILE_MANAGER_APP_PACKAGE_NAME)
        return if (areActivityIntent(context, intent)) {
            intent
        } else null
    }

    private fun areActivityIntent(context: Context?, intent: Intent?): Boolean {
        if (intent == null) {
            return false
        }
        // 这里为什么不用 Intent.resolveActivity(intent) != null 来判断呢？
        // 这是因为在 OPPO R7 Plus （Android 5.0）会出现误判，明明没有这个 Activity，却返回了 ComponentName 对象
        val packageManager = context?.packageManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            (packageManager?.queryIntentActivities(
                intent,
                PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_DEFAULT_ONLY.toLong())
            )?.isNotEmpty()) ?: false
        } else (packageManager?.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)?.isNotEmpty()) ?: false
    }

    private fun addSubIntentToMainIntent(mainIntent: Intent?, subIntent: Intent?): Intent? {
        if (mainIntent == null && subIntent != null) {
            return subIntent
        }
        if (subIntent == null) {
            return mainIntent
        }
        val deepSubIntent: Intent? =
            getDeepSubIntent(mainIntent)
        deepSubIntent?.putExtra(SUB_INTENT_KEY, subIntent)
        return mainIntent
    }

    private fun getDeepSubIntent(superIntent: Intent?): Intent? {
        val subIntent: Intent? =
            getSubIntentInMainIntent(superIntent)
        return subIntent?.let { getDeepSubIntent(it) } ?: superIntent
    }

    private fun getSubIntentInMainIntent(mainIntent: Intent?): Intent? {
        val subIntent: Intent? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mainIntent?.getParcelableExtra(
                SUB_INTENT_KEY,
                Intent::class.java
            )
        } else {
            mainIntent?.getParcelableExtra(SUB_INTENT_KEY)
        }
        return subIntent
    }
}