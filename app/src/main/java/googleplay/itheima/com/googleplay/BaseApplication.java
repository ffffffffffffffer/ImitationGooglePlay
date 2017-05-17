package googleplay.itheima.com.googleplay;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import org.xutils.x;

/**
 * @author TanJJ
 * @time 2017/5/14 23:06
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay
 * @des 基类Application
 */

public class BaseApplication extends Application {
    private static Context mContext;
    private static Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
        mContext = this;
        mHandler = new Handler();
    }


    //提供外界获取Context
    public static Context getContext() {
        return mContext;
    }

    //提供外界获取Handler
    public static Handler getHandler() {
        return mHandler;
    }
}
