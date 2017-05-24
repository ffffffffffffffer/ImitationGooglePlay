package googleplay.itheima.com.googleplay.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

import googleplay.itheima.com.googleplay.R;
import googleplay.itheima.com.googleplay.base.BaseActivity;
import googleplay.itheima.com.googleplay.utils.Constants;
import googleplay.itheima.com.googleplay.utils.ResourceUtils;

/**
 * @author TanJJ
 * @time 2017/5/22 22:06
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.activity
 * @des TODO
 */

public class AppDetailActivity extends BaseActivity {
    @ViewInject(R.id.detail_image)
    ImageView mImageView;
    @ViewInject(R.id.detail_navigation)
    Button mImageView_navigation;
    @ViewInject(R.id.detail_textView)
    TextView mTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_detail);
        initData();
    }

    private void initData() {
        x.view().inject(this);
        //获取传递过来的数据
        Intent intent = getIntent();
        String name = getResources().getString(R.string.app_name);
        String iconUrl = null;
        if (intent != null) {
            name = intent.getStringExtra("name");
            iconUrl = intent.getStringExtra("iconUrl");
        }
        //设置标题
        mTextView.setText(name);
        //加载上一级保存在本地的图片
        x.image().loadFile(Constants.BASE_SERVER + Constants.IMAGE_INTERFACE + iconUrl, ImageOptions.DEFAULT, new
                Callback.CacheCallback<File>() {
                    @Override
                    public boolean onCache(File result) {
                        return false;
                    }

                    @Override
                    public void onSuccess(File result) {
                        Drawable fromPath = Drawable.createFromPath(result.getPath());
                        mImageView.setImageDrawable(fromPath);
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        mImageView.setImageDrawable(ResourceUtils.getDrawbleResource(R.drawable.ic_launcher));
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
        //设置点击navigation返回上一级
        mImageView_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
