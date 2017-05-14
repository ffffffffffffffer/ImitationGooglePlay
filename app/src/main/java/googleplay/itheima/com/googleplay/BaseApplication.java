package googleplay.itheima.com.googleplay;

import android.app.Application;

import org.xutils.x;

/**
 * @author TanJJ
 * @time 2017/5/14 23:06
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay
 * @des TODO
 * @SVN_Version: $Rev$
 * @UpdateAuthor: $Author$
 * @UpdateTime: $Date$
 * @UpdateDes: TODO
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
    }
}
