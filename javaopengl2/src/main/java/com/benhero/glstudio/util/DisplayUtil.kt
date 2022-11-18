package com.benhero.glstudio.util

import android.content.Context
import android.view.WindowManager




/**
 * 绘制计算工具类
 *
 * @author Benhero
 */
object DisplayUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    fun getWindowWidth(context: Context):Int{
        val wm = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val width = wm.defaultDisplay.width
        return width
    }
    fun getWindowHeight(context: Context):Int{
        val wm = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val height = wm.defaultDisplay.height
        return height
    }

}
