package googleplay.itheima.com.googleplay.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import googleplay.itheima.com.googleplay.BaseApplication;

/**
 * @author TanJJ
 * @time 2017/5/14 23:39
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.utils
 * @des 资源工具类
 */

public class ResourceUtils {

    //获取全局Context
    public static Context getContext() {
        return BaseApplication.getContext();
    }

    //获取图片资源
    public static Drawable getDrawbleResource(int rId) {
        return getContext().getResources().getDrawable(rId);
    }

    //获取包名
    public static String getPackageName() {
        return getContext().getPackageName();
    }

    //获取统一的Handler
    public static Handler getHandler() {
        return BaseApplication.getHandler();
    }

    //执行post
    public static void post(Runnable task) {
        getHandler().post(task);
    }

    //执行延时post
    public static void postDelayed(Runnable task, int delayed) {
        getHandler().postDelayed(task, delayed);
    }

}
