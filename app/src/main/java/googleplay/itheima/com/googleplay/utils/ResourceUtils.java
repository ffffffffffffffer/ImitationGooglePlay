package googleplay.itheima.com.googleplay.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import googleplay.itheima.com.googleplay.BaseApplication;

/**
 * @author TanJJ
 * @time 2017/5/14 23:39
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.utils
 * @des 资源工具类
 * @SVN_Version: $Rev$
 * @UpdateAuthor: $Author$
 * @UpdateTime: $Date$
 * @UpdateDes: TODO
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

}
