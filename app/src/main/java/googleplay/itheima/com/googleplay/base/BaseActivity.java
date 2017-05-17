package googleplay.itheima.com.googleplay.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

/**
 * @author TanJJ
 * @time 2017/5/14 23:16
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay
 * @des 基类Activity
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    }
}
