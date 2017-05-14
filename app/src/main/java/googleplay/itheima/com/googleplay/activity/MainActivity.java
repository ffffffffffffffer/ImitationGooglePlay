package googleplay.itheima.com.googleplay.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import googleplay.itheima.com.googleplay.R;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    @ViewInject(R.id.toolbar)
    Toolbar mToolbar;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mToolbar.setTitle("ToolBar");
        mToolbar.setSubtitle("SubToolBar");
        mToolbar.setLogo(R.mipmap.ic_launcher);
//        setSupportActionBar(mToolbar);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
