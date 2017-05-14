package googleplay.itheima.com.googleplay.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import googleplay.itheima.com.googleplay.R;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    @ViewInject(R.id.toolbar)
    Toolbar mToolbar;
    @ViewInject(R.id.dl_drawer)
    DrawerLayout mDrawerLayout;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化view
        x.view().inject(this);
        //设置标题
        mToolbar.setTitle(getResources().getString(R.string.app_name));
        mToolbar.setLogo(R.drawable.ic_launcher);
        //定义抽屉开关
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,
                R.string.drawer_close);
        toggle.syncState();
        //设置了这个,连Navigation都不用设置了,还自带动画
        mDrawerLayout.addDrawerListener(toggle);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
